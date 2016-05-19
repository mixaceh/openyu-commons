package org.openyu.commons.redis.impl;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolConfigGroupFactoryBean extends BaseFactoryBeanSupporter<JedisPoolConfig[]> {

	private static final long serialVersionUID = 8998082529227210243L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(JedisPoolConfigGroupFactoryBean.class);

	public static final String MAX_CONNECTION_FACTORY_SIZE = "maxConnectionFactorySize";

	public static final int DEFAULT_MAX_CONNECTION_FACTORY_SIZE = 1;

	public static final String MAX_TOTAL = "maxTotal";

	public static final int DEFAULT_MAX_TOTAL = 8;

	public static final String MIN_IDLE = "minIdle";

	public static final int DEFAULT_MIN_IDLE = 1;

	public static final String MAX_WAIT_MILLIS = "maxWaitMillis";

	public static final long DEFAULT_MAX_WAIT_MILLIS = 10000;

	public static final String TEST_ON_BORROW = "testOnBorrow";

	public static final boolean DEFAULT_TEST_ON_BORROW = false;

	public static final String TEST_ON_RETURN = "testOnReturn";

	public static final boolean DEFAULT_TEST_ON_RETURN = false;

	private JedisPoolConfig[] jedisPoolConfigs;

	public JedisPoolConfigGroupFactoryBean() {
	}

	public JedisPoolConfig[] getJedisPoolConfigs() {
		return jedisPoolConfigs;
	}

	public void setJedisPoolConfigs(JedisPoolConfig[] jedisPoolConfigs) {
		this.jedisPoolConfigs = jedisPoolConfigs;
	}

	/**
	 * 建立多個JedisPoolConfig
	 * 
	 * @return
	 * @throws Exception
	 */
	protected JedisPoolConfig[] createJedisPoolConfigs() throws Exception {
		JedisPoolConfig[] result = null;
		//
		try {
			int maxConnectionFactorySize = extendedProperties.getInt(MAX_CONNECTION_FACTORY_SIZE,
					DEFAULT_MAX_CONNECTION_FACTORY_SIZE);
			result = new JedisPoolConfig[maxConnectionFactorySize];
			//
			for (int i = 0; i < maxConnectionFactorySize; i++) {
				JedisPoolConfig poolConfig = new JedisPoolConfig();
				//
				poolConfig.setMaxTotal(extendedProperties.getInt(MAX_TOTAL, DEFAULT_MAX_TOTAL));
				poolConfig.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
				poolConfig.setMaxWaitMillis(extendedProperties.getLong(MAX_WAIT_MILLIS, DEFAULT_MAX_WAIT_MILLIS));
				poolConfig.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
				poolConfig.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
				//
				result[i] = poolConfig;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createJedisPoolConfigs()").toString(), e);
			try {
				result = (JedisPoolConfig[]) shutdownJedisPoolConfigs();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected JedisPoolConfig[] shutdownJedisPoolConfigs() throws Exception {
		try {
			if (this.jedisPoolConfigs != null) {
				for (int i = 0; i < this.jedisPoolConfigs.length; i++) {
					JedisPoolConfig oldInstance = this.jedisPoolConfigs[i];
					// oldInstance.close();
					this.jedisPoolConfigs[i] = null;
				}
				//
				this.jedisPoolConfigs = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownJedisPoolConfigs()").toString(), e);
			throw e;
		}
		return this.jedisPoolConfigs;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected JedisPoolConfig[] restartJedisPoolConfigs() throws Exception {
		try {
			if (this.jedisPoolConfigs != null) {
				this.jedisPoolConfigs = shutdownJedisPoolConfigs();
				this.jedisPoolConfigs = createJedisPoolConfigs();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartJedisPoolConfigs()").toString(), e);
			throw e;
		}
		return this.jedisPoolConfigs;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		if (this.jedisPoolConfigs != null) {
			LOGGER.info(new StringBuilder().append("Inject from setJedisPoolConfigs()").toString());
		} else {
			LOGGER.info(new StringBuilder().append("Using createJedisPoolConfigs()").toString());
			this.jedisPoolConfigs = createJedisPoolConfigs();
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.jedisPoolConfigs = shutdownJedisPoolConfigs();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.jedisPoolConfigs = restartJedisPoolConfigs();
	}

	@Override
	public JedisPoolConfig[] getObject() throws Exception {
		return jedisPoolConfigs;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.jedisPoolConfigs != null) ? this.jedisPoolConfigs.getClass() : JedisPoolConfig[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
