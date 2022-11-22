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
package org.springblade.core.cloud.http;

import lombok.Getter;
import lombok.Setter;
import org.springblade.core.launch.log.BladeLogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.concurrent.TimeUnit;

/**
 * http 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("blade.http")
public class BladeHttpProperties {
	/**
	 * 最大连接数，默认：200
	 */
	private int maxConnections = 200;
	/**
	 * 连接存活时间，默认：900L
	 */
	private long timeToLive = 900L;
	/**
	 * 连接池存活时间单位，默认：秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	/**
	 * 链接超时，默认：2000毫秒
	 */
	private int connectionTimeout = 2000;
	/**
	 * 是否支持重定向，默认：true
	 */
	private boolean followRedirects = true;
	/**
	 * 关闭证书校验
	 */
	private boolean disableSslValidation = true;
	/**
	 * 日志级别
	 */
	private BladeLogLevel level = BladeLogLevel.NONE;
}
