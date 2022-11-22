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
package org.springblade.core.prometheus.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springblade.core.launch.props.BladePropertySource;
import org.springblade.core.prometheus.endpoint.AgentEndpoint;
import org.springblade.core.prometheus.endpoint.ServiceEndpoint;
import org.springblade.core.prometheus.service.RegistrationService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus配置类
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@BladePropertySource(value = "classpath:/blade-prometheus.yml")
public class PrometheusConfiguration {

	@Bean
	public RegistrationService registrationService(DiscoveryClient discoveryClient) {
		return new RegistrationService(discoveryClient);
	}

	@Bean
	public AgentEndpoint agentController(NacosDiscoveryProperties properties) {
		return new AgentEndpoint(properties);
	}

	@Bean
	public ServiceEndpoint serviceController(RegistrationService registrationService) {
		return new ServiceEndpoint(registrationService);
	}

}
