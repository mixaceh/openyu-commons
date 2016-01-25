package org.openyu.commons.commons.net.ftp.impl;

import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.nio.NioHelper;
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

	public static final String PORT = "port";

	public static final int DEFAULT_PORT = 21;

	public static final String CONNECT_TIMEOUT = "connectTimeout";

	public static final int DEFAULT_CONNECT_TIMEOUT = 0;

	public static final String RETRY_NUMBER = "retryNumber";

	public static final int DEFAULT_RETRY_NUMBER = NioHelper.DEFAULT_RETRY_NUMBER;

	public static final String RETRY_PAUSE_MILLS = "retryPauseMills";

	public static final long DEFAULT_RETRY_PAUSE_MILLS = NioHelper.DEFAULT_RETRY_PAUSE_MILLS;

	public static final String USERNAME = "username";

	public static final String DEFAULT_USERNAME = null;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;

	public static final String BUFFER_SIZE = "bufferSize";

	public static final int DEFAULT_BUFFER_SIZE = 1024;

	public static final String CLIENT_MODE = "clientMode";

	public static final int DEFAULT_CLIENT_MODE = 2;

	public static final String FILE_TYPE = "fileType";

	public static final int DEFAULT_FILE_TYPE = 0;

	public static final String CONTROLE_ENCODING = "controlEncoding";

	public static final String DEFAULT_CONTROLE_ENCODING = "UTF-8";

	public static final String REMOTE_PATH = "remotePath";

	public static final String DEFAULT_REMOTE_PATH = "/";

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

	public static final String NUM_TESTS_PER_EVICTION_RUN = "numTestsPerEvictionRun";

	public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;

	public static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";

	public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;

	public static final String TEST_WHILE_IDLE = "testWhileIdle";

	public static final boolean DEFAULT_TEST_WHILE_IDLE = false;

	public static final String TEST_ON_BORROW = "testOnBorrow";

	public static final boolean DEFAULT_TEST_ON_BORROW = true;

	public static final String TEST_ON_RETURN = "testOnReturn";

	public static final boolean DEFAULT_TEST_ON_RETURN = false;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { IP, PORT, CONNECT_TIMEOUT, RETRY_NUMBER, RETRY_PAUSE_MILLS,
			USERNAME, PASSWORD, BUFFER_SIZE, CLIENT_MODE, FILE_TYPE, CONTROLE_ENCODING, REMOTE_PATH, MAX_ACTIVE,
			INITIAL_SIZE, MAX_WAIT, MIN_IDLE, MAX_IDLE, TIME_BETWEEN_EVICTION_RUNS_MILLIS, NUM_TESTS_PER_EVICTION_RUN,
			MIN_EVICTABLE_IDLE_TIME_MILLIS, TEST_WHILE_IDLE, TEST_ON_BORROW, TEST_ON_RETURN };

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
			result.setIp(extendedProperties.getString(IP, DEFAULT_IP));
			result.setPort(extendedProperties.getInt(PORT, DEFAULT_PORT));
			result.setConnectTimeout(extendedProperties.getInt(CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT));
			result.setRetryNumber(extendedProperties.getInt(RETRY_NUMBER, DEFAULT_RETRY_NUMBER));
			result.setRetryPauseMills(extendedProperties.getLong(RETRY_PAUSE_MILLS, DEFAULT_RETRY_PAUSE_MILLS));
			result.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
			result.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
			result.setBufferSize(extendedProperties.getInt(BUFFER_SIZE, DEFAULT_BUFFER_SIZE));
			result.setClientMode(extendedProperties.getInt(CLIENT_MODE, DEFAULT_CLIENT_MODE));
			result.setFileType(extendedProperties.getInt(FILE_TYPE, DEFAULT_FILE_TYPE));
			result.setControlEncoding(extendedProperties.getString(CONTROLE_ENCODING, DEFAULT_CONTROLE_ENCODING));
			result.setRemotePath(extendedProperties.getString(REMOTE_PATH, DEFAULT_REMOTE_PATH));
			//
			result.setMaxActive(extendedProperties.getInt(MAX_ACTIVE, DEFAULT_MAX_ACTIVE));
			result.setInitialSize(extendedProperties.getInt(INITIAL_SIZE, DEFAULT_INITIAL_SIZE));
			result.setMaxWait(extendedProperties.getLong(MAX_WAIT, DEFAULT_MAX_WAIT));
			result.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
			result.setMaxIdle(extendedProperties.getInt(MAX_IDLE, DEFAULT_MAX_IDLE));
			//
			result.setTimeBetweenEvictionRunsMillis(extendedProperties.getLong(TIME_BETWEEN_EVICTION_RUNS_MILLIS,
					DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
			result.setNumTestsPerEvictionRun(
					extendedProperties.getInt(NUM_TESTS_PER_EVICTION_RUN, DEFAULT_NUM_TESTS_PER_EVICTION_RUN));
			result.setMinEvictableIdleTimeMillis(
					extendedProperties.getLong(MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
			result.setTestWhileIdle(extendedProperties.getBoolean(TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE));
			result.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
			result.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
			//
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
