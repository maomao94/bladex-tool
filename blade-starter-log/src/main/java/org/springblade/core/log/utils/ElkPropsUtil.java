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

import org.springblade.core.tool.utils.StringPool;

import java.util.Properties;

/**
 * Elk配置工具
 *
 * @author Chill
 */
public class ElkPropsUtil {

	/**
	 * 获取elk服务地址
	 *
	 * @return 服务地址
	 */
	public static String getDestination() {
		Properties props = System.getProperties();
		return props.getProperty("blade.log.elk.destination", StringPool.EMPTY);
	}

}
