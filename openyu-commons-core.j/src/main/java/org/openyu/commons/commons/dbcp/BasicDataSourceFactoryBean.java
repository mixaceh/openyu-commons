package org.openyu.commons.commons.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSource工廠
 */
public final class BasicDataSourceFactoryBean extends BasicDataSourceFactorySupporter<BasicDataSource> {

	private static final long serialVersionUID = -3534351911211538982L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceFactoryBean.class);

	private BasicDataSource basicDataSource;

	public BasicDataSourceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BasicDataSource createBasicDataSource() throws Exception {
		BasicDataSource result = null;
		try {
			result = new BasicDataSource();

			/**
			 * extendedProperties
			 */
			result.setUrl(extendedProperties.getString(URL, DEFAULT_URL));
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
	 * 關閉
	 *
	 * @return
	 */
	protected BasicDataSource shutdownBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				BasicDataSource oldInstance = this.basicDataSource;
				oldInstance.close();
				this.basicDataSource = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownBasicDataSource()").toString(), e);
			throw e;
		}
		return this.basicDataSource;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected BasicDataSource restartBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				this.basicDataSource = shutdownBasicDataSource();
				this.basicDataSource = createBasicDataSource();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartBasicDataSource()").toString(), e);
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
	public BasicDataSource getObject() throws Exception {
		return basicDataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSource != null) ? this.basicDataSource.getClass() : BasicDataSource.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
