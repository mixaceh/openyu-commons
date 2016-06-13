package org.openyu.commons.bao.redis;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openyu.commons.bao.BaseBao;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * Redis Big Data Access Object
 * 
 * @param <K>
 * @param <V>
 */
public interface RedisBao<K, V> extends BaseBao {

	<T> T execute(RedisCallback<T> action);

	<T> T execute(SessionCallback<T> session);

	Boolean hasKey(K key);

	void delete(K key);

	void delete(Collection<K> key);

	void rename(K oldKey, K newKey);

	Boolean expire(K key, long timeout, TimeUnit unit);

	Boolean expireAt(K key, Date date);

	Boolean move(K key, int dbIndex);

	//
	ValueOperations<K, V> opsForValue();

	BoundValueOperations<K, V> boundValueOps(K key);

	ListOperations<K, V> opsForList();

	BoundListOperations<K, V> boundListOps(K key);

	SetOperations<K, V> opsForSet();

	BoundSetOperations<K, V> boundSetOps(K key);

	ZSetOperations<K, V> opsForZSet();

	HyperLogLogOperations<K, V> opsForHyperLogLog();

	BoundZSetOperations<K, V> boundZSetOps(K key);

	<HK, HV> HashOperations<K, HK, HV> opsForHash();

	<HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key);
}
