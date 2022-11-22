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
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

/**
 * 异步处理
 *
 * @author L.cm
 */
@ParametersAreNonnullByDefault
public class AsyncCallback implements Callback {
	private final AsyncCall asyncCall;

	AsyncCallback(AsyncCall asyncCall) {
		this.asyncCall = asyncCall;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		asyncCall.onFailure(call.request(), e);
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		try (HttpResponse httpResponse = new HttpResponse(response)) {
			asyncCall.onResponse(httpResponse);
			if (response.isSuccessful()) {
				asyncCall.onSuccessful(httpResponse);
			} else {
				asyncCall.onFailure(call.request(), new IOException(httpResponse.message()));
			}
		}
	}

}
