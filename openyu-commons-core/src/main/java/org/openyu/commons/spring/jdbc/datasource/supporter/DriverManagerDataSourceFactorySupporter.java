package org.openyu.commons.spring.jdbc.datasource.supporter;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * DriverManagerDataSource工廠Supporter
 * 
 * @param <T>
 */
public abstract class DriverManagerDataSourceFactorySupporter<T> extends BaseFactoryBeanSupporter<T> {

	private static final long serialVersionUID = 3531071342416922607L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(DriverManagerDataSourceFactorySupporter.class);

	public static final String URL = "url";

	public static final String DEFAULT_URL = null;

	public static final String DRIVER_CLASSNAME = "driverClassName";

	public static final String DEFAULT_DRIVER_CLASSNAME = null;

	public static final String USERNAME = "username";

	public static final String DEFAULT_USERNAME = null;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { URL, DRIVER_CLASSNAME, USERNAME, PASSWORD };

	public DriverManagerDataSourceFactorySupporter() {

	}

	/**
	 * 建立
	 * 
	 * i=0, jdbc:hsqldb:hsql://127.0.0.1:9001/commons
	 * 
	 * i=1, jdbc:hsqldb:hsql://127.0.0.1:9001/commons_2
	 * 
	 * @param i
	 * @return
	 * @throws Exception
	 */
	protected DriverManagerDataSource createDriverManagerDataSource(int i) throws Exception {
		DriverManagerDataSource result = null;
		try {
			result = new DriverManagerDataSource();

			/**
			 * extendedProperties
			 */
			String url = nextUrl(extendedProperties.getString(URL, DEFAULT_URL), i);
			LOGGER.info("driver[" + i + "]: " + url);
			result.setUrl(url);
			//
			result.setDriverClassName(extendedProperties.getString(DRIVER_CLASSNAME, DEFAULT_DRIVER_CLASSNAME));
			result.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
			result.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createDriverManagerDataSource()").toString(),
					e);
			try {
				result = shutdownDriverManagerDataSource();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * jdbc:mysql://127.0.0.1:3306/commons?useUnicode=yes&
	 * characterEncoding=UTF-8
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

	protected abstract DriverManagerDataSource shutdownDriverManagerDataSource() throws Exception;
}
