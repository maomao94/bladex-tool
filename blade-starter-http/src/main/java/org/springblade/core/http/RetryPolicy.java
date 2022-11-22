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
package org.springblade.core.http;

import lombok.Getter;
import lombok.ToString;
import org.springframework.retry.policy.SimpleRetryPolicy;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * 重试策略
 *
 * @author dream.lu
 */
@Getter
@ToString
public class RetryPolicy {
	public static final RetryPolicy INSTANCE = new RetryPolicy();

	private final int maxAttempts;
	private final long sleepMillis;
	@Nullable
	private final Predicate<ResponseSpec> respPredicate;

	public RetryPolicy() {
		this(null);
	}

	public RetryPolicy(int maxAttempts, long sleepMillis) {
		this(maxAttempts, sleepMillis, null);
	}

	public RetryPolicy(@Nullable Predicate<ResponseSpec> respPredicate) {
		this(SimpleRetryPolicy.DEFAULT_MAX_ATTEMPTS, 0L, respPredicate);
	}

	public RetryPolicy(int maxAttempts, long sleepMillis, @Nullable Predicate<ResponseSpec> respPredicate) {
		this.maxAttempts = maxAttempts;
		this.sleepMillis = sleepMillis;
		this.respPredicate = respPredicate;
	}
}
