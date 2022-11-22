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

import org.springblade.core.secure.props.AuthSecure;
import org.springblade.core.secure.props.BasicSecure;
import org.springblade.core.secure.props.SignSecure;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

/**
 * secure 拦截器集合
 *
 * @author Chill
 */
public interface ISecureHandler {

	/**
	 * token拦截器
	 *
	 * @return tokenInterceptor
	 */
	HandlerInterceptorAdapter tokenInterceptor();

	/**
	 * auth拦截器
	 *
	 * @param authSecures 授权集合
	 * @return HandlerInterceptorAdapter
	 */
	HandlerInterceptorAdapter authInterceptor(List<AuthSecure> authSecures);

	/**
	 * basic拦截器
	 *
	 * @param basicSecures 基础认证集合
	 * @return HandlerInterceptorAdapter
	 */
	HandlerInterceptorAdapter basicInterceptor(List<BasicSecure> basicSecures);

	/**
	 * sign拦截器
	 *
	 * @param signSecures 签名认证集合
	 * @return HandlerInterceptorAdapter
	 */
	HandlerInterceptorAdapter signInterceptor(List<SignSecure> signSecures);

	/**
	 * client拦截器
	 *
	 * @param clientId 客户端id
	 * @return clientInterceptor
	 */
	HandlerInterceptorAdapter clientInterceptor(String clientId);

}
