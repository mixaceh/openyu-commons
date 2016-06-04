package org.openyu.commons.bao.redis.supporter;

import org.openyu.commons.bao.redis.RedisBao;
import org.openyu.commons.bao.redis.ex.RedisBaoException;
import org.openyu.commons.bao.supporter.BaseBaoSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

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

	public <T> T execute(RedisCallback<T> action) {
		try {
			return redisTemplate.execute(action);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}

	public <T> T execute(SessionCallback<T> session) {
		try {
			return redisTemplate.execute(session);
		} catch (Exception ex) {
			throw new RedisBaoException(ex);
		}
	}
}
