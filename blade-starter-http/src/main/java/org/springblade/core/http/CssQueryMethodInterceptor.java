/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package org.springblade.core.http;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 代理模型
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class CssQueryMethodInterceptor implements MethodInterceptor {
	private final Class<?> clazz;
	private final Element element;

	@Nullable
	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		// 只处理 get 方法 is
		String name = method.getName();
		if (!StringUtil.startsWithIgnoreCase(name, StringPool.GET)) {
			return methodProxy.invokeSuper(object, args);
		}
		Field field = clazz.getDeclaredField(StringUtil.firstCharToLower(name.substring(3)));
		CssQuery cssQuery = field.getAnnotation(CssQuery.class);
		// 没有注解，不代理
		if (cssQuery == null) {
			return methodProxy.invokeSuper(object, args);
		}
		Class<?> returnType = method.getReturnType();
		boolean isColl = Collection.class.isAssignableFrom(returnType);
		String cssQueryValue = cssQuery.value();
		// 是否为 bean 中 bean
		boolean isInner = cssQuery.inner();
		if (isInner) {
			return proxyInner(cssQueryValue, method, returnType, isColl);
		}
		Object proxyValue = proxyValue(cssQueryValue, cssQuery, returnType, isColl);
		if (String.class.isAssignableFrom(returnType)) {
			return proxyValue;
		}
		// 用于读取 field 上的注解
		TypeDescriptor typeDescriptor = new TypeDescriptor(field);
		return ConvertUtil.convert(proxyValue, typeDescriptor);
	}

	@Nullable
	private Object proxyValue(String cssQueryValue, CssQuery cssQuery, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			if (elements.isEmpty()) {
				return valueList;
			}
			for (Element select : elements) {
				String value = getValue(select, cssQuery);
				if (value != null) {
					valueList.add(value);
				}
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return getValue(select, cssQuery);
	}

	private Object proxyInner(String cssQueryValue, Method method, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
			Class<?> innerType = resolvableType.getGeneric(0).resolve();
			if (innerType == null) {
				throw new IllegalArgumentException("Class " + returnType + " 读取泛型失败。");
			}
			for (Element select : elements) {
				valueList.add(DomMapper.readValue(select, innerType));
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return DomMapper.readValue(select, returnType);
	}

	@Nullable
	private String getValue(@Nullable Element element, CssQuery cssQuery) {
		if (element == null) {
			return null;
		}
		// 读取的属性名
		String attrName = cssQuery.attr();
		// 读取的值
		String attrValue;
		if (StringUtil.isBlank(attrName)) {
			attrValue = element.outerHtml();
		} else if ("html".equalsIgnoreCase(attrName)) {
			attrValue = element.html();
		} else if ("text".equalsIgnoreCase(attrName)) {
			attrValue = getText(element);
		} else if ("allText".equalsIgnoreCase(attrName)) {
			attrValue = element.text();
		} else {
			attrValue = element.attr(attrName);
		}
		// 判断是否需要正则处理
		String regex = cssQuery.regex();
		if (StringUtil.isBlank(attrValue) || StringUtil.isBlank(regex)) {
			return attrValue;
		}
		// 处理正则表达式
		return getRegexValue(regex, cssQuery.regexGroup(), attrValue);
	}

	@Nullable
	private String getRegexValue(String regex, int regexGroup, String value) {
		// 处理正则表达式
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(value);
		if (!matcher.find()) {
			return null;
		}
		// 正则 group
		if (regexGroup > CssQuery.DEFAULT_REGEX_GROUP) {
			return matcher.group(regexGroup);
		}
		return matcher.group();
	}

	private String getText(Element element) {
		return element.childNodes().stream()
			.filter(node -> node instanceof TextNode)
			.map(node -> (TextNode) node)
			.map(TextNode::text)
			.collect(Collectors.joining());
	}

	private Collection<Object> newColl(Class<?> returnType) {
		return Set.class.isAssignableFrom(returnType) ? new HashSet<>() : new ArrayList<>();
	}
}
