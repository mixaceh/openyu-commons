package org.openyu.commons.commons.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSource工廠Supporter
 */
public abstract class BasicDataSourceFactorySupporter<T> extends BaseFactorySupporter<T> {

	private static final long serialVersionUID = -7843419748237271524L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceFactorySupporter.class);

	public static final String URL = "url";

	public static final String DEFAULT_URL = null;

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

	public BasicDataSourceFactorySupporter() {

	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BasicDataSource createBasicDataSource(int i) throws Exception {
		BasicDataSource result = null;
		try {
			result = new BasicDataSource();

			/**
			 * extendedProperties
			 */
			// i=0, jdbc:hsqldb:hsql://localhost:9001/commons
			// i=1, jdbc:hsqldb:hsql://localhost:9001/commons_2
			String url = nextUrl(extendedProperties.getString(URL, DEFAULT_URL), i);
			LOGGER.info("url: " + url);
			result.setUrl(url);
			//
			result.setDriverClassName(extendedProperties.getString(DRIVER_CLASSNAME, DEFAULT_DRIVER_CLASSNAME));
			result.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
			result.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
			//
			result.setMaxActive(extendedProperties.getInt(MAX_ACTIVE, DEFAULT_MAX_ACTIVE));
			result.setInitialSize(extendedProperties.getInt(INITIAL_SIZE, DEFAULT_INITIAL_SIZE));
			result.setMaxWait(extendedProperties.getLong(MAX_WAIT, DEFAULT_MAX_WAIT));
			result.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
			result.setMaxIdle(extendedProperties.getInt(MAX_IDLE, DEFAULT_MAX_IDLE));
			//
			result.setTimeBetweenEvictionRunsMillis(extendedProperties.getLong(TIME_BETWEEN_EVICTION_RUNS_MILLIS,
					DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
			result.setMinEvictableIdleTimeMillis(
					extendedProperties.getLong(MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
			result.setValidationQuery(extendedProperties.getString(VALIDATION_QUERY, DEFAULT_VALIDATION_QUERY));
			result.setTestWhileIdle(extendedProperties.getBoolean(TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE));
			result.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
			result.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
			//
			result.setPoolPreparedStatements(
					extendedProperties.getBoolean(POOL_PREPARED_STATEMENTS, DEFAULT_POOL_PREPARED_STATEMENTS));
			result.setRemoveAbandoned(extendedProperties.getBoolean(REMOVE_ABANDONED, DEFAULT_REMOVE_ABANDONED));
			result.setRemoveAbandonedTimeout(
					extendedProperties.getInt(REMOVE_EABANDONED_TIMEOUT, DEFAULT_REMOVE_EABANDONED_TIMEOUT));
			result.setLogAbandoned(extendedProperties.getBoolean(LOG_ABANDONED, DEFAULT_LOG_ABANDONED));

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createBasicDataSource()").toString(), e);
			try {
				result = (BasicDataSource) shutdownBasicDataSource();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * jdbc:hsqldb:hsql://localhost:9001/commons
	 * 
	 * jdbc:hsqldb:hsql://localhost:9001/commons_2
	 * 
	 * @param url
	 * @param i
	 * @return
	 */
	protected String nextUrl(String url, int i) {
		AssertHelper.notNull(url, "The Url must not be null");
		//
		StringBuilder result = new StringBuilder();
		if (i < 1) {
			return url;
		}
		//
		StringBuilder jdbc = new StringBuilder();
		StringBuilder database = new StringBuilder();
		StringBuilder param = new StringBuilder();
		int pos = url.lastIndexOf("/");
		if (pos > -1) {
			jdbc.append(url.substring(0, pos + 1));
			database.append(url.substring(pos + 1, url.length()));
			pos = database.indexOf("?");
			if (pos > -1) {
				param.append(database.substring(pos, database.length()));
				database = new StringBuilder(database.substring(0, pos));
			}
		}
		//
		result.append(jdbc);
		result.append(database);
		result.append("_");
		result.append(i + 1);
		result.append(param);
		return result.toString();
	}

	protected abstract BasicDataSource shutdownBasicDataSource() throws Exception;
}
