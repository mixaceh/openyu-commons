package org.openyu.commons.redis.impl;

import org.openyu.commons.redis.supporter.JedisPoolConfigFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolConfigFactoryBean extends JedisPoolConfigFactorySupporter<JedisPoolConfig> {

	private static final long serialVersionUID = 8173598562765422722L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(JedisPoolConfigFactoryBean.class);

	private JedisPoolConfig jedisPoolConfig;

	public JedisPoolConfigFactoryBean() {
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected JedisPoolConfig shutdownJedisPoolConfig() throws Exception {
		try {
			if (this.jedisPoolConfig != null) {
				JedisPoolConfig oldInstance = this.jedisPoolConfig;
				// oldInstance.close();
				//
				this.jedisPoolConfig = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownJedisPoolConfig()").toString(), e);
			throw e;
		}
		return this.jedisPoolConfig;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected JedisPoolConfig restartJedisPoolConfig() throws Exception {
		try {
			if (this.jedisPoolConfig != null) {
				this.jedisPoolConfig = shutdownJedisPoolConfig();
				this.jedisPoolConfig = createJedisPoolConfig(0);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartJedisPoolConfig()").toString(), e);
			throw e;
		}
		return this.jedisPoolConfig;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.jedisPoolConfig = createJedisPoolConfig(0);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.jedisPoolConfig = shutdownJedisPoolConfig();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.jedisPoolConfig = restartJedisPoolConfig();
	}

	@Override
	public JedisPoolConfig getObject() throws Exception {
		return jedisPoolConfig;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.jedisPoolConfig != null) ? this.jedisPoolConfig.getClass() : JedisPoolConfig.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
