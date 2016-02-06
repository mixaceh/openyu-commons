package org.openyu.commons.util;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;

/**
 * ConfigHelper工廠
 */
public final class ConfigHelperFactoryBean extends BaseFactoryBeanSupporter<ConfigHelper> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ConfigHelperFactoryBean.class);

	public static final String CONFIGURATION_LOCATION = "configurationLocation";
	/**
	 * 預設設定檔
	 * 
	 * file:src/test/config/etc/configuration.xml
	 */
	public static final String DEFAULT_CONFIGURATION_LOCATION = "file:" + ConfigHelper.DEFAULT_CONFIGURATION_FILE;

	public static final String JSON_DIR_LOCATION = "jsonDirLocation";

	public static final String DEFAULT_JSON_DIR_LOCATION = "file:" + ConfigHelper.DEFAULT_JSON_DIR;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { CONFIGURATION_LOCATION, JSON_DIR_LOCATION };

	public ConfigHelperFactoryBean() {
	}

	/**
	 * 重新初始化ConfigHelper
	 * 
	 * @return
	 */
	protected void reinitializeConfigHelper() throws Exception {
		try {
			/**
			 * extendedProperties
			 */
			// result.setMaxExecutorSize(extendedProperties.getInt(MAX_EXECUTOR_SIZE,
			// DEFAULT_MAX_EXECUTOR_SIZE));
			// result.setCorePoolSize(extendedProperties.getInt(CORE_POOL_SIZE,
			// DEFAULT_CORE_POOL_SIZE));
			// result.setKeepAliveSeconds(extendedProperties.getInt(KEEP_ALIVE_SECONDS,
			// DEFAULT_KEEP_ALIVE_SECONDS));
			// result.setMaxPoolSize(extendedProperties.getInt(MAX_POOL_SIZE,
			// DEFAULT_MAX_POOL_SIZE));
			// result.setQueueCapacity(extendedProperties.getInt(QUEUE_CAPACITY,
			// DEFAULT_QUEUE_CAPACITY));

			UrlResource configurationLocation = new UrlResource(
					extendedProperties.getString(CONFIGURATION_LOCATION, DEFAULT_CONFIGURATION_LOCATION));
			ConfigHelper.setConfigurationLocation(configurationLocation);
			//
			UrlResource jsonDirLocation = new UrlResource(
					extendedProperties.getString(JSON_DIR_LOCATION, DEFAULT_JSON_DIR_LOCATION));
			ConfigHelper.setJsonDirLocation(jsonDirLocation);

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during reinitializeConfigHelper()").toString(), e);
			try {

			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
	}

	@Override
	protected void doStart() throws Exception {
		reinitializeConfigHelper();
	}

	@Override
	protected void doShutdown() throws Exception {

	}

	@Override
	public ConfigHelper getObject() throws Exception {
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		return ConfigHelper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
