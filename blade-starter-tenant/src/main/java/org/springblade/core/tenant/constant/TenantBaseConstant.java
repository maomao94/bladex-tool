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
package org.springblade.core.tenant.constant;

/**
 * 租户常量.
 *
 * @author Chill
 */
public interface TenantBaseConstant {

	/**
	 * 租户数据源缓存名
	 */
	String TENANT_DATASOURCE_CACHE = "blade:datasource";

	/**
	 * 租户数据源缓存键
	 */
	String TENANT_DATASOURCE_KEY = "tenant:id:";

	/**
	 * 租户数据源缓存键
	 */
	String TENANT_DATASOURCE_EXIST_KEY = "tenant:exist:";

	/**
	 * 租户动态数据源键
	 */
	String TENANT_DYNAMIC_DATASOURCE_PROP = "blade.tenant.dynamic-datasource";

	/**
	 * 租户全局动态数据源切面键
	 */
	String TENANT_DYNAMIC_GLOBAL_PROP = "blade.tenant.dynamic-global";

	/**
	 * 租户是否存在数据源
	 */
	String TENANT_DATASOURCE_EXIST_STATEMENT = "select datasource_id from blade_tenant WHERE is_deleted = 0 AND tenant_id = ?";

	/**
	 * 租户数据源基础SQL
	 */
	String TENANT_DATASOURCE_BASE_STATEMENT = "SELECT tenant_id as tenantId, driver_class as driverClass, url, username, password from blade_tenant tenant LEFT JOIN blade_datasource datasource ON tenant.datasource_id = datasource.id ";

	/**
	 * 租户单数据源SQL
	 */
	String TENANT_DATASOURCE_SINGLE_STATEMENT = TENANT_DATASOURCE_BASE_STATEMENT + "WHERE tenant.is_deleted = 0 AND tenant.tenant_id = ?";

	/**
	 * 租户集动态数据源SQL
	 */
	String TENANT_DATASOURCE_GROUP_STATEMENT = TENANT_DATASOURCE_BASE_STATEMENT + "WHERE tenant.is_deleted = 0";

	/**
	 * 租户未找到返回信息
	 */
	String TENANT_DATASOURCE_NOT_FOUND = "未找到租户信息,数据源加载失败!";

}
