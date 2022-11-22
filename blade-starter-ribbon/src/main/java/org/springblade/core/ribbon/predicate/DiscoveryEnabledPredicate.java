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

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import org.springframework.lang.Nullable;

/**
 * 过滤服务
 *
 * @author L.cm
 */
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean apply(@Nullable PredicateKey input) {
		if (input == null) {
			return false;
		}
		MetadataServer metadataServer = new MetadataServer(input.getServer());
		// 支持 metadata 进行判断
		if (metadataServer.hasMetadata()) {
			return apply(metadataServer);
		}
		return false;
	}

	/**
	 * Returns whether the specific {@link MetadataServer} matches this predicate.
	 *
	 * @param server the discovered server
	 * @return whether the server matches the predicate
	 */
	protected abstract boolean apply(MetadataServer server);
}
