package org.openyu.commons.bao.redis;

import org.openyu.commons.bao.BaseBao;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SessionCallback;

public interface RedisBao<K, V> extends BaseBao {

	<T> T execute(RedisCallback<T> action);

	<T> T execute(SessionCallback<T> session);
}
