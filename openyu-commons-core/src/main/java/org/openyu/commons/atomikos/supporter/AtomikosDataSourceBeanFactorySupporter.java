package org.openyu.commons.atomikos.supporter;

import java.util.Properties;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public abstract class AtomikosDataSourceBeanFactorySupporter<T> extends BaseFactoryBeanSupporter<T> {

	private static final long serialVersionUID = 1808079562064927282L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(AtomikosDataSourceBeanFactorySupporter.class);

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

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { URL, UNIQUE_RESOURCE_NAME, XA_DATA_SOURCE_CLASSNAME, USER, PASSWORD,
			MAX_POOL_SIZE, MIN_POOL_SIZE, TEST_QUERY, MAX_LIFE_TIME, MAX_IDLE_TIME, BORROW_CONNECTION_TIMEOUT,
			REAP_TIMEOUT, MAINTENANCE_INTERVAL, LOGIN_TIMEOUT };

	public AtomikosDataSourceBeanFactorySupporter() {

	}

	/**
	 * 建構
	 * 
	 * @return
	 * @throws Exception
	 */
	protected AtomikosDataSourceBean createAtomikosDataSourceBean(int i) throws Exception {
		AtomikosDataSourceBean result = null;
		try {
			result = new AtomikosDataSourceBean();

			/**
			 * extendedProperties
			 */
			// i=0, jdbc:hsqldb:hsql://localhost:9001/commons
			// i=1, jdbc:hsqldb:hsql://localhost:9001/commons_2
			result.setUniqueResourceName(
					extendedProperties.getString(UNIQUE_RESOURCE_NAME, DEFAULT_UNIQUE_RESOURCE_NAME)
							+ (i < 1 ? "" : "_" + (i + 1)));
			result.setXaDataSourceClassName(
					extendedProperties.getString(XA_DATA_SOURCE_CLASSNAME, DEFAULT_XA_DATA_SOURCE_CLASSNAME));
			//
			String url = nextUrl(extendedProperties.getString(URL, DEFAULT_URL), i);
			LOGGER.info("[" + i + "] {" + result.getUniqueResourceName() + "} " + url);
			//
			Properties xaProperties = new Properties();
			xaProperties.put(URL, url);
			xaProperties.put(USER, extendedProperties.getString(USER, DEFAULT_USER));
			xaProperties.put(PASSWORD, extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
			result.setXaProperties(xaProperties);
			//
			result.setMaxPoolSize(extendedProperties.getInt(MAX_POOL_SIZE, DEFAULT_MAX_POOL_SIZE));
			result.setMinPoolSize(extendedProperties.getInt(MIN_POOL_SIZE, DEFAULT_MIN_POOL_SIZE));
			result.setTestQuery(extendedProperties.getString(TEST_QUERY, DEFAULT_TEST_QUERY));
			result.setMaxLifetime(extendedProperties.getInt(MAX_LIFE_TIME, DEFAULT_MAX_LIFE_TIME));
			result.setMaxIdleTime(extendedProperties.getInt(MAX_IDLE_TIME, DEFAULT_MAX_IDLE_TIME));
			//
			result.setBorrowConnectionTimeout(
					extendedProperties.getInt(BORROW_CONNECTION_TIMEOUT, DEFAULT_BORROW_CONNECTION_TIMEOUT));
			result.setReapTimeout(extendedProperties.getInt(REAP_TIMEOUT, DEFAULT_REAP_TIMEOUT));
			result.setMaintenanceInterval(
					extendedProperties.getInt(MAINTENANCE_INTERVAL, DEFAULT_MAINTENANCE_INTERVAL));
			result.setLoginTimeout(extendedProperties.getInt(LOGIN_TIMEOUT, DEFAULT_LOGIN_TIMEOUT));
			//
			result.init();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createAtomikosDataSourceBean()").toString(),
					e);
			try {
				result = (AtomikosDataSourceBean) shutdownAtomikosDataSourceBean();
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

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected abstract AtomikosDataSourceBean shutdownAtomikosDataSourceBean() throws Exception;

}
