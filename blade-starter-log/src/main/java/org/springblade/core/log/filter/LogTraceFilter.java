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
package org.springblade.core.log.filter;

import org.springblade.core.log.utils.LogTraceUtil;

import javax.servlet.*;
import java.io.IOException;

/**
 * 日志追踪过滤器
 *
 * @author Chill
 */
public class LogTraceFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean flag = LogTraceUtil.insert();
		try {
			chain.doFilter(request, response);
		} finally {
			if (flag) {
				LogTraceUtil.remove();
			}
		}
	}

	@Override
	public void destroy() {
	}

}
