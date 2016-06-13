package org.openyu.commons.bao.redis.supporter;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openyu.commons.bao.redis.RedisBao;
import org.openyu.commons.bao.redis.ex.RedisBaoException;
import org.openyu.commons.bao.supporter.BaseBaoSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

public class RedisBaoSupporter<K, V> extends BaseBaoSupporter implements RedisBao<K, V> {

	private static final long serialVersionUID = 8909428030402790500L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(RedisBaoSupporter.class);

	private RedisTemplate<K, V> redisTemplate;

	public RedisBaoSupporter() {

	}

	public RedisTemplate<K, V> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	protected final void checkConfig() throws Exception {
		AssertHelper.notNull(this.redisTemplate, "The RedisTemplate is required");
	}

	// --------------------------------------------------
	@Override
	public <T> T execute(RedisCallback<T> action) {
		try {
			return redisTemplate.execute(action);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public <T> T execute(SessionCallback<T> session) {
		try {
			return redisTemplate.execute(session);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public Boolean hasKey(K key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public void delete(K key) {
		try {
			redisTemplate.delete(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public void delete(Collection<K> key) {
		try {
			redisTemplate.delete(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public void rename(K oldKey, K newKey) {
		try {
			redisTemplate.rename(oldKey, newKey);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public Boolean expire(K key, long timeout, TimeUnit unit) {
		try {
			return redisTemplate.expire(key, timeout, unit);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public Boolean expireAt(K key, Date date) {
		try {
			return redisTemplate.expireAt(key, date);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public Boolean move(K key, int dbIndex) {
		try {
			return redisTemplate.move(key, dbIndex);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public ValueOperations<K, V> opsForValue() {
		try {
			return redisTemplate.opsForValue();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public BoundValueOperations<K, V> boundValueOps(K key) {
		try {
			return redisTemplate.boundValueOps(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public ListOperations<K, V> opsForList() {
		try {
			return redisTemplate.opsForList();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public BoundListOperations<K, V> boundListOps(K key) {
		try {
			return redisTemplate.boundListOps(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public SetOperations<K, V> opsForSet() {
		try {
			return redisTemplate.opsForSet();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public BoundSetOperations<K, V> boundSetOps(K key) {
		try {
			return redisTemplate.boundSetOps(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public ZSetOperations<K, V> opsForZSet() {
		try {
			return redisTemplate.opsForZSet();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public HyperLogLogOperations<K, V> opsForHyperLogLog() {
		try {
			return redisTemplate.opsForHyperLogLog();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public BoundZSetOperations<K, V> boundZSetOps(K key) {
		try {
			return redisTemplate.boundZSetOps(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
		try {
			return redisTemplate.opsForHash();
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	@Override
	public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
		try {
			return redisTemplate.boundHashOps(key);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

}
