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
package org.springblade.core.boot.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 异常类型
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum ErrorType {
	/**
	 * 异常类型
	 */
	REQUEST("request"),
	ASYNC("async"),
	SCHEDULER("scheduler"),
	WEB_SOCKET("websocket"),
	OTHER("other");

	@JsonValue
	private final String type;

	@Nullable
	@JsonCreator
	public static ErrorType of(String type) {
		ErrorType[] values = ErrorType.values();
		for (ErrorType errorType : values) {
			if (errorType.type.equals(type)) {
				return errorType;
			}
		}
		return null;
	}

}
