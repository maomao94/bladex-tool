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

import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tenant.exception.TenantDataSourceException;

/**
 * 租户数据源切换拦截器
 *
 * @author Chill
 */
public class TenantDataSourceAnnotationInterceptor extends DynamicDataSourceAnnotationInterceptor {

	@Setter
	private TenantDataSourceHolder holder;

	public TenantDataSourceAnnotationInterceptor(Boolean allowedPublicOnly, DsProcessor dsProcessor) {
		super(allowedPublicOnly, dsProcessor);
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			holder.handleDataSource(AuthUtil.getTenantId());
			return super.invoke(invocation);
		} catch (Exception exception) {
			throw new TenantDataSourceException(exception.getMessage());
		}
	}

}
