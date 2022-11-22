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

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户信息处理器
 *
 * @author Chill, L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class BladeTenantHandler implements TenantLineHandler, SmartInitializingSingleton {
	/**
	 * 匹配的多租户表
	 */
	private final List<String> tenantTableList = new ArrayList<>();
	/**
	 * 需要排除进行自定义的多租户表
	 */
	private final List<String> excludeTableList = Arrays.asList("blade_user", "blade_dept", "blade_role", "blade_tenant", "act_de_model");
	/**
	 * 多租户配置
	 */
	private final BladeTenantProperties tenantProperties;

	/**
	 * 获取租户ID
	 *
	 * @return 租户ID
	 */
	@Override
	public Expression getTenantId() {
		return new StringValue(Func.toStr(AuthUtil.getTenantId(), BladeConstant.ADMIN_TENANT_ID));
	}

	/**
	 * 获取租户字段名称
	 *
	 * @return 租户字段名称
	 */
	@Override
	public String getTenantIdColumn() {
		return tenantProperties.getColumn();
	}

	/**
	 * 根据表名判断是否忽略拼接多租户条件
	 * 默认都要进行解析并拼接多租户条件
	 *
	 * @param tableName 表名
	 * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
	 */
	@Override
	public boolean ignoreTable(String tableName) {
		if (BladeTenantHolder.isIgnore()) {
			return true;
		}
		return !(tenantTableList.contains(tableName) && StringUtil.isNotBlank(AuthUtil.getTenantId()));
	}

	@Override
	public void afterSingletonsInstantiated() {
		List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
		tableFor:
		for (TableInfo tableInfo : tableInfos) {
			String tableName = tableInfo.getTableName();
			if (tenantProperties.getExcludeTables().contains(tableName) ||
				excludeTableList.contains(tableName.toLowerCase()) ||
				excludeTableList.contains(tableName.toUpperCase())) {
				continue;
			}
			List<TableFieldInfo> fieldList = tableInfo.getFieldList();
			for (TableFieldInfo fieldInfo : fieldList) {
				String column = fieldInfo.getColumn();
				if (tenantProperties.getColumn().equals(column)) {
					tenantTableList.add(tableName);
					continue tableFor;
				}
			}
		}
	}
}
