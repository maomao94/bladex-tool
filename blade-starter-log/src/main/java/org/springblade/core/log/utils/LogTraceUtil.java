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
package org.springblade.core.log.utils;

import org.slf4j.MDC;
import org.springblade.core.tool.utils.StringUtil;

/**
 * 日志追踪工具类
 *
 * @author Chill
 */
public class LogTraceUtil {
	private static final String UNIQUE_ID = "traceId";

	/**
	 * 获取日志追踪id格式
	 */
	public static String getTraceId() {
		return StringUtil.randomUUID();
	}

	/**
	 * 插入traceId
	 */
	public static boolean insert() {
		MDC.put(UNIQUE_ID, getTraceId());
		return true;
	}

	/**
	 * 移除traceId
	 */
	public static boolean remove() {
		MDC.remove(UNIQUE_ID);
		return true;
	}

}
