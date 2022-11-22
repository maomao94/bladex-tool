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
package org.springblade.core.context;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpHeaders 获取器，用于跨服务和线程的传递，
 * <p>
 * 暂时不支持 webflux。
 *
 * @author L.cm
 */
public interface BladeHttpHeadersGetter {

	/**
	 * 获取 HttpHeaders
	 *
	 * @return HttpHeaders
	 */
	@Nullable
	HttpHeaders get();

	/**
	 * 获取 HttpHeaders
	 *
	 * @param request 请求
	 * @return HttpHeaders
	 */
	@Nullable
	HttpHeaders get(HttpServletRequest request);

}
