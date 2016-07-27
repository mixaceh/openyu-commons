package org.openyu.commons.bitronix;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class PoolingDataSourceGroupFactoryBean extends BaseFactoryBeanSupporter<PoolingDataSource[]> {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(PoolingDataSourceGroupFactoryBean.class);
	// props
	public static final String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	public static final String URL = "url";

	public static final String DEFAULT_URL = null;

	public static final String UNIQUE_RESOURCE_NAME = "uniqueResourceName";

	public static final String DEFAULT_UNIQUE_RESOURCE_NAME = null;

	public static final String XA_DATA_SOURCE_CLASSNAME = "xaDataSourceClassName";

	public static final String DEFAULT_XA_DATA_SOURCE_CLASSNAME = null;

	public static final String USER = "user";

	public static final String DEFAULT_USER = null;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;
	//
	public static final String MAX_POOL_SIZE = "maxPoolSize";

	public static final int DEFAULT_MAX_POOL_SIZE = 8;

	public static final String MIN_POOL_SIZE = "minPoolSize";

	public static final int DEFAULT_MIN_POOL_SIZE = 1;

	public static final String TEST_QUERY = "testQuery";

	public static final String DEFAULT_TEST_QUERY = null;

	public static final String MAX_LIFE_TIME = "maxLifetime";

	public static final int DEFAULT_MAX_LIFE_TIME = 0;

	public static final String MAX_IDLE_TIME = "maxIdleTime";

	public static final int DEFAULT_MAX_IDLE_TIME = 60;

	//
	public static final String BORROW_CONNECTION_TIMEOUT = "borrowConnectionTimeout";

	public static final int DEFAULT_BORROW_CONNECTION_TIMEOUT = 30;

	public static final String REAP_TIMEOUT = "reapTimeout";

	public static final int DEFAULT_REAP_TIMEOUT = 0;

	public static final String MAINTENANCE_INTERVAL = "maintenanceInterval";

	public static final int DEFAULT_MAINTENANCE_INTERVAL = 60;

	public static final String LOGIN_TIMEOUT = "loginTimeout";

	public static final int DEFAULT_LOGIN_TIMEOUT = 0;

	private PoolingDataSource[] poolingDataSourceArray;

	public PoolingDataSourceGroupFactoryBean() {

	}

	public void init() throws RuntimeException {
		super.init();
		//
		try {
			if (poolingDataSourceArray != null) {
				LOGGER.info(new StringBuilder().append("Inject from setPoolingDataSourceArray()").toString());
			} else {
				LOGGER.info("Using createPoolingDataSourceArray()");
				this.poolingDataSourceArray = createPoolingDataSourceArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立多個PoolingDataSource
	 * 
	 * @return
	 * @throws Exception
	 */
	protected PoolingDataSource[] createPoolingDataSourceArray() throws Exception {
		PoolingDataSource[] result = null;
		//
		result = new PoolingDataSource[extendedProperties.getInt(MAX_DATA_SOURCE_SIZE, DEFAULT_MAX_DATA_SOURCE_SIZE)];
		//
		for (int i = 0; i < result.length; i++) {
			PoolingDataSource dataSource = new PoolingDataSource();
			// // dataSource, dataSource_2...
			// dataSource.setUniqueResourceName(
			// extendedProperties.getString(UNIQUE_RESOURCE_NAME,
			// DEFAULT_UNIQUE_RESOURCE_NAME)
			// + (i < 1 ? "" : "_" + (i + 1)));
			// dataSource.setXaDataSourceClassName(
			// extendedProperties.getString(XA_DATA_SOURCE_CLASSNAME,
			// DEFAULT_XA_DATA_SOURCE_CLASSNAME));
			// //
			// String url = nextUrl(extendedProperties.getString(URL,
			// DEFAULT_URL), i);
			// // uniqueResourceName+": "+url
			// LOGGER.info(dataSource.getUniqueResourceName() + ": " + url);
			// //
			// Properties xaProperties = new Properties();
			// xaProperties.put(URL, url);
			// xaProperties.put(USER, extendedProperties.getString(USER,
			// DEFAULT_USER));
			// xaProperties.put(PASSWORD, extendedProperties.getString(PASSWORD,
			// DEFAULT_PASSWORD));
			// dataSource.setXaProperties(xaProperties);
			// //
			// dataSource.setMaxPoolSize(extendedProperties.getInt(MAX_POOL_SIZE,
			// DEFAULT_MAX_POOL_SIZE));
			// dataSource.setMinPoolSize(extendedProperties.getInt(MIN_POOL_SIZE,
			// DEFAULT_MIN_POOL_SIZE));
			// dataSource.setTestQuery(extendedProperties.getString(TEST_QUERY,
			// DEFAULT_TEST_QUERY));
			// dataSource.setMaxLifetime(extendedProperties.getInt(MAX_LIFE_TIME,
			// DEFAULT_MAX_LIFE_TIME));
			// dataSource.setMaxIdleTime(extendedProperties.getInt(MAX_IDLE_TIME,
			// DEFAULT_MAX_IDLE_TIME));
			// //
			// dataSource.setBorrowConnectionTimeout(
			// extendedProperties.getInt(BORROW_CONNECTION_TIMEOUT,
			// DEFAULT_BORROW_CONNECTION_TIMEOUT));
			// dataSource.setReapTimeout(extendedProperties.getInt(REAP_TIMEOUT,
			// DEFAULT_REAP_TIMEOUT));
			// dataSource.setMaintenanceInterval(
			// extendedProperties.getInt(MAINTENANCE_INTERVAL,
			// DEFAULT_MAINTENANCE_INTERVAL));
			// dataSource.setLoginTimeout(extendedProperties.getInt(LOGIN_TIMEOUT,
			// DEFAULT_LOGIN_TIMEOUT));
			// //
			// dataSource.init();
			result[i] = dataSource;
		}
		return result;
	}

	/**
	 * jdbc:mysql://10.16.211.102:3306/neweggsso_db?useUnicode=yes&
	 * characterEncoding=UTF-8
	 * 
	 * @param url
	 * @param i
	 * @return
	 */
	protected String nextUrl(String url, int i) {
		AssertUtil.notNull(url, "The Url must not be null");
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

	@Override
	public PoolingDataSource[] getObject() throws Exception {
		return poolingDataSourceArray;
	}

	public PoolingDataSource[] getPoolingDataSourceArray() {
		return poolingDataSourceArray;
	}

	public void setPoolingDataSourceArray(PoolingDataSource[] poolingDataSourceArray) {
		this.poolingDataSourceArray = poolingDataSourceArray;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.poolingDataSourceArray != null) ? this.poolingDataSourceArray.getClass()
				: PoolingDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
