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
package org.springblade.core.prometheus.endpoint;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import org.springblade.core.auto.annotation.AutoIgnore;
import org.springblade.core.prometheus.data.Agent;
import org.springblade.core.prometheus.data.Config;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * consul agent api
 *
 * @author L.cm
 */
@AutoIgnore
@RestController
@RequiredArgsConstructor
public class AgentEndpoint {
	private final NacosDiscoveryProperties properties;

	@GetMapping(value = "/v1/agent/self", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Agent getNodes() {
		Config config = Config.builder().dataCenter(properties.getGroup()).build();
		return Agent.builder().config(config).build();
	}

}
