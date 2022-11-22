package org.springblade.core.social.cache;

import lombok.AllArgsConstructor;
import me.zhyd.oauth.cache.AuthCacheConfig;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 扩展Redis版的state缓存
 *
 * @author yadong.zhang, Chill
 */
@AllArgsConstructor
public class AuthStateRedisCache implements AuthStateCache {

	private final RedisTemplate<String, Object> redisTemplate;

	private final ValueOperations<String, Object> valueOperations;


	/**
	 * 存入缓存，默认3分钟
	 *
	 * @param key   缓存key
	 * @param value 缓存内容
	 */
	@Override
	public void cache(String key, String value) {
		valueOperations.set(key, value, AuthCacheConfig.timeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * 存入缓存
	 *
	 * @param key     缓存key
	 * @param value   缓存内容
	 * @param timeout 指定缓存过期时间（毫秒）
	 */
	@Override
	public void cache(String key, String value, long timeout) {
		valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * 获取缓存内容
	 *
	 * @param key 缓存key
	 * @return 缓存内容
	 */
	@Override
	public String get(String key) {
		return String.valueOf(valueOperations.get(key));
	}

	/**
	 * 是否存在key，如果对应key的value值已过期，也返回false
	 *
	 * @param key 缓存key
	 * @return true：存在key，并且value没过期；false：key不存在或者已过期
	 */
	@Override
	public boolean containsKey(String key) {
		return redisTemplate.hasKey(key);
	}

}
