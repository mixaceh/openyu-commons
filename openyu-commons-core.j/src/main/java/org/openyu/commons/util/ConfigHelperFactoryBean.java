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

	public static final String CONFIGURATION_RESOURCE = "configurationResource";

	/**
	 * 預設設定檔
	 * 
	 * file:src/test/config/etc/configuration.xml
	 */
	public static final String DEFAULT_CONFIGURATION_RESOURCE = "file:" + ConfigHelper.DEFAULT_CONFIGURATION_FILE;

	//
	public static final String JSON_DIR_RESOURCE = "jsonDirResource";

	/**
	 * 預設json目錄
	 * 
	 * src/test/config/json
	 */
	public static final String DEFAULT_JSON_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_JSON_DIR;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { CONFIGURATION_RESOURCE, JSON_DIR_RESOURCE };

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
			UrlResource configurationResource = new UrlResource(
					extendedProperties.getString(CONFIGURATION_RESOURCE, DEFAULT_CONFIGURATION_RESOURCE));
			ConfigHelper.setConfigurationUrl(configurationResource.getURL());
			//
			UrlResource jsonDirResource = new UrlResource(
					extendedProperties.getString(JSON_DIR_RESOURCE, DEFAULT_JSON_DIR_RESOURCE));
			ConfigHelper.setJsonDirUrl(jsonDirResource.getURL());
			// TODO

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
