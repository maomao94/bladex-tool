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
package org.springblade.core.ribbon.predicate;

import com.netflix.loadbalancer.Server;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 利用反射，让 server 直接支持 nacos、consul、Eureka
 *
 * @author L.cm
 */
public class MetadataServer {
	private final static ConcurrentMap<Class<?>, Method> METHOD_MAP = new ConcurrentHashMap<>(1);
	private final static List<Class<?>> NO_METHOD_LIST = new ArrayList<>();
	private final Server server;
	private final Method method;

	public MetadataServer(Server server) {
		this.server = server;
		this.method = findMetadataMethod(server.getClass());
	}

	private static Method findMetadataMethod(Class<?> serverClass) {
		// 缓存中有方法
		Method method = METHOD_MAP.get(serverClass);
		if (method != null) {
			return method;
		}
		// 没有方法
		if (NO_METHOD_LIST.contains(serverClass)) {
			return null;
		}
		// 查找 getMetadata 方法
		method = ReflectionUtils.findMethod(serverClass, "getMetadata");
		if (method == null) {
			NO_METHOD_LIST.add(serverClass);
		} else {
			METHOD_MAP.put(serverClass, method);
		}
		return method;
	}

	/**
	 * 判断是否有 getMetadata 方法
	 *
	 * @return bool
	 */
	public boolean hasMetadata() {
		return method != null;
	}

	/**
	 * 获取 Metadata
	 *
	 * @return Metadata 信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMetadata() {
		return (Map<String, String>) ReflectionUtils.invokeMethod(method, server);
	}

}
