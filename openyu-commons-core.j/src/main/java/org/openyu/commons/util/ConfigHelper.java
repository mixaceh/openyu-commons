package org.openyu.commons.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.web.context.support.ServletContextResource;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.FileHelper;
import org.openyu.commons.security.SecurityType;

/**
 * 1.預設設定檔: src/test/config/etc/configuration.xml
 * 
 * 2.直接使用static方法取值
 * 
 * 3.可利用spring重新給設定檔路徑configurationLocation
 */
public final class ConfigHelper extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

	/**
	 * 是否由Static()建構
	 */
	private static boolean buildWithStatic;
	/**
	 * 預設設定檔目錄
	 * 
	 * src/test/config
	 */
	public final static String DEFAULT_CONFIG_DIR = "src" + File.separator + "test" + File.separator + "config";

	/**
	 * apache configuration file
	 * 
	 * 預設設定檔檔名
	 * 
	 * configuration.xml
	 */
	public final static String DEFAULT_CONFIGURATION_FILE_NAME = "configuration.xml";

	/**
	 * 預設設定檔
	 * 
	 * src/test/config/etc/configuration.xml
	 */
	public final static String DEFAULT_CONFIGURATION_FILE = DEFAULT_CONFIG_DIR + File.separator + "etc" + File.separator
			+ DEFAULT_CONFIGURATION_FILE_NAME;

	/**
	 * 設定檔
	 */
	private static String configurationFile = DEFAULT_CONFIGURATION_FILE;

	/**
	 * 設定檔資源
	 * 
	 * file:src/test/config/etc/configuration.xml
	 */
	private static URL configurationUrl;

	private static DefaultConfigurationBuilder configurationBuilder;

	private static CombinedConfiguration configuration;

	// --------------------------------------------------------
	// default dir
	// --------------------------------------------------------
	/**
	 * 預設json目錄
	 * 
	 * src/test/config/json
	 */
	public final static String DEFAULT_JSON_DIR = DEFAULT_CONFIG_DIR + File.separator + "json";

	/**
	 * json目錄
	 */
	private static String jsonDir = DEFAULT_JSON_DIR;

	/**
	 * json目錄資源
	 */
	private static URL jsonDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設key目錄
	 * 
	 * src/test/config/key
	 */
	public final static String DEFAULT_KEY_DIR = DEFAULT_CONFIG_DIR + File.separator + "key";

	/**
	 * key目錄
	 */
	private static String keyDir = DEFAULT_KEY_DIR;

	/**
	 * key目錄資源
	 */
	private static URL keyDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設ser目錄
	 * 
	 * src/test/config/ser
	 */
	public final static String DEFAULT_SER_DIR = DEFAULT_CONFIG_DIR + File.separator + "ser";

	/**
	 * ser目錄
	 */
	private static String serDir = DEFAULT_SER_DIR;

	/**
	 * ser目錄資源
	 */
	private static URL serDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設xml目錄
	 * 
	 * src/test/config/xml
	 */
	public final static String DEFAULT_XML_DIR = DEFAULT_CONFIG_DIR + File.separator + "xml";

	/**
	 * xml目錄
	 */
	private static String xmlDir = DEFAULT_XML_DIR;

	/**
	 * xml目錄資源
	 */
	private static URL xmlDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設excel目錄
	 * 
	 * src/test/config/excel
	 */
	public final static String DEFAULT_EXCEL_DIR = DEFAULT_CONFIG_DIR + File.separator + "excel";

	/**
	 * excel目錄
	 */
	private static String excelDir = DEFAULT_EXCEL_DIR;

	/**
	 * excel目錄資源
	 */
	private static URL excelDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設Log4j設定檔檔名
	 * 
	 * log4j.properties
	 */
	public final static String DEFAULT_LOG4J_CONFIG_FILE_NAME = "log4j.properties";

	/**
	 * 預設Log4j2設定檔檔名, 支援 XML, JSON, YAML, or properties format
	 * 
	 * log4j2.xml
	 */
	public final static String DEFAULT_LOG4J2_CONFIG_FILE_NAME = "log4j2.xml";

	/**
	 * 預設log4j設定檔
	 * 
	 * log4j.properties
	 */
	public final static String DEFAULT_LOG4J_CONFIG_FILE = DEFAULT_LOG4J_CONFIG_FILE_NAME;

	/**
	 * log4j設定檔
	 */
	private static String log4jConfigFile = DEFAULT_LOG4J_CONFIG_FILE;

	/**
	 * log4j設定檔
	 */
	private static URL log4jConfigUrl;

	// --------------------------------------------------------
	// custom
	// --------------------------------------------------------

	/**
	 * 預設客製目錄
	 */
	private static String DEFAULT_CUSTOM_DIR = "custom";

	// --------------------------------------------------------
	/**
	 * 預設input目錄
	 * 
	 * custom/input
	 */
	public final static String DEFAULT_INPUT_DIR = DEFAULT_CUSTOM_DIR + File.separator + "input";

	/**
	 * input目錄
	 */
	private static String inputDir = DEFAULT_INPUT_DIR;

	/**
	 * input目錄資源
	 */
	private static URL inputDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設output目錄
	 * 
	 * custom/output
	 */
	public final static String DEFAULT_OUTPUT_DIR = DEFAULT_CUSTOM_DIR + File.separator + "output";

	/**
	 * output目錄
	 */
	private static String outputDir = DEFAULT_OUTPUT_DIR;

	/**
	 * output目錄資源
	 */
	private static URL outputDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設download目錄
	 */
	public final static String DEFAULT_DOWNLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "download";

	/**
	 * download目錄
	 */
	private static String downloadDir = DEFAULT_DOWNLOAD_DIR;

	/**
	 * download目錄資源
	 */
	private static URL downloadDirUrl;

	// --------------------------------------------------------
	/**
	 * 預設upload目錄
	 */
	public final static String DEFAULT_UPLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "upload";

	/**
	 * upload目錄
	 */
	private static String uploadDir = DEFAULT_UPLOAD_DIR;

	/**
	 * upload目錄資源,由spring注入
	 */
	private static URL uploadDirUrl;

	// --------------------------------------------------------
	// config-op.xml
	// --------------------------------------------------------
	/** 是否檢查碼 */
	public final static String CHECKSUM = "configHelper.checksum";

	// private static boolean checksum;

	/** 檢查碼類別 */
	public final static String CHECKSUM_TYPE = "configHelper.checksumType";

	// private static ChecksumType checksumType;

	/** 檢查碼key */
	public final static String CHECKSUM_KEY = "configHelper.checksumKey";

	// private static String checksumKey;

	/** 是否序列化 */
	public final static String SERIALIZE = "configHelper.serialize";

	// private static boolean serialize;

	/** 序列化類別 */
	public final static String SERIALIZE_TYPE = "configHelper.serializeType";

	// private static SerializeType serializeType;

	/** 是否安全性 */
	public final static String SECURITY = "configHelper.security";

	// private static boolean security;

	/** 安全性類別 */
	public final static String SECURITY_TYPE = "configHelper.securityType";

	/** 安全性key */
	public final static String SECURITY_KEY = "configHelper.securityKey";

	/** 是否壓縮 */

	public final static String COMPRESS = "configHelper.compress";

	/** 壓縮類別 */
	public final static String COMPRESS_TYPE = "configHelper.compressType";

	// --------------------------------------------------------
	// config-debug.xml
	// --------------------------------------------------------
	//
	/**
	 * 是否除錯
	 */
	public final static String DEBUG = "configHelper.debug";

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			buildWithStatic = true;
			buildWithFile(null);
		}
	}

	public ConfigHelper() {

	}

	public static String getConfigurationFile() {
		return configurationFile;
	}

	public static void setConfigurationFile(String configurationFile) {
		ConfigHelper.configurationFile = configurationFile;
		ConfigHelper.configurationUrl = null;
		//
		buildWithStatic = false;
		buildWithFile(configurationFile);
	}

	public static URL getConfigurationUrl() {
		return configurationUrl;
	}

	public static void setConfigurationUrl(URL configurationUrl) {
		ConfigHelper.configurationUrl = configurationUrl;
		ConfigHelper.configurationFile = configurationUrl.getFile();
		//
		buildWithStatic = false;
		buildWithFile(null);
	}

	// --------------------------------------------------------
	public static String getJsonDir() {
		return jsonDir;
	}

	public static void setJsonDir(String jsonDir) {
		ConfigHelper.jsonDir = jsonDir;
		ConfigHelper.jsonDirUrl = null;
		//
		buildDir(jsonDirUrl, jsonDir);
	}

	public static URL getJsonDirUrl() {
		return jsonDirUrl;
	}

	public static void setJsonDirUrl(URL jsonDirUrl) {
		ConfigHelper.jsonDirUrl = jsonDirUrl;
		ConfigHelper.jsonDir = jsonDirUrl.getFile();
		//
		buildDir(jsonDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getKeyDir() {
		return keyDir;
	}

	public static void setKeyDir(String keyDir) {
		ConfigHelper.keyDir = keyDir;
		ConfigHelper.keyDirUrl = null;
		//
		buildDir(keyDirUrl, keyDir);
	}

	public static URL getKeyDirUrl() {
		return keyDirUrl;
	}

	public static void setKeyDirUrl(URL keyDirUrl) {
		ConfigHelper.keyDirUrl = keyDirUrl;
		ConfigHelper.keyDir = keyDirUrl.getFile();
		//
		buildDir(keyDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getSerDir() {
		return serDir;
	}

	public static void setSerDir(String serDir) {
		ConfigHelper.serDir = serDir;
		ConfigHelper.serDirUrl = null;
		//
		buildDir(serDirUrl, serDir);
	}

	public static URL getSerDirUrl() {
		return serDirUrl;
	}

	public static void setSerDirUrl(URL serDirUrl) {
		ConfigHelper.serDirUrl = serDirUrl;
		ConfigHelper.serDir = serDirUrl.getFile();
		//
		buildDir(serDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getXmlDir() {
		return xmlDir;
	}

	public static void setXmlDir(String xmlDir) {
		ConfigHelper.xmlDir = xmlDir;
		ConfigHelper.xmlDirUrl = null;
		//
		buildDir(xmlDirUrl, xmlDir);
	}

	public static URL getXmlDirUrl() {
		return xmlDirUrl;
	}

	public static void setXmlDirUrl(URL xmlUrl) {
		ConfigHelper.xmlDirUrl = xmlUrl;
		ConfigHelper.xmlDir = xmlUrl.getFile();
		//
		buildDir(xmlUrl, null);
	}

	// --------------------------------------------------------
	public static String getExcelDir() {
		return excelDir;
	}

	public static void setExcelDir(String excelDir) {
		ConfigHelper.excelDir = excelDir;
		ConfigHelper.excelDirUrl = null;
		//
		buildDir(excelDirUrl, excelDir);
	}

	public static URL getExcelDirUrl() {
		return excelDirUrl;
	}

	public static void setExcelDirUrl(URL excelDirUrl) {
		ConfigHelper.excelDirUrl = excelDirUrl;
		ConfigHelper.excelDir = excelDirUrl.getFile();
		//
		buildDir(excelDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getLog4jConfigFile() {
		return log4jConfigFile;
	}

	public static void setLog4jConfigFile(String log4jConfigFile) {
		ConfigHelper.log4jConfigFile = log4jConfigFile;
		ConfigHelper.log4jConfigUrl = null;
		//
		int pos = log4jConfigFile.indexOf("log4j2");
		// log4j
		if (pos < 0) {
			PropertyConfigurator.configure(log4jConfigFile);
			LOGGER.info("Using Log4j");

		} else {
			// log4j2
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			context.setConfigLocation(new File(log4jConfigFile).toURI());
			LOGGER.info("Using Log4j2");
		}
	}

	/**
	 * spring注入
	 * 
	 * @return
	 */
	public static URL getLog4jConfigUrl() {
		return log4jConfigUrl;
	}

	public static void setLog4jConfigUrl(URL log4jConfigUrl) throws Exception {
		ConfigHelper.log4jConfigUrl = log4jConfigUrl;
		ConfigHelper.log4jConfigFile = log4jConfigUrl.getFile();
		//
		int pos = log4jConfigFile.indexOf("log4j2");
		// log4j
		if (pos < 0) {
			PropertyConfigurator.configure(log4jConfigFile);
			LOGGER.info("Using Log4j");
		} else {
			// log4j2
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			context.setConfigLocation(log4jConfigUrl.toURI());
			LOGGER.info("Using Log4j2");
		}
	}

	// --------------------------------------------------------
	public static String getInputDir() {
		return inputDir;
	}

	public static void setInputDir(String inputDir) {
		ConfigHelper.inputDir = inputDir;
		ConfigHelper.inputDirUrl = null;
		//
		buildDir(inputDirUrl, inputDir);
	}

	public static URL getInputDirUrl() {
		return inputDirUrl;
	}

	public static void setInputDirUrl(URL inputDirUrl) {
		ConfigHelper.inputDirUrl = inputDirUrl;
		ConfigHelper.inputDir = inputDirUrl.getFile();
		//
		buildDir(inputDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getOutputDir() {
		return outputDir;
	}

	public static void setOutputDir(String outputDir) {
		ConfigHelper.outputDir = outputDir;
		ConfigHelper.outputDirUrl = null;
		//
		buildDir(outputDirUrl, outputDir);
	}

	public static URL getOutputDirUrl() {
		return outputDirUrl;
	}

	public static void setOutputDirUrl(URL outputDirUrl) {
		ConfigHelper.outputDirUrl = outputDirUrl;
		ConfigHelper.outputDir = outputDirUrl.getFile();
		//
		buildDir(outputDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getDownloadDir() {
		return downloadDir;
	}

	public static void setDownloadDir(String downloadDir) {
		ConfigHelper.downloadDir = downloadDir;
		ConfigHelper.downloadDirUrl = null;
		//
		buildDir(downloadDirUrl, downloadDir);
	}

	public static URL getDownloadUrl() {
		return downloadDirUrl;
	}

	public static void setDownloadDirUrl(URL downloadDirUrl) {
		ConfigHelper.downloadDirUrl = downloadDirUrl;
		ConfigHelper.downloadDir = downloadDirUrl.getFile();
		//
		buildDir(downloadDirUrl, null);
	}

	// --------------------------------------------------------
	public static String getUploadDir() {
		return uploadDir;
	}

	public static void setUploadDir(String uploadDir) {
		ConfigHelper.uploadDir = uploadDir;
		ConfigHelper.uploadDirUrl = null;
		//
		buildDir(uploadDirUrl, uploadDir);
	}

	public static URL getUploadDirUrl() {
		return uploadDirUrl;
	}

	public static void setUploadDirUrl(URL uploadDirUrl) {
		ConfigHelper.uploadDirUrl = uploadDirUrl;
		ConfigHelper.uploadDir = uploadDirUrl.getFile();
		//
		buildDir(uploadDirUrl, null);
	}

	// --------------------------------------------------------

	/**
	 * 建構
	 * 
	 * @param assignFile
	 *            指定檔案
	 */
	protected static void buildWithFile(String assignFile) {
		try {
			buildConfigurationBuilder(assignFile);
			if (configurationBuilder == null) {
				if (buildWithStatic) {
					return;
				} else {
					LOGGER.error(
							"Can not find configLocation: " + (assignFile != null ? assignFile : configurationUrl));
					return;
				}
			}
			//
			configuration = configurationBuilder.getConfiguration(true);
			// 2014/11/25,
			// 需設為true,否則即是在configuration.xml有設FileChangedReloadingStrategy,也是無法reload
			configuration.setForceReloadCheck(true);

			// // config-op.xml
			// #issue 為了reload,應改寫在getXxx中
			// checksum
			// checksum = getBoolean(CHECKSUM, false);
			// checksumType = EnumHelper.valueOf(ChecksumType.class,
			// getString(CHECKSUM_TYPE, null));
			// checksumKey = getString(CHECKSUM_KEY, "");
			//
			// // serialize
			// serialize = getBoolean(SERIALIZE, false);
			// serializeType = EnumHelper.valueOf(SerializeType.class,
			// getString(SERIALIZE_TYPE, null));
			//
			// // security
			// security = getBoolean(SECURITY, false);
			// securityType = EnumHelper.valueOf(SecurityType.class,
			// getString(SECURITY_TYPE, null));
			// securityKey = getString(SECURITY_KEY, "");
			//
			// // compressType
			// compress = getBoolean(COMPRESS, false);
			// compressType = EnumHelper.valueOf(CompressType.class,
			// getString(COMPRESS_TYPE, null));
			// //
			// // config-debug.xml
			// debug = getBoolean(DEBUG, false);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 建構builder
	 * 
	 * @param assignFile
	 */
	protected static void buildConfigurationBuilder(String assignFile) {
		//
		try {
			// 當沒使用spring注入時,或指定設定檔
			if (configurationUrl == null || assignFile != null) {
				String fileName = (assignFile != null ? assignFile : DEFAULT_CONFIGURATION_FILE);
				File file = new File(fileName);
				if (file.exists()) {
					configurationBuilder = new DefaultConfigurationBuilder();
					configurationBuilder.setFile(file);
					//
					if (buildWithStatic) {
						LOGGER.info("Initialization of file[" + fileName + "]");
					} else {
						LOGGER.info("Reinitialization of file[" + fileName + "]");
					}
				} else {
					// 當在web下,會出現src\test\config\etc\configuration.xml File does
					// not exist, 預設的檔案找不到, 這是正常的
					//
					// 1.若在spring下, 可在applicationContext-ini.xml可重新設定
					//
					// 2.若在junit下, 設定檔的目錄會放不同, 所以測試時會發生找不到檔案的狀況,因此單元測試中
					// 更改以下設定的目錄
					//
					// ConfigHelper
					// .setConfigFile("src/main/webapp/WEB-INF/config/etc/configuration.xml");
					LOGGER.warn("[" + fileName + "] File does not exist");
				}
			}
			// 使用spring注入時
			else {
				// file:src/test/config/etc/configuration.xml
				// src/test/config/etc/configuration.xml
				if (configurationUrl != null) {
					configurationBuilder = new DefaultConfigurationBuilder(configurationUrl);
					LOGGER.info("Reinitialize of file[" + configurationUrl.getFile() + "]");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 建構目錄
	 * 
	 * @param defaultDir
	 * @param resource
	 */
	protected static void buildDir(URL url, String assignDir) {
		try {
			// 當沒使用spring注入時,或指定目錄
			if (url == null || assignDir != null) {
				File dir = new File(assignDir);
				FileHelper.md(dir);
			}
			// 使用spring注入時
			else {
				// web
				// /WEB-INF/xml
				// /custom/output

				// TODO in web
				// if (resource instanceof ServletContextResource) {
				// ServletContextResource recource = (ServletContextResource)
				// resource;
				// // 1./cms/WEB-INF/xml
				// // 2./cms/custom/input
				// FileHelper.md(recource.getFile().getAbsolutePath());
				// }
				// file:xml
				// xml
				// custom/input
				// else {
				FileHelper.md(url.getFile());
				// }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isDebug() {
		// return debug;
		try {
			return getBoolean(DEBUG, false);
		} catch (Exception ex) {
		}
		return true;
	}

	public static CombinedConfiguration getConfiguration() {
		return configuration;
	}

	public static boolean isEmpty() {
		return configuration.isEmpty();
	}

	public static boolean containsKey(String key) {
		return configuration.containsKey(key);
	}

	public static void addProperty(String key, Object value) {
		configuration.addProperty(key, value);
	}

	public static void setProperty(String key, Object value) {
		configuration.setProperty(key, value);
	}

	public static void clearProperty(String key) {
		configuration.clearProperty(key);
	}

	public static Object getProperty(String key) {
		return configuration.getProperty(key);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(String prefix) {
		// try {
		// return configurationBuilder.getConfiguration().getKeys(prefix);
		return configuration.getKeys(prefix);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return null;
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys() {
		return configuration.getKeys();
	}

	public static Properties getProperties(String key) {
		return configuration.getProperties(key);
	}

	public static boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public static byte getByte(String key) {
		return configuration.getByte(key);
	}

	public static byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public static short getShort(String key) {
		return configuration.getShort(key);
	}

	public static short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public static int getInt(String key) {
		return configuration.getInt(key);
	}

	public static int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	public static long getLong(String key) {
		return configuration.getLong(key);
	}

	public static long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public static float getFloat(String key) {
		return configuration.getFloat(key);
	}

	public static float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public static double getDouble(String key) {
		return configuration.getDouble(key);
	}

	public static double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public static BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	public static BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	public static String getString(String key) {
		return configuration.getString(key);
	}

	public static String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	public static String[] getStringArray(String key) {
		return configuration.getStringArray(key);
	}

	@SuppressWarnings("unchecked")
	public static List<Object> getList(String key) {
		return configuration.getList(key);
	}

	@SuppressWarnings("unchecked")
	public static List<Object> getList(String key, List<String> defaultValue) {
		return configuration.getList(key, defaultValue);
	}

	public static Map<String, String> getMap(String key) {
		Map<String, String> result = null;
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append(".entry");
		// System.out.println(sb);
		List<HierarchicalConfiguration> list = configurationsAt(sb.toString());
		// System.out.println(list.size());
		if (!list.isEmpty()) {
			result = new LinkedHashMap<String, String>();
			for (HierarchicalConfiguration node : list) {
				String nodeKey = node.getString("key");
				String nodeValue = node.getString("value");
				// System.out.println(nodeKey+" "+nodeValue);
				result.put(nodeKey, nodeValue);
			}
		}
		return result;
	}

	public static Map<String, String> getMap(String key, Map<String, String> defaultValue) {
		Map<String, String> result = getMap(key);
		if (result == null || result.isEmpty()) {
			result = defaultValue;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static List<HierarchicalConfiguration> configurationsAt(String key) {
		return configuration.configurationsAt(key);
	}

	public static SubnodeConfiguration configurationAt(String key) {
		return configurationAt(key, false);
	}

	public static SubnodeConfiguration configurationAt(String key, boolean supportUpdates) {
		return configuration.configurationAt(key, supportUpdates);

	}

	public static boolean isChecksum() {
		return getBoolean(CHECKSUM, false);
	}

	public static ChecksumType getChecksumType() {
		return EnumHelper.valueOf(ChecksumType.class, getString(CHECKSUM_TYPE, null));
	}

	public static String getChecksumKey() {
		return getString(CHECKSUM_KEY, "");
	}

	public static boolean isSerialize() {
		return getBoolean(SERIALIZE, false);
	}

	public static SerializeType getSerializeType() {
		return EnumHelper.valueOf(SerializeType.class, getString(SERIALIZE_TYPE, null));
	}

	public static boolean isSecurity() {
		return getBoolean(SECURITY, false);
	}

	public static SecurityType getSecurityType() {
		return EnumHelper.valueOf(SecurityType.class, getString(SECURITY_TYPE, null));
	}

	public static String getSecurityKey() {
		return getString(SECURITY_KEY, "");
	}

	public static boolean isCompress() {
		return getBoolean(COMPRESS, false);
	}

	public static CompressType getCompressType() {
		return EnumHelper.valueOf(CompressType.class, getString(COMPRESS_TYPE, null));
	}
}
