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
package org.springblade.core.ribbon.support;

import org.springblade.core.ribbon.rule.DiscoveryEnabledRule;
import org.springblade.core.ribbon.rule.MetadataAwareRule;
import org.springblade.core.ribbon.utils.BeanUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Blade ribbon rule auto configuration.
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@EnableConfigurationProperties(BladeRibbonRuleProperties.class)
public class BladeRibbonRuleAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = BladeRibbonRuleProperties.PROPERTIES_PREFIX + ".enabled", matchIfMissing = true)
	public static class MetadataAwareRuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		public DiscoveryEnabledRule discoveryEnabledRule() {
			return new MetadataAwareRule();
		}
	}

	@Bean
	public BeanUtil beanUtil(){
		return new BeanUtil();
	}

}
