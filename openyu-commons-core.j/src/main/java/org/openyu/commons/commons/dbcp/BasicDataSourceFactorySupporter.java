package org.openyu.commons.commons.dbcp;

import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSource工廠Supporter
 */
public abstract class BasicDataSourceFactorySupporter<T> extends BaseFactorySupporter<T> {

	private static final long serialVersionUID = -7843419748237271524L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceFactorySupporter.class);

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

	public BasicDataSourceFactorySupporter() {
	}

}
