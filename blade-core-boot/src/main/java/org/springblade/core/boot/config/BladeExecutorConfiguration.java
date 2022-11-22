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
package org.springblade.core.boot.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.error.ErrorType;
import org.springblade.core.boot.error.ErrorUtil;
import org.springblade.core.context.BladeContext;
import org.springblade.core.context.BladeRunnableWrapper;
import org.springblade.core.launch.props.BladeProperties;
import org.springblade.core.log.constant.EventConstant;
import org.springblade.core.log.event.ErrorLogEvent;
import org.springblade.core.log.model.LogError;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ErrorHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理
 *
 * @author Chill
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@AllArgsConstructor
public class BladeExecutorConfiguration extends AsyncConfigurerSupport {

	private final BladeContext bladeContext;
	private final BladeProperties bladeProperties;
	private final ApplicationEventPublisher publisher;

	@Bean
	public TaskExecutorCustomizer taskExecutorCustomizer() {
		return taskExecutor -> {
			taskExecutor.setThreadNamePrefix("async-task-");
			taskExecutor.setTaskDecorator(BladeRunnableWrapper::new);
			taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		};
	}

	@Bean
	public TaskSchedulerCustomizer taskSchedulerCustomizer() {
		return taskExecutor -> {
			taskExecutor.setThreadNamePrefix("async-scheduler");
			taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			taskExecutor.setErrorHandler(new BladeErrorHandler(bladeContext, bladeProperties, publisher));
		};
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new BladeAsyncUncaughtExceptionHandler(bladeContext, bladeProperties, publisher);
	}

	@RequiredArgsConstructor
	private static class BladeAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
		private final BladeContext bladeContext;
		private final BladeProperties bladeProperties;
		private final ApplicationEventPublisher eventPublisher;

		@Override
		public void handleUncaughtException(@NonNull Throwable error, @NonNull Method method, @NonNull Object... params) {
			log.error("Unexpected exception occurred invoking async method: {}", method, error);
			LogError logError = new LogError();
			// 服务信息、环境、异常类型
			logError.setParams(ErrorType.ASYNC.getType());
			logError.setEnv(bladeProperties.getEnv());
			logError.setServiceId(bladeProperties.getName());
			logError.setRequestUri(bladeContext.getRequestId());
			// 堆栈信息
			ErrorUtil.initErrorInfo(error, logError);
			Map<String, Object> event = new HashMap<>(16);
			event.put(EventConstant.EVENT_LOG, logError);
			eventPublisher.publishEvent(new ErrorLogEvent(event));
		}
	}

	@RequiredArgsConstructor
	private static class BladeErrorHandler implements ErrorHandler {
		private final BladeContext bladeContext;
		private final BladeProperties bladeProperties;
		private final ApplicationEventPublisher eventPublisher;

		@Override
		public void handleError(@NonNull Throwable error) {
			log.error("Unexpected scheduler exception", error);
			LogError logError = new LogError();
			// 服务信息、环境、异常类型
			logError.setParams(ErrorType.SCHEDULER.getType());
			logError.setServiceId(bladeProperties.getName());
			logError.setEnv(bladeProperties.getEnv());
			logError.setRequestUri(bladeContext.getRequestId());
			// 堆栈信息
			ErrorUtil.initErrorInfo(error, logError);
			Map<String, Object> event = new HashMap<>(16);
			event.put(EventConstant.EVENT_LOG, logError);
			eventPublisher.publishEvent(new ErrorLogEvent(event));
		}
	}

}
