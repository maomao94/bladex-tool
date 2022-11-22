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
package org.springblade.core.cache.utils;

import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

/**
 * 缓存工具类
 *
 * @author Chill
 */
public class CacheUtil {

	private static CacheManager cacheManager;

	private static final Boolean TENANT_MODE = Boolean.TRUE;

	/**
	 * 获取缓存工具
	 *
	 * @return CacheManager
	 */
	private static CacheManager getCacheManager() {
		if (cacheManager == null) {
			cacheManager = SpringUtil.getBean(CacheManager.class);
		}
		return cacheManager;
	}

	/**
	 * 获取缓存对象
	 *
	 * @param cacheName 缓存名
	 * @return Cache
	 */
	public static Cache getCache(String cacheName) {
		return getCache(cacheName, TENANT_MODE);
	}

	/**
	 * 获取缓存对象
	 *
	 * @param cacheName  缓存名
	 * @param tenantMode 租户模式
	 * @return Cache
	 */
	public static Cache getCache(String cacheName, Boolean tenantMode) {
		return getCacheManager().getCache(formatCacheName(cacheName, tenantMode));
	}

	/**
	 * 根据租户信息格式化缓存名
	 *
	 * @param cacheName  缓存名
	 * @param tenantMode 租户模式
	 * @return String
	 */
	public static String formatCacheName(String cacheName, Boolean tenantMode) {
		if (!tenantMode) {
			return cacheName;
		}
		String tenantId = AuthUtil.getTenantId();
		return (StringUtil.isBlank(tenantId) ? cacheName : tenantId.concat(StringPool.COLON).concat(cacheName));
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @return Object
	 */
	@Nullable
	public static Object get(String cacheName, String keyPrefix, Object key) {
		return get(cacheName, keyPrefix, key, TENANT_MODE);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName  缓存名
	 * @param keyPrefix  缓存键前缀
	 * @param key        缓存键值
	 * @param tenantMode 租户模式
	 * @return Object
	 */
	@Nullable
	public static Object get(String cacheName, String keyPrefix, Object key, Boolean tenantMode) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		Cache.ValueWrapper valueWrapper = getCache(cacheName, tenantMode).get(keyPrefix.concat(String.valueOf(key)));
		if (Func.isEmpty(valueWrapper)) {
			return null;
		}
		return valueWrapper.get();
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @param type      类型
	 * @param <T>       泛型
	 * @return T
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, @Nullable Class<T> type) {
		return get(cacheName, keyPrefix, key, type, TENANT_MODE);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName  缓存名
	 * @param keyPrefix  缓存键前缀
	 * @param key        缓存键值
	 * @param type       类型
	 * @param tenantMode 租户模式
	 * @param <T>        泛型
	 * @return T
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, @Nullable Class<T> type, Boolean tenantMode) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		return getCache(cacheName, tenantMode).get(keyPrefix.concat(String.valueOf(key)), type);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName   缓存名
	 * @param keyPrefix   缓存键前缀
	 * @param key         缓存键值
	 * @param valueLoader 重载对象
	 * @param <T>         泛型
	 * @return T
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
		return get(cacheName, keyPrefix, key, valueLoader, TENANT_MODE);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName   缓存名
	 * @param keyPrefix   缓存键前缀
	 * @param key         缓存键值
	 * @param valueLoader 重载对象
	 * @param tenantMode  租户模式
	 * @param <T>         泛型
	 * @return T
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader, Boolean tenantMode) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		try {
			Cache.ValueWrapper valueWrapper = getCache(cacheName, tenantMode).get(keyPrefix.concat(String.valueOf(key)));
			Object value = null;
			if (valueWrapper == null) {
				T call = valueLoader.call();
				if (ObjectUtil.isNotEmpty(call)) {
					Field field = ReflectUtil.getField(call.getClass(), BladeConstant.DB_PRIMARY_KEY);
					if (ObjectUtil.isNotEmpty(field) && ObjectUtil.isEmpty(ClassUtil.getMethod(call.getClass(), BladeConstant.DB_PRIMARY_KEY_METHOD).invoke(call))) {
						return null;
					}
					getCache(cacheName, tenantMode).put(keyPrefix.concat(String.valueOf(key)), call);
					value = call;
				}
			} else {
				value = valueWrapper.get();
			}
			return (T) value;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @param value     缓存值
	 */
	public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
		put(cacheName, keyPrefix, key, value, TENANT_MODE);
	}

	/**
	 * 设置缓存
	 *
	 * @param cacheName  缓存名
	 * @param keyPrefix  缓存键前缀
	 * @param key        缓存键值
	 * @param value      缓存值
	 * @param tenantMode 租户模式
	 */
	public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value, Boolean tenantMode) {
		getCache(cacheName, tenantMode).put(keyPrefix.concat(String.valueOf(key)), value);
	}

	/**
	 * 清除缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 */
	public static void evict(String cacheName, String keyPrefix, Object key) {
		evict(cacheName, keyPrefix, key, TENANT_MODE);
	}

	/**
	 * 清除缓存
	 *
	 * @param cacheName  缓存名
	 * @param keyPrefix  缓存键前缀
	 * @param key        缓存键值
	 * @param tenantMode 租户模式
	 */
	public static void evict(String cacheName, String keyPrefix, Object key, Boolean tenantMode) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return;
		}
		getCache(cacheName, tenantMode).evict(keyPrefix.concat(String.valueOf(key)));
	}

	/**
	 * 清空缓存
	 *
	 * @param cacheName 缓存名
	 */
	public static void clear(String cacheName) {
		clear(cacheName, TENANT_MODE);
	}

	/**
	 * 清空缓存
	 *
	 * @param cacheName  缓存名
	 * @param tenantMode 租户模式
	 */
	public static void clear(String cacheName, Boolean tenantMode) {
		if (Func.isEmpty(cacheName)) {
			return;
		}
		getCache(cacheName, tenantMode).clear();
	}

}
