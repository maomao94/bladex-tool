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
package org.springblade.core.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端校验配置
 *
 * @author Chill
 */
@Data
@ConfigurationProperties("blade.secure")
public class BladeSecureProperties {

	/**
	 * 开启鉴权规则
	 */
	private Boolean enabled = false;

	/**
	 * 鉴权放行请求
	 */
	private final List<String> skipUrl = new ArrayList<>();

	/**
	 * 开启授权规则
	 */
	private Boolean authEnabled = true;

	/**
	 * 授权配置
	 */
	private final List<AuthSecure> auth = new ArrayList<>();

	/**
	 * 开启基础认证规则
	 */
	private Boolean basicEnabled = true;

	/**
	 * 基础认证配置
	 */
	private final List<BasicSecure> basic = new ArrayList<>();

	/**
	 * 开启签名认证规则
	 */
	private Boolean signEnabled = true;

	/**
	 * 签名认证配置
	 */
	private final List<SignSecure> sign = new ArrayList<>();

	/**
	 * 开启客户端规则
	 */
	private Boolean clientEnabled = true;

	/**
	 * 客户端配置
	 */
	private final List<ClientSecure> client = new ArrayList<>();

}
