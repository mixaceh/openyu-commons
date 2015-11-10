package org.openyu.commons.commons.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSourceGroup工廠
 */
public final class BasicDataSourceGroupFactoryBean extends BasicDataSourceFactorySupporter<BasicDataSource[]> {

	private static final long serialVersionUID = 5865182754049441001L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceGroupFactoryBean.class);

	public final static String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	private BasicDataSource[] basicDataSources;

	public BasicDataSourceGroupFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BasicDataSource[] createBasicDataSources() throws Exception {
		BasicDataSource[] result = null;
		try {
			result = new BasicDataSource[extendedProperties.getInt(MAX_DATA_SOURCE_SIZE, DEFAULT_MAX_DATA_SOURCE_SIZE)];
			//
			for (int i = 0; i < result.length; i++) {
				BasicDataSource dataSource = new BasicDataSource();

				/**
				 * extendedProperties
				 */
				// TODO url
				dataSource.setUrl(extendedProperties.getString(URL, DEFAULT_URL));
				dataSource.setDriverClassName(extendedProperties.getString(DRIVER_CLASSNAME, DEFAULT_DRIVER_CLASSNAME));
				dataSource.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
				dataSource.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
				//
				dataSource.setMaxActive(extendedProperties.getInt(MAX_ACTIVE, DEFAULT_MAX_ACTIVE));
				dataSource.setInitialSize(extendedProperties.getInt(INITIAL_SIZE, DEFAULT_INITIAL_SIZE));
				dataSource.setMaxWait(extendedProperties.getLong(MAX_WAIT, DEFAULT_MAX_WAIT));
				dataSource.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
				dataSource.setMaxIdle(extendedProperties.getInt(MAX_IDLE, DEFAULT_MAX_IDLE));
				//
				dataSource.setTimeBetweenEvictionRunsMillis(extendedProperties
						.getLong(TIME_BETWEEN_EVICTION_RUNS_MILLIS, DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
				dataSource.setMinEvictableIdleTimeMillis(extendedProperties.getLong(MIN_EVICTABLE_IDLE_TIME_MILLIS,
						DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
				dataSource.setValidationQuery(extendedProperties.getString(VALIDATION_QUERY, DEFAULT_VALIDATION_QUERY));
				dataSource.setTestWhileIdle(extendedProperties.getBoolean(TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE));
				dataSource.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
				dataSource.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
				//
				dataSource.setPoolPreparedStatements(
						extendedProperties.getBoolean(POOL_PREPARED_STATEMENTS, DEFAULT_POOL_PREPARED_STATEMENTS));
				dataSource
						.setRemoveAbandoned(extendedProperties.getBoolean(REMOVE_ABANDONED, DEFAULT_REMOVE_ABANDONED));
				dataSource.setRemoveAbandonedTimeout(
						extendedProperties.getInt(REMOVE_EABANDONED_TIMEOUT, DEFAULT_REMOVE_EABANDONED_TIMEOUT));
				dataSource.setLogAbandoned(extendedProperties.getBoolean(LOG_ABANDONED, DEFAULT_LOG_ABANDONED));
				//
				result[i] = dataSource;
			}

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createBasicDataSources()").toString(), e);
			try {
				result = (BasicDataSource[]) shutdownBasicDataSources();
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
	protected BasicDataSource[] shutdownBasicDataSources() throws Exception {
		try {
			if (this.basicDataSources != null) {
				for (int i = 0; i < this.basicDataSources.length; i++) {
					BasicDataSource oldInstance = this.basicDataSources[i];
					oldInstance.close();
					this.basicDataSources[i] = null;
				}
				//
				this.basicDataSources = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownBasicDataSources()").toString(), e);
			throw e;
		}
		return this.basicDataSources;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected BasicDataSource[] restartBasicDataSources() throws Exception {
		try {
			if (this.basicDataSources != null) {
				this.basicDataSources = shutdownBasicDataSources();
				this.basicDataSources = createBasicDataSources();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartBasicDataSources()").toString(), e);
			throw e;
		}
		return this.basicDataSources;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.basicDataSources = createBasicDataSources();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.basicDataSources = shutdownBasicDataSources();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.basicDataSources = restartBasicDataSources();
	}

	@Override
	public BasicDataSource[] getObject() throws Exception {
		return basicDataSources;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSources != null) ? this.basicDataSources.getClass() : BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
