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
package org.springblade.core.sms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 通知内容
 *
 * @author Chill
 */
@Data
@Accessors(chain = true)
public class SmsData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造器
	 *
	 * @param params 参数列表
	 */
	public SmsData(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * 变量key,用于从参数列表获取变量值
	 */
	private String key;

	/**
	 * 参数列表
	 */
	private Map<String, String> params;

}
