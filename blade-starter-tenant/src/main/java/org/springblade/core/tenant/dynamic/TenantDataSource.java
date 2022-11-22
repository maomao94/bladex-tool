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
package org.springblade.core.tenant.dynamic;

import lombok.Data;

/**
 * 租户数据源
 *
 * @author Chill
 */
@Data
public class TenantDataSource {

	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 数据源ID
	 */
	private String datasourceId;
	/**
	 * 驱动类
	 */
	private String driverClass;
	/**
	 * 数据库链接
	 */
	private String url;
	/**
	 * 数据库账号名
	 */
	private String username;
	/**
	 * 数据库密码
	 */
	private String password;

}
