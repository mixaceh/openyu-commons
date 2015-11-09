package org.openyu.commons.commons.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ShardingBasicDataSource工廠
 */
public final class ShardingBasicDataSourceFactoryBean<T extends BasicDataSource>
		extends BaseFactorySupporter<BasicDataSource[]> {

	private static final long serialVersionUID = 5865182754049441001L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ShardingBasicDataSourceFactoryBean.class);

	public final static String MAX_SHARDING_SIZE = "maxShardingSize";

	public static final int DEFAULT_MAX_SHARDING_SIZE = 1;

	public final static String URL = "url";

	public static final String DEFAULT_URL = null;

	public final static String DRIVER_CLASSNAME = "driverClassName";

	public static final String DEFAULT_DRIVER_CLASSNAME = null;

	public final static String USERNAME = "username";

	public static final String DEFAULT_USERNAME = null;

	public final static String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;
	//
	public final static String MAX_ACTIVE = "maxActive";

	public static final int DEFAULT_MAX_ACTIVE = 8;

	public final static String INITIAL_SIZE = "initialSize";

	public static final int DEFAULT_INITIAL_SIZE = 0;

	public final static String MAX_WAIT = "maxWait";

	public static final long DEFAULT_MAX_WAIT = -1L;

	public final static String MIN_IDLE = "minIdle";

	public static final int DEFAULT_MIN_IDLE = 0;

	public final static String MAX_IDLE = "maxIdle";

	public static final int DEFAULT_MAX_IDLE = 8;
	//
	public final static String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";

	public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;

	public final static String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";

	public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;

	public final static String VALIDATION_QUERY = "validationQuery";

	public static final String DEFAULT_VALIDATION_QUERY = null;

	public final static String TEST_WHILE_IDLE = "testWhileIdle";

	public static final boolean DEFAULT_TEST_WHILE_IDLE = false;

	public final static String TEST_ON_BORROW = "testOnBorrow";

	public static final boolean DEFAULT_TEST_ON_BORROW = true;

	public final static String TEST_ON_RETURN = "testOnReturn";

	public static final boolean DEFAULT_TEST_ON_RETURN = false;
	//
	public final static String POOL_PREPARED_STATEMENTS = "poolPreparedStatements";

	public static final boolean DEFAULT_POOL_PREPARED_STATEMENTS = false;

	public final static String REMOVE_ABANDONED = "removeAbandoned";

	public static final boolean DEFAULT_REMOVE_ABANDONED = false;

	public final static String REMOVE_EABANDONED_TIMEOUT = "removeAbandonedTimeout";

	public static final int DEFAULT_REMOVE_EABANDONED_TIMEOUT = 300;

	public final static String LOG_ABANDONED = "logAbandoned";

	public static final boolean DEFAULT_LOG_ABANDONED = false;

	private T[] basicDataSource;

	public ShardingBasicDataSourceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createBasicDataSource() throws Exception {
		BasicDataSource[] result = null;
		try {
			result = new BasicDataSource[extendedProperties.getInt(MAX_SHARDING_SIZE, DEFAULT_MAX_SHARDING_SIZE)];
			//
			for (int i = 0; i < result.length; i++) {
				BasicDataSource ds = new BasicDataSource();

				/**
				 * extendedProperties
				 */
				// TODO url
				ds.setUrl(extendedProperties.getString(URL, DEFAULT_URL));
				ds.setDriverClassName(extendedProperties.getString(DRIVER_CLASSNAME, DEFAULT_DRIVER_CLASSNAME));
				ds.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
				ds.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
				//
				ds.setMaxActive(extendedProperties.getInt(MAX_ACTIVE, DEFAULT_MAX_ACTIVE));
				ds.setInitialSize(extendedProperties.getInt(INITIAL_SIZE, DEFAULT_INITIAL_SIZE));
				ds.setMaxWait(extendedProperties.getLong(MAX_WAIT, DEFAULT_MAX_WAIT));
				ds.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
				ds.setMaxIdle(extendedProperties.getInt(MAX_IDLE, DEFAULT_MAX_IDLE));
				//
				ds.setTimeBetweenEvictionRunsMillis(extendedProperties.getLong(TIME_BETWEEN_EVICTION_RUNS_MILLIS,
						DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
				ds.setMinEvictableIdleTimeMillis(extendedProperties.getLong(MIN_EVICTABLE_IDLE_TIME_MILLIS,
						DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
				ds.setValidationQuery(extendedProperties.getString(VALIDATION_QUERY, DEFAULT_VALIDATION_QUERY));
				ds.setTestWhileIdle(extendedProperties.getBoolean(TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE));
				ds.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
				ds.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
				//
				ds.setPoolPreparedStatements(
						extendedProperties.getBoolean(POOL_PREPARED_STATEMENTS, DEFAULT_POOL_PREPARED_STATEMENTS));
				ds.setRemoveAbandoned(extendedProperties.getBoolean(REMOVE_ABANDONED, DEFAULT_REMOVE_ABANDONED));
				ds.setRemoveAbandonedTimeout(
						extendedProperties.getInt(REMOVE_EABANDONED_TIMEOUT, DEFAULT_REMOVE_EABANDONED_TIMEOUT));
				ds.setLogAbandoned(extendedProperties.getBoolean(LOG_ABANDONED, DEFAULT_LOG_ABANDONED));
				//
				result[i] = ds;
			}

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createBasicDataSource()").toString(), e);
			try {
				result = (BasicDataSource[]) shutdownBasicDataSource();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return (T[]) result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected T shutdownBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				for (int i = 0; i < basicDataSource.length; i++) {
					T oldInstance = this.basicDataSource[i];
					oldInstance.close();
					oldInstance = null;
					this.basicDataSource = null;
				}
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownBasicDataSource()").toString(), e);
			throw e;
		}
		return  this.basicDataSource;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected T restartBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				this.basicDataSource = shutdownBasicDataSource();
				this.basicDataSource = createBasicDataSource();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartService()").toString(), e);
			throw e;
		}
		return this.basicDataSource;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.basicDataSource = createBasicDataSource();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.basicDataSource = shutdownBasicDataSource();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.basicDataSource = restartBasicDataSource();
	}

	@Override
	public BasicDataSource[] getObject() throws Exception {
		return basicDataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSource != null) ? this.basicDataSource.getClass() : BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
