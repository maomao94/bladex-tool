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
package org.springblade.core.secure.handler;

import org.springblade.core.secure.interceptor.*;
import org.springblade.core.secure.props.AuthSecure;
import org.springblade.core.secure.props.BasicSecure;
import org.springblade.core.secure.props.SignSecure;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

/**
 * Secure处理器
 *
 * @author Chill
 */
public class SecureHandlerHandler implements ISecureHandler {

	@Override
	public HandlerInterceptorAdapter tokenInterceptor() {
		return new TokenInterceptor();
	}

	@Override
	public HandlerInterceptorAdapter authInterceptor(List<AuthSecure> authSecures) {
		return new AuthInterceptor(authSecures);
	}

	@Override
	public HandlerInterceptorAdapter basicInterceptor(List<BasicSecure> basicSecures) {
		return new BasicInterceptor(basicSecures);
	}

	@Override
	public HandlerInterceptorAdapter signInterceptor(List<SignSecure> signSecures) {
		return new SignInterceptor(signSecures);
	}

	@Override
	public HandlerInterceptorAdapter clientInterceptor(String clientId) {
		return new ClientInterceptor(clientId);
	}

}
