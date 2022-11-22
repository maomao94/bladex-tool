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
package org.springblade.core.context.config;

import org.springblade.core.context.BladeContext;
import org.springblade.core.context.BladeHttpHeadersGetter;
import org.springblade.core.context.BladeServletContext;
import org.springblade.core.context.ServletHttpHeadersGetter;
import org.springblade.core.context.props.BladeContextProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * blade 服务上下文配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(BladeContextProperties.class)
public class BladeContextAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public BladeHttpHeadersGetter bladeHttpHeadersGetter(BladeContextProperties contextProperties) {
		return new ServletHttpHeadersGetter(contextProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public BladeContext bladeContext(BladeContextProperties contextProperties, BladeHttpHeadersGetter httpHeadersGetter) {
		return new BladeServletContext(contextProperties, httpHeadersGetter);
	}

}
