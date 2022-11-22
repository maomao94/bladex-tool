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
package org.springblade.core.ribbon.predicate;

import org.apache.commons.lang.StringUtils;
import org.springblade.core.ribbon.support.BladeRibbonRuleProperties;
import org.springblade.core.ribbon.utils.BeanUtil;

import java.util.Map;

/**
 * 基于 Metadata 的服务筛选
 *
 * @author L.cm
 */
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {
	public static final MetadataAwarePredicate INSTANCE = new MetadataAwarePredicate();

	@Override
	protected boolean apply(MetadataServer server) {
		// 获取配置
		BladeRibbonRuleProperties properties = BeanUtil.getBean(BladeRibbonRuleProperties.class);
		// 服务里的配置
		String localTag = properties.getTag();
		if (StringUtils.isBlank(localTag)) {
			return true;
		}
		Map<String, String> metadata = server.getMetadata();
		// 本地有 tag，服务没有，返回 false
		String metaDataTag = metadata.get("tag");
		if (StringUtils.isBlank(metaDataTag)) {
			return false;
		}
		return metaDataTag.equals(localTag);
	}

}
