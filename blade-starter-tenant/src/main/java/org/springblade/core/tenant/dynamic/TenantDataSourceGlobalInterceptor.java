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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tenant.exception.TenantDataSourceException;

/**
 * 租户数据源全局拦截器
 *
 * @author Chill
 */
public class TenantDataSourceGlobalInterceptor implements MethodInterceptor {

	@Setter
	private TenantDataSourceHolder holder;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			String tenantId = AuthUtil.getTenantId();
			holder.handleDataSource(tenantId);
			DynamicDataSourceContextHolder.push(tenantId);
			return invocation.proceed();
		} catch (Exception exception) {
			throw new TenantDataSourceException(exception.getMessage());
		} finally {
			DynamicDataSourceContextHolder.poll();
		}
	}
}
