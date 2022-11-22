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
package org.springblade.core.http.test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理设置
 *
 * @author L.cm
 */
@Slf4j
public class BladeProxySelector extends ProxySelector {

	@Override
	public List<Proxy> select(URI uri) {
		// 注意代理都不可用
		List<Proxy> proxyList = new ArrayList<>();
		proxyList.add(getProxy("127.0.0.1", 8080));
		proxyList.add(getProxy("127.0.0.1", 8081));
		proxyList.add(getProxy("127.0.0.1", 8082));
		proxyList.add(getProxy("127.0.0.1", 3128));
		return proxyList;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress address, IOException ioe) {
		// 注意：经过测试，此处不会触发
		log.error("ConnectFailed uri:{}, address:{}, ioe:{}", uri, address, ioe);
	}

	/**
	 * 构造 Proxy
	 *
	 * @param host host
	 * @param port 端口
	 * @return Proxy 对象
	 */
	public static Proxy getProxy(String host, int port) {
		return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
	}
}
