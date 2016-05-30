package org.openyu.commons.redis.supporter;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

public abstract class JedisConnectionFactoryFactorySupporter<T> extends BaseFactoryBeanSupporter<T> {

	private static final long serialVersionUID = 2761520293081860518L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(JedisConnectionFactoryFactorySupporter.class);

	public static final String HOSTNAME = "hostName";

	public static final String DEFAULT_HOSTNAME = "127.0.0.1";

	public static final String PORT = "port";

	public static final int DEFAULT_PORT = 6379;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { HOSTNAME, PORT, PASSWORD };

	public JedisConnectionFactoryFactorySupporter() {
	}

	/**
	 * 建立
	 * 
	 * i=0, port(6379)
	 * 
	 * i=1, port(6379)+i=6380
	 * 
	 * @param i
	 * @param jedisPoolConfig
	 * @return
	 */
	protected JedisConnectionFactory createJedisConnectionFactory(int i, JedisPoolConfig jedisPoolConfig)
			throws Exception {
		JedisConnectionFactory result = new JedisConnectionFactory();
		String hostName = extendedProperties.getString(HOSTNAME, DEFAULT_HOSTNAME);
		//
		int port = extendedProperties.getInt(PORT, DEFAULT_PORT);
		port += i;
		LOGGER.info("redis[" + i + "]: " + hostName + ":" + port);
		//
		result.setHostName(hostName);
		result.setPort(port);
		//
		result.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
		//
		result.setPoolConfig(jedisPoolConfig);
		result.afterPropertiesSet();
		//
		return result;
	}

	protected abstract JedisConnectionFactory shutdownJedisConnectionFactory() throws Exception;
}
