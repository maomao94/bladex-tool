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

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.AllArgsConstructor;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Set;

import static org.springblade.core.tenant.constant.TenantBaseConstant.*;

/**
 * 租户数据源核心处理类
 *
 * @author Chill
 */
@AllArgsConstructor
public class TenantDataSourceHolder {

	private final DataSource dataSource;
	private final DataSourceCreator dataSourceCreator;
	private final JdbcTemplate jdbcTemplate;

	/**
	 * 数据源缓存处理
	 *
	 * @param tenantId 租户ID
	 */
	public void handleDataSource(String tenantId) {
		// 获取储存的数据源集合
		DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
		Set<String> keys = ds.getCurrentDataSources().keySet();
		// 配置不存在则动态添加数据源，以懒加载的模式解决分布式场景的配置同步
		// 为了保证数据完整性，配置后生成数据源缓存，后台便无法修改更换数据源，若一定要修改请迁移数据后重启服务或自行修改底层逻辑
		if (!keys.contains(tenantId)) {
			TenantDataSource tenantDataSource = getDataSource(tenantId);
			if (tenantDataSource != null) {
				// 创建数据源配置
				DataSourceProperty dataSourceProperty = new DataSourceProperty();
				// 拷贝数据源配置
				BeanUtils.copyProperties(tenantDataSource, dataSourceProperty);
				// 创建动态数据源
				DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
				// 添加最新数据源
				ds.addDataSource(tenantId, dataSource);
			}
		}
	}

	/**
	 * 判断租户是否有数据源配置
	 *
	 * @param tenantId 租户ID
	 */
	private Boolean existDataSource(String tenantId) {
		// 将租户是否配置数据源进行缓存，若重新配置会将此缓存清空并在下次请求的时候懒加载
		// 若租户没有配置数据源则会自动使用master数据源，此举是为了避免在没有数据库的时候频繁查询导致缓存击穿
		Boolean exist = CacheUtil.get(TENANT_DATASOURCE_CACHE, TENANT_DATASOURCE_EXIST_KEY, tenantId, Boolean.class, Boolean.FALSE);
		if (exist == null) {
			TenantDataSource tenantDataSource = jdbcTemplate.queryForObject(TENANT_DATASOURCE_EXIST_STATEMENT, new String[]{tenantId}, new BeanPropertyRowMapper<>(TenantDataSource.class));
			if (tenantDataSource != null && StringUtil.isNotBlank(tenantDataSource.getDatasourceId())) {
				exist = Boolean.TRUE;
			} else {
				exist = Boolean.FALSE;
			}
			CacheUtil.put(TENANT_DATASOURCE_CACHE, TENANT_DATASOURCE_EXIST_KEY, tenantId, exist, Boolean.FALSE);
		}
		return exist;
	}

	/**
	 * 获取对应的数据源配置
	 *
	 * @param tenantId 租户ID
	 */
	private TenantDataSource getDataSource(String tenantId) {
		// 不存在租户数据源则返回空，防止缓存击穿
		if (!existDataSource(tenantId)) {
			return null;
		}
		// 获取租户数据源信息
		TenantDataSource tenantDataSource = CacheUtil.get(TENANT_DATASOURCE_CACHE, TENANT_DATASOURCE_KEY, tenantId, TenantDataSource.class, Boolean.FALSE);
		if (tenantDataSource == null) {
			tenantDataSource = jdbcTemplate.queryForObject(TENANT_DATASOURCE_SINGLE_STATEMENT, new String[]{tenantId}, new BeanPropertyRowMapper<>(TenantDataSource.class));
			if (tenantDataSource != null && StringUtil.isNoneBlank(tenantDataSource.getTenantId(), tenantDataSource.getDriverClass(), tenantDataSource.getUrl(), tenantDataSource.getUsername(), tenantDataSource.getPassword())) {
				CacheUtil.put(TENANT_DATASOURCE_CACHE, TENANT_DATASOURCE_KEY, tenantId, tenantDataSource, Boolean.FALSE);
			} else {
				tenantDataSource = null;
			}
		}
		return tenantDataSource;
	}

}
