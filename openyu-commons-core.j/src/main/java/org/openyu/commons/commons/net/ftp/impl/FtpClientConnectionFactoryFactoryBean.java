package org.openyu.commons.commons.net.ftp.impl;

import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.service.supporter.BaseServiceFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FtpClientConnectionFactory工廠
 */
public final class FtpClientConnectionFactoryFactoryBean<T extends FtpClientConnectionFactory>
		extends BaseServiceFactoryBeanSupporter<FtpClientConnectionFactory> {

	private static final long serialVersionUID = -1011527154648467009L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(FtpClientConnectionFactoryFactoryBean.class);

	public static final String IP = "ip";

	public static final String DEFAULT_IP = null;

	public static final String DRIVER_CLASSNAME = "driverClassName";

	public static final String DEFAULT_DRIVER_CLASSNAME = null;

	public static final String USERNAME = "username";

	public static final String DEFAULT_USERNAME = null;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;
	//
	public static final String MAX_ACTIVE = "maxActive";

	public static final int DEFAULT_MAX_ACTIVE = 8;

	public static final String INITIAL_SIZE = "initialSize";

	public static final int DEFAULT_INITIAL_SIZE = 0;

	public static final String MAX_WAIT = "maxWait";

	public static final long DEFAULT_MAX_WAIT = -1L;

	public static final String MIN_IDLE = "minIdle";

	public static final int DEFAULT_MIN_IDLE = 0;

	public static final String MAX_IDLE = "maxIdle";

	public static final int DEFAULT_MAX_IDLE = 8;
	//
	public static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";

	public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;

	public static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";

	public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;

	public static final String VALIDATION_QUERY = "validationQuery";

	public static final String DEFAULT_VALIDATION_QUERY = null;

	public static final String TEST_WHILE_IDLE = "testWhileIdle";

	public static final boolean DEFAULT_TEST_WHILE_IDLE = false;

	public static final String TEST_ON_BORROW = "testOnBorrow";

	public static final boolean DEFAULT_TEST_ON_BORROW = true;

	public static final String TEST_ON_RETURN = "testOnReturn";

	public static final boolean DEFAULT_TEST_ON_RETURN = false;
	//
	public static final String POOL_PREPARED_STATEMENTS = "poolPreparedStatements";

	public static final boolean DEFAULT_POOL_PREPARED_STATEMENTS = false;

	public static final String REMOVE_ABANDONED = "removeAbandoned";

	public static final boolean DEFAULT_REMOVE_ABANDONED = false;

	public static final String REMOVE_EABANDONED_TIMEOUT = "removeAbandonedTimeout";

	public static final int DEFAULT_REMOVE_EABANDONED_TIMEOUT = 300;

	public static final String LOG_ABANDONED = "logAbandoned";

	public static final boolean DEFAULT_LOG_ABANDONED = false;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { IP, DRIVER_CLASSNAME, USERNAME, PASSWORD, MAX_ACTIVE, INITIAL_SIZE,
			MAX_WAIT, MIN_IDLE, MAX_IDLE, TIME_BETWEEN_EVICTION_RUNS_MILLIS, MIN_EVICTABLE_IDLE_TIME_MILLIS,
			VALIDATION_QUERY, TEST_WHILE_IDLE, TEST_ON_BORROW, TEST_ON_RETURN, POOL_PREPARED_STATEMENTS,
			REMOVE_ABANDONED, REMOVE_EABANDONED_TIMEOUT, LOG_ABANDONED };

	public FtpClientConnectionFactoryFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected FtpClientConnectionFactory createService() throws Exception {
		FtpClientConnectionFactoryImpl result = null;
		try {
			result = new FtpClientConnectionFactoryImpl();
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);
			/**
			 * extendedProperties
			 */
			// TODO

			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
			try {
				result = (FtpClientConnectionFactoryImpl) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
