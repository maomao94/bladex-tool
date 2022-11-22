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
package org.springblade.core.swagger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.springblade.core.launch.constant.TokenConstant;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiKey;

import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger工具类
 *
 * @author Chill
 */
public class SwaggerUtil {

	/**
	 * 获取包集合
	 *
	 * @param basePackages 多个包名集合
	 */
	public static Predicate<RequestHandler> basePackages(final List<String> basePackages) {
		return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
	}

	private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages) {
		return input -> {
			// 循环判断匹配
			for (String strPackage : basePackages) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.fromNullable(input.declaringClass());
	}


	public static ApiKey clientInfo() {
		return new ApiKey("ClientInfo", "Authorization", "header");
	}

	public static ApiKey bladeAuth() {
		return new ApiKey("BladeAuth", TokenConstant.HEADER, "header");
	}

	public static ApiKey bladeTenant() {
		return new ApiKey("TenantId", "Tenant-Id", "header");
	}

}
