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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springblade.core.tool.function.CheckedSupplier;
import org.springblade.core.tool.utils.Exceptions;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLockClientImpl implements RedisLockClient {
	private final RedissonClient redissonClient;

	@Override
	public boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
		RLock lock = getLock(lockName, lockType);
		return lock.tryLock(waitTime, leaseTime, timeUnit);
	}

	@Override
	public void unLock(String lockName, LockType lockType) {
		RLock lock = getLock(lockName, lockType);
		// 仅仅在已经锁定和当前线程持有锁时解锁
		if (lock.isLocked() && lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	private RLock getLock(String lockName, LockType lockType) {
		RLock lock;
		if (LockType.REENTRANT == lockType) {
			lock = redissonClient.getLock(lockName);
		} else {
			lock = redissonClient.getFairLock(lockName);
		}
		return lock;
	}

	@Override
	public <T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier) {
		try {
			boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
			if (result) {
				return supplier.get();
			}
		} catch (Throwable e) {
			throw Exceptions.unchecked(e);
		} finally {
			this.unLock(lockName, lockType);
		}
		return null;
	}

}
