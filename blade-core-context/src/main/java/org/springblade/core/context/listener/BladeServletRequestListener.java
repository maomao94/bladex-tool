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
package org.springblade.core.context.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springblade.core.context.BladeHttpHeadersGetter;
import org.springblade.core.context.props.BladeContextProperties;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.core.tool.utils.ThreadLocalUtil;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet 请求监听器
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class BladeServletRequestListener implements ServletRequestListener {
	private final BladeContextProperties contextProperties;
	private final BladeHttpHeadersGetter httpHeadersGetter;

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
		// MDC 获取透传的 变量
		BladeContextProperties.Headers headers = contextProperties.getHeaders();
		String requestId = request.getHeader(headers.getRequestId());
		if (StringUtil.isNotBlank(requestId)) {
			MDC.put(BladeConstant.MDC_REQUEST_ID_KEY, requestId);
		}
		String accountId = request.getHeader(headers.getAccountId());
		if (StringUtil.isNotBlank(accountId)) {
			MDC.put(BladeConstant.MDC_ACCOUNT_ID_KEY, accountId);
		}
		String tenantId = request.getHeader(headers.getTenantId());
		if (StringUtil.isNotBlank(tenantId)) {
			MDC.put(BladeConstant.MDC_TENANT_ID_KEY, tenantId);
		}
		// 处理 context，直接传递 request，因为 spring 中的尚未初始化完成
		HttpHeaders httpHeaders = httpHeadersGetter.get(request);
		ThreadLocalUtil.put(BladeConstant.CONTEXT_KEY, httpHeaders);
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// 会话销毁时，清除上下文
		ThreadLocalUtil.clear();
		// 会话销毁时，清除 mdc
		MDC.remove(BladeConstant.MDC_REQUEST_ID_KEY);
		MDC.remove(BladeConstant.MDC_ACCOUNT_ID_KEY);
		MDC.remove(BladeConstant.MDC_TENANT_ID_KEY);
	}

}
