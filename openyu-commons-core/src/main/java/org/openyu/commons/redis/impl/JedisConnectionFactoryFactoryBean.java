package org.openyu.commons.redis.impl;

import org.openyu.commons.redis.supporter.JedisConnectionFactoryFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionFactoryFactoryBean extends JedisConnectionFactoryFactorySupporter<JedisConnectionFactory> {

	private static final long serialVersionUID = -8934376750004399395L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(JedisConnectionFactoryFactoryBean.class);

	private JedisConnectionFactory jedisConnectionFactory;

	private JedisPoolConfig jedisPoolConfig;

	public JedisConnectionFactoryFactoryBean(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public JedisConnectionFactoryFactoryBean() {
		this(new JedisPoolConfig());
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected JedisConnectionFactory shutdownJedisConnectionFactory() throws Exception {
		try {
			if (this.jedisConnectionFactory != null) {
				JedisConnectionFactory oldInstance = this.jedisConnectionFactory;
				// oldInstance.close();
				//
				this.jedisConnectionFactory = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownJedisConnectionFactory()").toString(),
					e);
			throw e;
		}
		return this.jedisConnectionFactory;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected JedisConnectionFactory restartJedisConnectionFactory() throws Exception {
		try {
			if (this.jedisConnectionFactory != null) {
				this.jedisConnectionFactory = shutdownJedisConnectionFactory();
				this.jedisConnectionFactory = createJedisConnectionFactory(0, this.jedisPoolConfig);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartJedisConnectionFactory()").toString(),
					e);
			throw e;
		}
		return this.jedisConnectionFactory;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.jedisConnectionFactory = createJedisConnectionFactory(0, this.jedisPoolConfig);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.jedisConnectionFactory = shutdownJedisConnectionFactory();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.jedisConnectionFactory = restartJedisConnectionFactory();
	}

	@Override
	public JedisConnectionFactory getObject() throws Exception {
		return jedisConnectionFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.jedisConnectionFactory != null) ? this.jedisConnectionFactory.getClass()
				: JedisConnectionFactory.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
