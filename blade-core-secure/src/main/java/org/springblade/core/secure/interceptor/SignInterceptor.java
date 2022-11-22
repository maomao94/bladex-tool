/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.core.secure.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.secure.props.SignSecure;
import org.springblade.core.secure.provider.HttpMethod;
import org.springblade.core.secure.provider.ResponseProvider;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.WebUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * 签名认证拦截器校验
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class SignInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 表达式匹配
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * 授权集合
	 */
	private final List<SignSecure> signSecures;

	/**
	 * 请求时间
	 */
	private final static String TIMESTAMP = "timestamp";

	/**
	 * 随机数
	 */
	private final static String NONCE = "nonce";

	/**
	 * 时间随机数组合加密串
	 */
	private final static String SIGNATURE = "signature";

	/**
	 * sha1加密方式
	 */
	private final static String SHA1 = "sha1";

	/**
	 * md5加密方式
	 */
	private final static String MD5 = "md5";

	/**
	 * 时间差最小值
	 */
	private final static Integer SECOND_MIN = 0;

	/**
	 * 时间差最大值
	 */
	private final static Integer SECOND_MAX = 10;

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		boolean check = signSecures.stream().filter(signSecure -> checkAuth(request, signSecure)).findFirst().map(
			authSecure -> checkSign(authSecure.getCrypto())
		).orElse(Boolean.TRUE);
		if (!check) {
			log.warn("授权认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
			ResponseProvider.write(response);
			return false;
		}
		return true;
	}

	/**
	 * 检测授权
	 */
	private boolean checkAuth(HttpServletRequest request, SignSecure signSecure) {
		return checkMethod(request, signSecure.getMethod()) && checkPath(request, signSecure.getPattern());
	}

	/**
	 * 检测请求方法
	 */
	private boolean checkMethod(HttpServletRequest request, HttpMethod method) {
		return method == HttpMethod.ALL || (
			method != null && method == HttpMethod.of(request.getMethod())
		);
	}

	/**
	 * 检测路径匹配
	 */
	private boolean checkPath(HttpServletRequest request, String pattern) {
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		if (pathInfo != null && pathInfo.length() > 0) {
			servletPath = servletPath + pathInfo;
		}
		return ANT_PATH_MATCHER.match(pattern, servletPath);
	}

	/**
	 * 检测表达式
	 */
	private boolean checkSign(String crypto) {
		try {
			HttpServletRequest request = WebUtil.getRequest();
			if (request == null) {
				return false;
			}
			// 获取头部动态签名信息
			String timestamp = request.getHeader(TIMESTAMP);
			// 判断是否在合法时间段
			long seconds = Duration.between(new Date(Func.toLong(timestamp)).toInstant(), DateUtil.now().toInstant()).getSeconds();
			if (seconds < SECOND_MIN || seconds > SECOND_MAX) {
				log.warn("授权认证失败，错误信息：{}", "请求时间戳非法");
				return false;
			}
			String nonce = request.getHeader(NONCE);
			String signature = request.getHeader(SIGNATURE);
			// 加密签名比对，可自行拓展加密规则
			String sign;
			if (crypto.equals(MD5)) {
				sign = DigestUtil.md5Hex(timestamp + nonce);
			} else if (crypto.equals(SHA1)) {
				sign = DigestUtil.sha1Hex(timestamp + nonce);
			} else {
				sign = DigestUtil.sha1Hex(timestamp + nonce);
			}
			return sign.equalsIgnoreCase(signature);
		} catch (Exception e) {
			log.warn("授权认证失败，错误信息：{}", e.getMessage());
			return false;
		}
	}


}
