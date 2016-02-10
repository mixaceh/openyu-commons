package org.openyu.commons.util;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * ConfigHelper工廠
 */
public final class ConfigHelperFactoryBean extends BaseFactoryBeanSupporter<ConfigHelper> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ConfigHelperFactoryBean.class);

	/**
	 * apache configuration file
	 */
	public static final String CONFIGURATION_RESOURCE = "configurationResource";

	/**
	 * 預設設定檔
	 * 
	 * file:src/test/config/etc/configuration.xml
	 */
	public static final String DEFAULT_CONFIGURATION_RESOURCE = "file:" + ConfigHelper.DEFAULT_CONFIGURATION_FILE;

	/**
	 * json目錄
	 */
	public static final String JSON_DIR_RESOURCE = "jsonDirResource";

	/**
	 * 預設json目錄
	 * 
	 * src/test/config/json
	 */
	public static final String DEFAULT_JSON_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_JSON_DIR;

	/**
	 * key目錄
	 */
	public static final String KEY_DIR_RESOURCE = "keyDirResource";

	/**
	 * 預設key目錄
	 * 
	 * src/test/config/key
	 */
	public static final String DEFAULT_KEY_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_KEY_DIR;

	/**
	 * ser目錄
	 */
	public static final String SER_DIR_RESOURCE = "serDirResource";

	/**
	 * 預設ser目錄
	 * 
	 * src/test/config/ser
	 */
	public static final String DEFAULT_SER_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_SER_DIR;

	/**
	 * xml目錄
	 */
	public static final String XML_DIR_RESOURCE = "xmlDirResource";

	/**
	 * 預設xml目錄
	 * 
	 * src/test/config/xml
	 */
	public static final String DEFAULT_XML_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_XML_DIR;

	/**
	 * excel目錄
	 */
	public static final String EXCEL_DIR_RESOURCE = "excelDirResource";

	/**
	 * 預設excel目錄
	 * 
	 * src/test/config/excel
	 */
	public static final String DEFAULT_EXCEL_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_EXCEL_DIR;

	/**
	 * log4j設定檔
	 */
	public static final String LOG4J_CONFIG_RESOURCE = "log4jConfigResource";

	/**
	 * input目錄
	 */
	public static final String INPUT_DIR_RESOURCE = "inputDirResource";

	/**
	 * 預設input目錄
	 * 
	 * custom/input
	 */
	public static final String DEFAULT_INPUT_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_INPUT_DIR;

	/**
	 * output目錄
	 */
	public static final String OUTPUT_DIR_RESOURCE = "outputDirResource";

	/**
	 * 預設output目錄
	 * 
	 * custom/output
	 */
	public static final String DEFAULT_OUTPUT_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_OUTPUT_DIR;

	/**
	 * download目錄
	 */
	public static final String DOWNLOAD_DIR_RESOURCE = "downloadDirResource";

	/**
	 * 預設download目錄
	 * 
	 * custom/download
	 */
	public static final String DEFAULT_DOWNLOAD_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_DOWNLOAD_DIR;

	/**
	 * upload目錄
	 */
	public static final String UPLOAD_DIR_RESOURCE = "uploadDirResource";

	/**
	 * 預設upload目錄
	 * 
	 * custom/upload
	 */
	public static final String DEFAULT_UPLOAD_DIR_RESOURCE = "file:" + ConfigHelper.DEFAULT_UPLOAD_DIR;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { CONFIGURATION_RESOURCE, JSON_DIR_RESOURCE, KEY_DIR_RESOURCE,
			KEY_DIR_RESOURCE, XML_DIR_RESOURCE, EXCEL_DIR_RESOURCE, LOG4J_CONFIG_RESOURCE, INPUT_DIR_RESOURCE,
			OUTPUT_DIR_RESOURCE, DOWNLOAD_DIR_RESOURCE, UPLOAD_DIR_RESOURCE };

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
			// apache configuration file
			UrlResource configurationResource = new UrlResource(
					extendedProperties.getString(CONFIGURATION_RESOURCE, DEFAULT_CONFIGURATION_RESOURCE));
			ConfigHelper.setConfigurationUrl(configurationResource.getURL());
			//
			UrlResource jsonDirResource = new UrlResource(
					extendedProperties.getString(JSON_DIR_RESOURCE, DEFAULT_JSON_DIR_RESOURCE));
			ConfigHelper.setJsonDirUrl(jsonDirResource.getURL());
			//
			UrlResource keyDirResource = new UrlResource(
					extendedProperties.getString(KEY_DIR_RESOURCE, DEFAULT_KEY_DIR_RESOURCE));
			ConfigHelper.setKeyDirUrl(keyDirResource.getURL());
			//
			UrlResource serDirResource = new UrlResource(
					extendedProperties.getString(SER_DIR_RESOURCE, DEFAULT_SER_DIR_RESOURCE));
			ConfigHelper.setSerDirUrl(serDirResource.getURL());
			//
			UrlResource xmlDirResource = new UrlResource(
					extendedProperties.getString(XML_DIR_RESOURCE, DEFAULT_XML_DIR_RESOURCE));
			ConfigHelper.setXmlDirUrl(xmlDirResource.getURL());
			//
			UrlResource excelDirResource = new UrlResource(
					extendedProperties.getString(EXCEL_DIR_RESOURCE, DEFAULT_EXCEL_DIR_RESOURCE));
			ConfigHelper.setExcelDirUrl(excelDirResource.getURL());

			// log4j
			/**
			 * log4jConfigResource=file:src/test/resources/log4j2.xml
			 * log4jConfigResource=/log4j2.xml
			 * log4jConfigResource=classpath:log4j2.xml
			 */
			String log4jConfig = extendedProperties.getString(LOG4J_CONFIG_RESOURCE);
			if (log4jConfig != null) {
				Resource log4jConfigResource = null;
				int pos = log4jConfig.indexOf("/");
				if (pos > -1) {
					log4jConfigResource = new ClassPathResource(new String(log4jConfig.substring(pos)));
				} else {
					final String CLASSPATH = "classpath:";
					pos = log4jConfig.indexOf(CLASSPATH);
					if (pos > -1) {
						log4jConfigResource = new ClassPathResource(
								new String(log4jConfig.substring(pos + CLASSPATH.length())));
					} else {
						log4jConfigResource = new UrlResource(log4jConfig);
					}
				}
				ConfigHelper.setLog4jConfigUrl(log4jConfigResource.getURL());
				//
				UrlResource inputDirResource = new UrlResource(
						extendedProperties.getString(INPUT_DIR_RESOURCE, DEFAULT_INPUT_DIR_RESOURCE));
				ConfigHelper.setInputDirUrl(inputDirResource.getURL());

				//
				UrlResource ouputDirResource = new UrlResource(
						extendedProperties.getString(OUTPUT_DIR_RESOURCE, DEFAULT_OUTPUT_DIR_RESOURCE));
				ConfigHelper.setOutputDirUrl(ouputDirResource.getURL());
				//
				UrlResource downloadDirResource = new UrlResource(
						extendedProperties.getString(DOWNLOAD_DIR_RESOURCE, DEFAULT_DOWNLOAD_DIR_RESOURCE));
				ConfigHelper.setDownloadDirUrl(downloadDirResource.getURL());
				//
				UrlResource uploadDirResource = new UrlResource(
						extendedProperties.getString(UPLOAD_DIR_RESOURCE, DEFAULT_UPLOAD_DIR_RESOURCE));
				ConfigHelper.setUploadDirUrl(uploadDirResource.getURL());

			}
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
