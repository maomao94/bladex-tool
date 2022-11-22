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

import okhttp3.Call;
import okhttp3.Request;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异步执行器
 *
 * @author L.cm
 */
public class AsyncCall {
	private final static Consumer<ResponseSpec> DEFAULT_CONSUMER = (r) -> {};
	private final static BiConsumer<Request, IOException> DEFAULT_FAIL_CONSUMER = (r, e) -> {};
	private final Call call;
	private Consumer<ResponseSpec> successConsumer;
	private Consumer<ResponseSpec> responseConsumer;
	private BiConsumer<Request, IOException> failedBiConsumer;

	AsyncCall(Call call) {
		this.call = call;
		this.successConsumer = DEFAULT_CONSUMER;
		this.responseConsumer = DEFAULT_CONSUMER;
		this.failedBiConsumer = DEFAULT_FAIL_CONSUMER;
	}

	public void onSuccessful(Consumer<ResponseSpec> consumer) {
		this.successConsumer = consumer;
		this.execute();
	}

	public void onResponse(Consumer<ResponseSpec> consumer) {
		this.responseConsumer = consumer;
		this.execute();
	}

	public AsyncCall onFailed(BiConsumer<Request, IOException> biConsumer) {
		this.failedBiConsumer = biConsumer;
		return this;
	}

	private void execute() {
		call.enqueue(new AsyncCallback(this));
	}

	void onResponse(HttpResponse httpResponse) {
		responseConsumer.accept(httpResponse);
	}

	void onSuccessful(HttpResponse httpResponse) {
		successConsumer.accept(httpResponse);
	}

	void onFailure(Request request, IOException e) {
		failedBiConsumer.accept(request, e);
	}
}
