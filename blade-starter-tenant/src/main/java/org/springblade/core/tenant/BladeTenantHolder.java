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

import org.springframework.core.NamedThreadLocal;

/**
 * 租户线程处理
 *
 * @author Chill
 */
public class BladeTenantHolder {

	private static final ThreadLocal<Boolean> TENANT_KEY_HOLDER = new NamedThreadLocal<Boolean>("blade-tenant") {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public static void setIgnore(Boolean ignore) {
		TENANT_KEY_HOLDER.set(ignore);
	}

	public static Boolean isIgnore() {
		return TENANT_KEY_HOLDER.get();
	}


	public static void clear() {
		TENANT_KEY_HOLDER.remove();
	}


}
