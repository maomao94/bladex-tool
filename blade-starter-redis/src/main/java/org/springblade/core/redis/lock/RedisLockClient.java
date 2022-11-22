/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
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
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package org.springblade.core.redis.lock;

import org.springblade.core.tool.function.CheckedSupplier;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author L.cm
 */
public interface RedisLockClient {

	/**
	 * 尝试获取锁
	 *
	 * @param lockName  锁名
	 * @param lockType  锁类型
	 * @param waitTime  等待时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间
	 * @param timeUnit  时间参数
	 * @return 是否成功
	 * @throws InterruptedException InterruptedException
	 */
	boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

	/**
	 * 解锁
	 *
	 * @param lockName 锁名
	 * @param lockType 锁类型
	 */
	void unLock(String lockName, LockType lockType);

	/**
	 * 自定获取锁后执行方法
	 *
	 * @param lockName  锁名
	 * @param lockType  锁类型
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param timeUnit  时间单位
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	<T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier);

	/**
	 * 公平锁
	 *
	 * @param lockName  锁名
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	default <T> T lockFair(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
		return lock(lockName, LockType.FAIR, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
	}

	/**
	 * 可重入锁
	 *
	 * @param lockName  锁名
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	default <T> T lockReentrant(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
		return lock(lockName, LockType.REENTRANT, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
	}

}
