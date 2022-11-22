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
package org.springblade.core.http.util;

import lombok.extern.slf4j.Slf4j;
import org.springblade.core.http.Exchange;
import org.springblade.core.http.FormBuilder;
import org.springblade.core.http.HttpRequest;

import java.util.Map;

/**
 * Http请求工具类
 *
 * @author Chill
 */
@Slf4j
public class HttpUtil {

	/**
	 * GET
	 *
	 * @param url     请求的url
	 * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
	 * @return String
	 */
	public static String get(String url, Map<String, Object> queries) {
		return get(url, null, queries);
	}

	/**
	 * GET
	 *
	 * @param url     请求的url
	 * @param header  请求头
	 * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
	 * @return String
	 */
	public static String get(String url, Map<String, String> header, Map<String, Object> queries) {
		// 添加请求头
		HttpRequest httpRequest = HttpRequest.get(url);
		if (header != null && header.keySet().size() > 0) {
			header.forEach(httpRequest::addHeader);
		}
		// 添加参数
		httpRequest.queryMap(queries);
		return httpRequest.execute().asString();
	}

	/**
	 * POST
	 *
	 * @param url    请求的url
	 * @param params post form 提交的参数
	 * @return String
	 */
	public static String post(String url, Map<String, Object> params) {
		return exchange(url, null, params).asString();
	}

	/**
	 * POST
	 *
	 * @param url    请求的url
	 * @param header 请求头
	 * @param params post form 提交的参数
	 * @return String
	 */
	public static String post(String url, Map<String, String> header, Map<String, Object> params) {
		return exchange(url, header, params).asString();
	}

	/**
	 * POST请求发送JSON数据
	 *
	 * @param url  请求的url
	 * @param json 请求的json串
	 * @return String
	 */
	public static String postJson(String url, String json) {
		return exchange(url, null, json).asString();
	}

	/**
	 * POST请求发送JSON数据
	 *
	 * @param url    请求的url
	 * @param header 请求头
	 * @param json   请求的json串
	 * @return String
	 */
	public static String postJson(String url, Map<String, String> header, String json) {
		return exchange(url, header, json).asString();
	}

	public static Exchange exchange(String url, Map<String, String> header, Map<String, Object> params) {
		HttpRequest httpRequest = HttpRequest.post(url);
		//添加请求头
		if (header != null && header.keySet().size() > 0) {
			header.forEach(httpRequest::addHeader);
		}
		FormBuilder formBuilder = httpRequest.formBuilder();
		//添加参数
		if (params != null && params.keySet().size() > 0) {
			params.forEach(formBuilder::add);
		}
		return formBuilder.execute();
	}

	public static Exchange exchange(String url, Map<String, String> header, String content) {
		HttpRequest httpRequest = HttpRequest.post(url);
		//添加请求头
		if (header != null && header.keySet().size() > 0) {
			header.forEach(httpRequest::addHeader);
		}
		return httpRequest.bodyString(content).execute();
	}

}
