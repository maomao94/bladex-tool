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

import okhttp3.FormBody;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 表单构造器
 *
 * @author L.cm
 */
public class FormBuilder {
	private final HttpRequest request;
	private final FormBody.Builder formBuilder;

	FormBuilder(HttpRequest request) {
		this.request = request;
		this.formBuilder = new FormBody.Builder();
	}

	public FormBuilder add(String name, @Nullable Object value) {
		this.formBuilder.add(name, HttpRequest.handleValue(value));
		return this;
	}

	public FormBuilder addMap(@Nullable Map<String, Object> formMap) {
		if (formMap != null && !formMap.isEmpty()) {
			formMap.forEach(this::add);
		}
		return this;
	}

	public FormBuilder addEncoded(String name, @Nullable Object encodedValue) {
		this.formBuilder.addEncoded(name, HttpRequest.handleValue(encodedValue));
		return this;
	}

	public HttpRequest build() {
		FormBody formBody = formBuilder.build();
		this.request.form(formBody);
		return this.request;
	}

	public Exchange execute() {
		return this.build().execute();
	}

	public AsyncCall async() {
		return this.build().async();
	}
}
