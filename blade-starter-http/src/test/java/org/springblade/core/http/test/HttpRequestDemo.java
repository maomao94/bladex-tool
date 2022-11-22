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
package org.springblade.core.http.test;

import com.fasterxml.jackson.databind.JsonNode;
import org.springblade.core.http.HttpRequest;
import org.springblade.core.http.LogLevel;
import org.springblade.core.http.ResponseSpec;
import okhttp3.Cookie;
import org.springblade.core.tool.utils.Base64Util;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

/**
 * This example of blade http
 *
 * @author L.cm
 */
public class HttpRequestDemo {

	public void doc() {
		// 设定全局日志级别 NONE，BASIC，HEADERS，BODY， 默认：NONE
		HttpRequest.setGlobalLog(LogLevel.BODY);

		// 同步请求 url，方法支持 get、post、patch、put、delete
		HttpRequest.get("https://www.baidu.com")
			.log(LogLevel.BASIC)             //设定本次的日志级别，优先于全局
			.addHeader("x-account-id", "blade001") // 添加 header
			.addCookie(new Cookie.Builder()  // 添加 cookie
				.name("sid")
				.value("blade_user_001")
				.build()
			)
			.query("q", "blade") //设置 url 参数，默认进行 url encode
			.queryEncoded("name", "encodedValue")
			.formBuilder()    // 表单构造器，同类 multipartFormBuilder 文件上传表单
			.add("id", 123123) // 表单参数
			.execute()// 发起请求
			.asJsonNode();
		// 结果集转换，注：如果网络异常等会直接抛出异常。
		// 同类的方法有 asString、asBytes
		// json 类响应：asJsonNode、asObject、asList、asMap，采用 jackson 处理
		// xml、html响应：asDocument，采用的 jsoup 处理
		// file 文件：toFile

		// 同步
		String html = HttpRequest.post("https://www.baidu.com")
			.execute()
			.onSuccess(ResponseSpec::asString);// 处理响应，有网络异常等直接返回 null

		// 同步
		String text = HttpRequest.patch("https://www.baidu.com")
			.execute()
			.onSuccess(ResponseSpec::asString);
		// onSuccess http code in [200..300) 处理响应，有网络异常等直接返回 null

		// 发送异步请求
		HttpRequest.delete("https://www.baidu.com")
			.async() // 开启异步
			.onFailed((request, e) -> {    // 异常时的处理
				e.printStackTrace();
			})
			.onSuccessful(responseSpec -> { // 消费响应成功 http code in [200..300)
				// 注意：响应结果流只能读一次
				JsonNode jsonNode = responseSpec.asJsonNode();
			});
	}

	public static void main(String[] args) {
		// 设定全局日志级别
		HttpRequest.setGlobalLog(LogLevel.BODY);

		// 同步，异常时 返回 null
		String html = HttpRequest.get("https://www.baidu.com")
			.connectTimeout(Duration.ofSeconds(1000))
			.query("test", "a")
			.query("name", "張三")
			.query("x", 1)
			.query("abd", Base64Util.encode("123&$#%"))
			.queryEncoded("abc", Base64Util.encode("123&$#%"))
			.execute()
			.onFailed(((request, e) -> {
				e.printStackTrace();
			}))
			.onSuccess(ResponseSpec::asString);
		System.out.println(html);

		// 同步调用，返回 Optional，异常时返回 Optional.empty()
		Optional<String> opt = HttpRequest.post(URI.create("https://www.baidu.com"))
			.bodyString("Important stuff")
			.formBuilder()
			.add("a", "b")
			.execute()
			.onSuccessOpt(ResponseSpec::asString);

		// 同步，成功时消费（处理） response
		HttpRequest.post("https://www.baidu.com/some-form")
			.addHeader("X-Custom-header", "stuff")
			.execute()
			.onFailed((request, e) -> {
				e.printStackTrace();
			})
			.onSuccessful(ResponseSpec::asString);

		// async，异步执行结果，失败时打印堆栈
		HttpRequest.get("https://www.baidu.com/some-form")
			.async()
			.onFailed((request, e) -> {
				e.printStackTrace();
			})
			.onSuccessful(System.out::println);
	}

}
