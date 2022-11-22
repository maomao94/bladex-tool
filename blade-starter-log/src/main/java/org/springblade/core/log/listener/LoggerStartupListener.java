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
package org.springblade.core.log.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import org.springblade.core.log.utils.ElkPropsUtil;
import org.springblade.core.tool.utils.StringUtil;

/**
 * logback监听类
 *
 * @author Chill
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

	@Override
	public void start() {
		Context context = getContext();
		context.putProperty("ELK_MODE", "FALSE");
		context.putProperty("STDOUT_APPENDER", "STDOUT");
		context.putProperty("INFO_APPENDER", "INFO");
		context.putProperty("ERROR_APPENDER", "ERROR");
		context.putProperty("DESTINATION", "127.0.0.1:9000");
		String destination = ElkPropsUtil.getDestination();
		if (StringUtil.isNotBlank(destination)) {
			context.putProperty("ELK_MODE", "TRUE");
			context.putProperty("STDOUT_APPENDER", "STDOUT_LOGSTASH");
			context.putProperty("INFO_APPENDER", "INFO_LOGSTASH");
			context.putProperty("ERROR_APPENDER", "ERROR_LOGSTASH");
			context.putProperty("DESTINATION", destination);
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public boolean isResetResistant() {
		return false;
	}

	@Override
	public void onStart(LoggerContext context) {

	}

	@Override
	public void onReset(LoggerContext context) {

	}

	@Override
	public void onStop(LoggerContext context) {

	}

	@Override
	public void onLevelChange(Logger logger, Level level) {

	}
}
