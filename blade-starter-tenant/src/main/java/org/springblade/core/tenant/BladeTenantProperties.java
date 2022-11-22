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
package org.springblade.core.tenant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置
 *
 * @author Chill
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "blade.tenant")
public class BladeTenantProperties {

	/**
	 * 是否增强多租户
	 */
	private Boolean enhance = Boolean.FALSE;

	/**
	 * 是否开启授权码校验
	 */
	private Boolean license = Boolean.FALSE;

	/**
	 * 是否开启动态数据源功能
	 */
	private Boolean dynamicDatasource = Boolean.FALSE;

	/**
	 * 是否开启动态数据源全局扫描
	 */
	private Boolean dynamicGlobal = Boolean.FALSE;

	/**
	 * 多租户字段名称
	 */
	private String column = "tenant_id";

	/**
	 * 需要排除进行自定义的多租户表
	 */
	private List<String> excludeTables = new ArrayList<>();

}
