package org.openyu.commons.redis.supporter;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;

public abstract class JedisPoolConfigFactorySupporter<T> extends BaseFactoryBeanSupporter<T> {

	private static final long serialVersionUID = -3115943094433243928L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(JedisPoolConfigFactorySupporter.class);

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

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { MAX_TOTAL, MIN_IDLE, MAX_WAIT_MILLIS, TEST_ON_BORROW,
			TEST_ON_RETURN };

	public JedisPoolConfigFactorySupporter() {
	}

	/**
	 * 建立
	 * 
	 * @return
	 * @throws Exception
	 */
	protected JedisPoolConfig createJedisPoolConfig(int i) throws Exception {
		JedisPoolConfig result = null;
		//
		try {
			result = new JedisPoolConfig();

			/**
			 * extendedProperties
			 */
			result.setMaxTotal(extendedProperties.getInt(MAX_TOTAL, DEFAULT_MAX_TOTAL));
			result.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
			result.setMaxWaitMillis(extendedProperties.getLong(MAX_WAIT_MILLIS, DEFAULT_MAX_WAIT_MILLIS));
			result.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
			result.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createJedisPoolConfig()").toString(), e);
			try {
				result = (JedisPoolConfig) shutdownJedisPoolConfig();
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
	protected abstract JedisPoolConfig shutdownJedisPoolConfig() throws Exception;
}
