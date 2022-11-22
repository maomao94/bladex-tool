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

package org.springblade.core.tenant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springblade.core.tenant.BladeTenantHolder;
import org.springblade.core.tenant.annotation.TenantIgnore;

/**
 * 自定义租户切面
 *
 * @author Chill
 */
@Slf4j
@Aspect
public class BladeTenantAspect {

	@Around("@annotation(tenantIgnore)")
	public Object around(ProceedingJoinPoint point, TenantIgnore tenantIgnore) throws Throwable {
		//开启忽略
		BladeTenantHolder.setIgnore(Boolean.TRUE);
		//执行方法
		Object result = point.proceed();
		//关闭忽略
		BladeTenantHolder.clear();
		return result;
	}

}
