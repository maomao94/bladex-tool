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

package org.springblade.core.redis.config;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 序列化
 *
 * @author L.cm
 */
public interface BladeRedisSerializerConfigAble {

	/**
	 * 序列化接口
	 *
	 * @param properties 配置
	 * @return RedisSerializer
	 */
	RedisSerializer<Object> redisSerializer(BladeRedisProperties properties);

	/**
	 * 默认的序列化方式
	 *
	 * @param properties 配置
	 * @return RedisSerializer
	 */
	default RedisSerializer<Object> defaultRedisSerializer(BladeRedisProperties properties) {
		BladeRedisProperties.SerializerType serializerType = properties.getSerializerType();
		if (BladeRedisProperties.SerializerType.JDK == serializerType) {
			/**
			 * SpringBoot扩展了ClassLoader，进行分离打包的时候，使用到JdkSerializationRedisSerializer的地方
			 * 会因为ClassLoader的不同导致加载不到Class
			 * 指定使用项目的ClassLoader
			 *
			 * JdkSerializationRedisSerializer默认使用{@link sun.misc.Launcher.AppClassLoader}
			 * SpringBoot默认使用{@link org.springframework.boot.loader.LaunchedURLClassLoader}
			 */
			ClassLoader classLoader = this.getClass().getClassLoader();
			return new JdkSerializationRedisSerializer(classLoader);
		}
		return new GenericJackson2JsonRedisSerializer();
	}
}
