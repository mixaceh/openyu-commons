package org.openyu.commons.redis;

import org.springframework.data.redis.serializer.RedisSerializer;

public interface BaseRedisSerializer<T> extends RedisSerializer<T> {

}
