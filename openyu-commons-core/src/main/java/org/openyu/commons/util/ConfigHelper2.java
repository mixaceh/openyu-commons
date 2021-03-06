package org.openyu.commons.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.CombinedConfiguration;
//import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
//import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
//import org.apache.commons.configuration.tree.DefaultConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.FileHelper;
import org.openyu.commons.security.SecurityType;

/**
 * 1.預設設定檔: src/test/config/etc/config.xml
 * 
 * 2.直接使用static方法取值
 * 
 * 3.可利用spring重新給設定檔路徑configLocation
 */
public final class ConfigHelper2 extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper2.class);

	/**
	 * 是否由Static()建構
	 */
	protected static boolean staticBuild;
	/**
	 * 預設設定檔目錄
	 * 
	 * src/test/config
	 */
	private final static String DEFAULT_CONFIG_DIR = "src" + File.separator + "test" + File.separator + "config";

	/**
	 * 預設設定檔檔名
	 * 
	 * config.xml
	 */
	private final static String DEFAULT_CONFIG_FILE_NAME = "config.xml";

	/**
	 * 預設設定檔
	 * 
	 * src/test/config/etc/config.xml
	 */
	private final static String DEFAULT_CONFIG_FILE = DEFAULT_CONFIG_DIR + File.separator + "etc" + File.separator
			+ DEFAULT_CONFIG_FILE_NAME;

	/**
	 * 設定檔
	 */
	private static String configFile = DEFAULT_CONFIG_FILE;

	/**
	 * 設定檔資源,由spring注入
	 * 
	 * file:src/test/config/etc/config.xml
	 */
	private static Resource configLocation;

	private static DefaultConfigurationBuilder factory;

	// private static CombinedConfiguration configuration;

	// --------------------------------------------------------
	// default dir
	// --------------------------------------------------------
	/**
	 * 預設json目錄
	 * 
	 * src/test/config/json
	 */
	private final static String DEFAULT_JSON_DIR = DEFAULT_CONFIG_DIR + File.separator + "json";

	/**
	 * json目錄
	 */
	private static String jsonDir = DEFAULT_JSON_DIR;

	/**
	 * json目錄資源,由spring注入
	 */
	private static Resource jsonDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設key目錄
	 * 
	 * src/test/config/key
	 */
	private final static String DEFAULT_KEY_DIR = DEFAULT_CONFIG_DIR + File.separator + "key";

	/**
	 * key目錄
	 */
	private static String keyDir = DEFAULT_KEY_DIR;

	/**
	 * key目錄資源,由spring注入
	 */
	private static Resource keyDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設ser目錄
	 * 
	 * src/test/config/ser
	 */
	private final static String DEFAULT_SER_DIR = DEFAULT_CONFIG_DIR + File.separator + "ser";

	/**
	 * ser目錄
	 */
	private static String serDir = DEFAULT_SER_DIR;

	/**
	 * ser目錄資源,由spring注入
	 */
	private static Resource serDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設xml目錄
	 * 
	 * src/test/config/xml
	 */
	private final static String DEFAULT_XML_DIR = DEFAULT_CONFIG_DIR + File.separator + "xml";

	/**
	 * xml目錄
	 */
	private static String xmlDir = DEFAULT_XML_DIR;

	/**
	 * xml目錄資源,由spring注入
	 */
	private static Resource xmlDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設excel目錄
	 * 
	 * src/test/config/excel
	 */
	private final static String DEFAULT_EXCEL_DIR = DEFAULT_CONFIG_DIR + File.separator + "excel";

	/**
	 * excel目錄
	 */
	private static String excelDir = DEFAULT_EXCEL_DIR;

	/**
	 * excel目錄資源,由spring注入
	 */
	private static Resource excelDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設log目錄
	 * 
	 * src/test/config/log
	 */
	private final static String DEFAULT_LOG_DIR = DEFAULT_CONFIG_DIR + File.separator + "log";

	/**
	 * log目錄
	 */
	private static String logDir = DEFAULT_LOG_DIR;

	/**
	 * log目錄資源,由spring注入
	 */
	private static Resource logDirLocation;

	// --------------------------------------------------------
	// custom
	// --------------------------------------------------------

	/**
	 * 預設客製目錄
	 */
	private static String DEFAULT_CUSTOM_DIR = "custom";

	/**
	 * 預設output目錄
	 * 
	 * custom/output
	 */
	private final static String DEFAULT_OUTPUT_DIR = DEFAULT_CUSTOM_DIR + File.separator + "output";

	/**
	 * output目錄
	 */
	private static String outputDir = DEFAULT_OUTPUT_DIR;

	/**
	 * output目錄資源,由spring注入
	 */
	private static Resource outputDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設download目錄
	 */
	private final static String DEFAULT_DOWNLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "download";

	/**
	 * download目錄
	 */
	private static String downloadDir = DEFAULT_DOWNLOAD_DIR;

	/**
	 * download目錄資源,由spring注入
	 */
	private static Resource downloadDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設upload目錄
	 */
	private final static String DEFAULT_UPLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "upload";

	/**
	 * upload目錄
	 */
	private static String uploadDir = DEFAULT_UPLOAD_DIR;

	/**
	 * upload目錄資源,由spring注入
	 */
	private static Resource uploadDirLocation;

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

	// private static SecurityType securityType;

	/** 安全性key */
	public final static String SECURITY_KEY = "configHelper.securityKey";

	// private static String securityKey;

	/** 是否壓縮 */
	// private static boolean compress;

	public final static String COMPRESS = "configHelper.compress";

	/** 壓縮類別 */
	public final static String COMPRESS_TYPE = "configHelper.compressType";

	// private static CompressType compressType;

	// --------------------------------------------------------
	// config-debug.xml
	// --------------------------------------------------------
	//
	/**
	 * 是否除錯
	 */
	public final static String DEBUG = "configHelper.debug";

	// private static boolean debug;

	// // <String, Object>
	// private static MapCache<String, Object> getPropertyCache = new
	// MapCacheImpl<String, Object>();
	//
	// // <String, List<HierarchicalConfiguration>>
	// private static ConcurrentMap<String, List<HierarchicalConfiguration>>
	// configurationsAtCache = new ConcurrentHashMap<String,
	// List<HierarchicalConfiguration>>();
	//
	// private static ReentrantLock configurationsAtCacheLock = new
	// ReentrantLock();
	//
	// // <String, SubnodeConfiguration>
	// private static ConcurrentMap<String, SubnodeConfiguration>
	// configurationAtCache = new ConcurrentHashMap<String,
	// SubnodeConfiguration>();
	//
	// private static ReentrantLock configurationAtCacheLock = new
	// ReentrantLock();
	//
	// public static final NullSubnodeConfiguration EMPTY_SUBNODE_CONFIGURATION
	// = new NullSubnodeConfiguration();
	//
	// // <String, List<String>>
	// private static ConcurrentMap<String, List<String>> getListCache = new
	// ConcurrentHashMap<String, List<String>>();
	//
	// private static ReentrantLock getListCacheLock = new ReentrantLock();
	//
	// private static ConcurrentMap<String, Map<String, String>> getMapCache =
	// new ConcurrentHashMap<String, Map<String, String>>();
	//
	// private static ReentrantLock getMapCacheLock = new ReentrantLock();

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			staticBuild = true;
			buildWithFile(null);
		}
	}

	public ConfigHelper2() {
		throw new HelperException(
				new StringBuilder().append(ConfigHelper2.class.getName()).append(" can not construct").toString());
	}

	protected void closeInternal() throws Exception {
		factory = null;
	}

	public static String getConfigFile() {
		return configFile;
	}

	public static void setConfigFile(String configFile) {
		ConfigHelper2.configFile = configFile;
		ConfigHelper2.configLocation = null;
		//
		staticBuild = false;
		buildWithFile(configFile);
	}

	/**
	 * spring注入
	 * 
	 * @return
	 */
	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		ConfigHelper2.configLocation = configLocation;
		ConfigHelper2.configFile = getFile(configLocation);
		//
		staticBuild = false;
		buildWithFile(null);
	}

	/**
	 * 取得檔名含路徑
	 * 
	 * @param resource
	 * @return
	 */
	public static String getFile(Resource resource) {
		String result = null;
		try {
			if (resource != null) {
				result = resource.getFile().getPath();
			}
		} catch (Exception ex) {
		}
		return result;
	}

	// --------------------------------------------------------
	public static String getJsonDir() {
		return jsonDir;
	}

	public static void setJsonDir(String jsonDir) {
		ConfigHelper2.jsonDir = jsonDir;
		ConfigHelper2.jsonDirLocation = null;
		//
		buildDir(DEFAULT_JSON_DIR, jsonDirLocation, jsonDir);
	}

	public Resource getJsonDirLocation() {
		return jsonDirLocation;
	}

	public void setJsonDirLocation(Resource jsonDirLocation) {
		ConfigHelper2.jsonDirLocation = jsonDirLocation;
		ConfigHelper2.jsonDir = getFile(jsonDirLocation);
		//
		buildDir(DEFAULT_JSON_DIR, jsonDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getKeyDir() {
		return keyDir;
	}

	public static void setKeyDir(String keyDir) {
		ConfigHelper2.keyDir = keyDir;
		ConfigHelper2.keyDirLocation = null;
		//
		buildDir(DEFAULT_KEY_DIR, keyDirLocation, keyDir);
	}

	public Resource getKeyDirLocation() {
		return keyDirLocation;
	}

	public void setKeyDirLocation(Resource keyDirLocation) {
		ConfigHelper2.keyDirLocation = keyDirLocation;
		ConfigHelper2.keyDir = getFile(keyDirLocation);
		//
		buildDir(DEFAULT_KEY_DIR, keyDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getSerDir() {
		return serDir;
	}

	public static void setSerDir(String serDir) {
		ConfigHelper2.serDir = serDir;
		ConfigHelper2.serDirLocation = null;
		//
		buildDir(DEFAULT_SER_DIR, serDirLocation, serDir);
	}

	public Resource getSerDirLocation() {
		return serDirLocation;
	}

	public void setSerDirLocation(Resource serDirLocation) {
		ConfigHelper2.serDirLocation = serDirLocation;
		ConfigHelper2.serDir = getFile(serDirLocation);
		//
		buildDir(DEFAULT_SER_DIR, serDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getXmlDir() {
		return xmlDir;
	}

	public static void setXmlDir(String xmlDir) {
		ConfigHelper2.xmlDir = xmlDir;
		ConfigHelper2.xmlDirLocation = null;
		//
		buildDir(DEFAULT_XML_DIR, xmlDirLocation, xmlDir);
	}

	public Resource getXmlDirLocation() {
		return xmlDirLocation;
	}

	public void setXmlDirLocation(Resource xmlDirLocation) {
		ConfigHelper2.xmlDirLocation = xmlDirLocation;
		ConfigHelper2.xmlDir = getFile(xmlDirLocation);
		//
		buildDir(DEFAULT_XML_DIR, xmlDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getExcelDir() {
		return excelDir;
	}

	public static void setExcelDir(String excelDir) {
		ConfigHelper2.excelDir = excelDir;
		ConfigHelper2.excelDirLocation = null;
		//
		buildDir(DEFAULT_EXCEL_DIR, excelDirLocation, excelDir);
	}

	public Resource getExcelDirLocation() {
		return excelDirLocation;
	}

	public void setExcelDirLocation(Resource excelDirLocation) {
		ConfigHelper2.excelDirLocation = excelDirLocation;
		ConfigHelper2.excelDir = getFile(excelDirLocation);
		//
		buildDir(DEFAULT_EXCEL_DIR, excelDirLocation, null);
	}

	// --------------------------------------------------------

	public static String getLogDir() {
		return logDir;
	}

	public static void setLogDir(String logDir) {
		ConfigHelper2.logDir = logDir;
		ConfigHelper2.logDirLocation = null;
		//
		buildDir(DEFAULT_LOG_DIR, logDirLocation, logDir);
	}

	public static Resource getLogDirLocation() {
		return logDirLocation;
	}

	public static void setLogDirLocation(Resource logDirLocation) {
		ConfigHelper2.logDirLocation = logDirLocation;
		ConfigHelper2.logDir = getFile(logDirLocation);
		//
		buildDir(DEFAULT_LOG_DIR, logDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getOutputDir() {
		return outputDir;
	}

	public static void setOutputDir(String outputDir) {
		ConfigHelper2.outputDir = outputDir;
		ConfigHelper2.outputDirLocation = null;
		//
		buildDir(DEFAULT_OUTPUT_DIR, outputDirLocation, outputDir);
	}

	public Resource getOutputDirLocation() {
		return outputDirLocation;
	}

	public void setOutputDirLocation(Resource outputDirLocation) {
		ConfigHelper2.outputDirLocation = outputDirLocation;
		ConfigHelper2.outputDir = getFile(outputDirLocation);
		//
		buildDir(DEFAULT_OUTPUT_DIR, outputDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getDownloadDir() {
		return downloadDir;
	}

	public static void setDownloadDir(String downloadDir) {
		ConfigHelper2.downloadDir = downloadDir;
		ConfigHelper2.downloadDirLocation = null;
		//
		buildDir(DEFAULT_DOWNLOAD_DIR, downloadDirLocation, downloadDir);
	}

	public Resource getDownloadDirLocation() {
		return downloadDirLocation;
	}

	public void setDownloadDirLocation(Resource downloadDirLocation) {
		ConfigHelper2.downloadDirLocation = downloadDirLocation;
		ConfigHelper2.downloadDir = getFile(downloadDirLocation);
		//
		buildDir(DEFAULT_DOWNLOAD_DIR, downloadDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getUploadDir() {
		return uploadDir;
	}

	public static void setUploadDir(String uploadDir) {
		ConfigHelper2.uploadDir = uploadDir;
		ConfigHelper2.uploadDirLocation = null;
		//
		buildDir(DEFAULT_UPLOAD_DIR, uploadDirLocation, uploadDir);
	}

	public Resource getUploadDirLocation() {
		return uploadDirLocation;
	}

	public void setUploadDirLocation(Resource uploadDirLocation) {
		ConfigHelper2.uploadDirLocation = uploadDirLocation;
		ConfigHelper2.uploadDir = getFile(uploadDirLocation);
		//
		buildDir(DEFAULT_UPLOAD_DIR, uploadDirLocation, null);
	}

	// --------------------------------------------------------

	/**
	 * 建構
	 * 
	 * @param firstBuild
	 *            是否第一次建構
	 * @param assignFile
	 *            指定檔案
	 */
	protected static void buildWithFile(String assignFile) {
		try {
			buildFactory(assignFile);
			if (factory == null) {
				if (staticBuild) {
					return;
				} else {
					LOGGER.error("Can not find configLocation: " + (assignFile != null ? assignFile : configLocation));
					return;
				}
			}
			//
			// configuration = factory.getConfiguration(true);

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
	 * 建構factory
	 * 
	 * @param assignFile
	 * @return
	 */
	protected static void buildFactory(String assignFile) {
		//
		try {
			// 當沒使用spring注入時,或指定設定檔
			if (configLocation == null || assignFile != null) {
				String fileName = (assignFile != null ? assignFile : DEFAULT_CONFIG_FILE);
				File file = new File(fileName);
				if (file.exists()) {
					factory = new DefaultConfigurationBuilder(file);
					// factory.setReloadingStrategy(new
					// FileChangedReloadingStrategy());
					if (staticBuild) {
						LOGGER.info("Initialization of file [" + fileName + "]");
					} else {
						LOGGER.info("Reinitialization of file [" + fileName + "]");
					}
				} else {
					// 當在web下,會出現src\test\config\data\conf\config.xml File does
					// not exist, 預設的檔案找不到, 這是正常的
					//
					// 1.若在spring下, 可在applicationContext-ini.xml可重新設定
					//
					// 2.若在junit下, 設定檔的目錄會放不同, 所以測試時會發生找不到檔案的狀況,因此單元測試中
					// 更改以下設定的目錄
					//
					// ConfigHelper
					// .setConfigFile("src/main/webapp/WEB-INF/data/conf/config.xml");
					LOGGER.warn("[" + fileName + "] File does not exist");
				}
			}
			// 使用spring注入時
			else {
				// file:src/test/config/data/conf/config.xml
				// src/test/config/data/conf/config.xml
				URL url = configLocation.getURL();
				LOGGER.info("Reinitialization with Spring [" + (url != null ? url.getFile() : null) + "]");

				if (url != null) {
					factory = new DefaultConfigurationBuilder(url);
					// factory.setReloadingStrategy(new
					// FileChangedReloadingStrategy());
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
	protected static void buildDir(String defaultDir, Resource resource, String assignDir) {
		try {
			// 當沒使用spring注入時,或指定目錄
			if (resource == null || assignDir != null) {
				File dir = new File(assignDir != null ? assignDir : defaultDir);
				FileHelper.md(dir);
			}
			// 使用spring注入時
			else {
				// web
				// /WEB-INF/xml
				// /custom/output
				if (resource instanceof ServletContextResource) {
					ServletContextResource recource = (ServletContextResource) resource;
					// 1./cms/WEB-INF/xml
					// 2./cms/custom/output
					FileHelper.md(recource.getFile().getAbsolutePath());
				}
				// file:xml
				// xml
				// custom/output
				else {
					URL url = resource.getURL();
					if (url != null) {
						FileHelper.md(url.getFile());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isDebug() {
		// return debug;
		return getBoolean(DEBUG, false);
	}

	// public static void setDebug(boolean debug) {
	// ConfigHelper.debug = debug;
	// }

	// protected void clearCache() {
	// getPropertyCache.clear();
	// configurationsAtCache.clear();
	// configurationAtCache.clear();
	// getListCache.clear();
	// getMapCache.clear();
	// }

	// public static CombinedConfiguration getConfiguration() {
	// return configuration;
	// }

	public static boolean isEmpty() {
		try {
			return factory.getConfiguration().isEmpty();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean containsKey(String key) {
		try {
			return factory.getConfiguration().containsKey(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void addProperty(String key, Object value) {
		try {
			factory.getConfiguration().addProperty(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void setProperty(String key, Object value) {
		try {
			factory.getConfiguration().setProperty(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void clearProperty(String key) {
		try {
			factory.getConfiguration().clearProperty(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Object getProperty(String key) {
		// return configuration.getProperty(key);
		// return getPropertyAndCache(key);
		try {
			return factory.getConfiguration().getProperty(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// #fix: 當找不到時, 會太慢, ok
	// @SuppressWarnings("unchecked")
	// public static <T> T getPropertyAndCache(String key) {
	// T result = null;
	// try {
	// getPropertyCache.lockInterruptibly();
	// try {
	// if (getPropertyCache.isNotNullValue(key)) {
	// result = (T) getPropertyCache.get(key);
	// if (result == null) {
	// try {
	// result = (T) configuration.getProperty(key);
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// getPropertyCache.put(key, result);
	// }
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// getPropertyCache.unlock();
	// }
	// } catch (InterruptedException ex) {
	// ex.printStackTrace();
	// }
	// return result;
	// }

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(String prefix) {
		// return configuration.getKeys(prefix);
		try {
			return factory.getConfiguration().getKeys(prefix);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys() {
		// return configuration.getKeys();
		try {
			return factory.getConfiguration().getKeys();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Properties getProperties(String key) {
		// return configuration.getProperties(key);
		try {
			return factory.getConfiguration().getProperties(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean getBoolean(String key) {
		try {
			return factory.getConfiguration().getBoolean(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		try {
			return factory.getConfiguration().getBoolean(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static byte getByte(String key) {
		// return configuration.getByte(key);
		try {
			return factory.getConfiguration().getByte(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static byte getByte(String key, byte defaultValue) {
		// return configuration.getByte(key, defaultValue);
		try {
			return factory.getConfiguration().getByte(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static short getShort(String key) {
		// return configuration.getShort(key);
		try {
			return factory.getConfiguration().getShort(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static short getShort(String key, short defaultValue) {
		// return configuration.getShort(key, defaultValue);
		try {
			return factory.getConfiguration().getShort(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static int getInt(String key) {
		// return configuration.getInt(key);
		try {
			return factory.getConfiguration().getInt(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static int getInt(String key, int defaultValue) {
		// return configuration.getInt(key, defaultValue);
		try {
			return factory.getConfiguration().getInt(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static long getLong(String key) {
		// return configuration.getLong(key);
		try {
			return factory.getConfiguration().getLong(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0L;
	}

	public static long getLong(String key, long defaultValue) {
		// return configuration.getLong(key, defaultValue);
		try {
			return factory.getConfiguration().getLong(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0L;
	}

	public static float getFloat(String key) {
		// return configuration.getFloat(key);
		try {
			return factory.getConfiguration().getFloat(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0f;
	}

	public static float getFloat(String key, float defaultValue) {
		// return configuration.getFloat(key, defaultValue);
		try {
			return factory.getConfiguration().getFloat(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0f;
	}

	public static double getDouble(String key) {
		// return configuration.getDouble(key);
		try {
			return factory.getConfiguration().getDouble(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0d;
	}

	public static double getDouble(String key, double defaultValue) {
		// return configuration.getDouble(key, defaultValue);
		try {
			return factory.getConfiguration().getDouble(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0d;
	}

	public static BigDecimal getBigDecimal(String key) {
		// return configuration.getBigDecimal(key);
		try {
			return factory.getConfiguration().getBigDecimal(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		// return configuration.getBigDecimal(key, defaultValue);
		try {
			return factory.getConfiguration().getBigDecimal(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static BigInteger getBigInteger(String key) {
		// return configuration.getBigInteger(key);
		try {
			return factory.getConfiguration().getBigInteger(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
		// return configuration.getBigInteger(key, defaultValue);
		try {
			return factory.getConfiguration().getBigInteger(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getString(String key) {
		// return (String) getPropertyAndCache(key);
		// return configuration.getString(key);
		try {
			return factory.getConfiguration().getString(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getString(String key, String defaultValue) {
		// String ret = getString(key);
		// if (ret == null) {
		// ret = defaultValue;
		// }
		// return ret;
		// return configuration.getString(key, defaultValue);
		try {
			return factory.getConfiguration().getString(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String[] getStringArray(String key) {
		// return configuration.getStringArray(key);
		try {
			return factory.getConfiguration().getStringArray(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List<Object> getList(String key) {
		// return configuration.getList(key);
		// return getListAndCache(key);
		try {
			return factory.getConfiguration().getList(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List<Object> getList(String key, List<String> defaultValue) {
		// List<String> result = getList(key);
		// if (result == null || result.isEmpty()) {
		// result = defaultValue;
		// }
		// return result;
		// return configuration.getList(key, defaultValue);

		try {
			return factory.getConfiguration().getList(key, defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// // 當不存在時,傳回空集合
	// @SuppressWarnings("unchecked")
	// public static List<String> getListAndCache(String key) {
	// List<String> result = null;
	// try {
	// getListCacheLock.lock();
	// result = getListCache.get(key);
	// if (result == null) {
	// try {
	// result = configuration.getList(key);
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// // 找不到時,會放入一個empty object,之後會用此來判斷
	// if (result != null) {
	// getListCache.put(key, result);
	// } else {
	// result = new LinkedList<String>();
	// getListCache.put(key, CollectionHelper.EMPTY_LIST);
	// }
	//
	// }
	// // by address
	// else if (result == CollectionHelper.EMPTY_LIST) {
	// result = new LinkedList<String>();
	// }
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// } finally {
	// getListCacheLock.unlock();
	// }
	// return result;
	// }

	public static Map<String, String> getMap(String key) {
		// return getMapAndCache(key);
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

	// 當不存在時,傳回空集合
	// @SuppressWarnings("unchecked")
	// public static Map<String, String> getMapAndCache(String key) {
	// Map<String, String> result = null;
	// try {
	// getMapCacheLock.lock();
	// result = getMapCache.get(key);
	// if (result == null) {
	// try {
	// StringBuilder sb = new StringBuilder();
	// sb.append(key);
	// sb.append(".entry");
	// // System.out.println(sb);
	// List<HierarchicalConfiguration> list = configurationsAt(sb
	// .toString());
	// // System.out.println(list.size());
	// if (!list.isEmpty()) {
	// result = new LinkedHashMap<String, String>();
	// for (HierarchicalConfiguration node : list) {
	// String nodeKey = node.getString("key");
	// String nodeValue = node.getString("value");
	// // System.out.println(nodeKey+" "+nodeValue);
	// result.put(nodeKey, nodeValue);
	// }
	// }
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// // 找不到時,會放入一個empty object,之後會用此來判斷
	// if (result != null) {
	// getMapCache.put(key, result);
	// } else {
	// result = new LinkedHashMap<String, String>();
	// getMapCache.put(key, CollectionHelper.EMPTY_MAP);
	// }
	// }
	// // by address
	// else if (result == CollectionHelper.EMPTY_MAP) {
	// result = new LinkedHashMap<String, String>();
	// }
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// } finally {
	// getMapCacheLock.unlock();
	// }
	// return result;
	// }

	@SuppressWarnings("unchecked")
	public static List<HierarchicalConfiguration> configurationsAt(String key) {
		// return configuration.configurationsAt(key);
		// return configurationsAtAndCache(key);

		try {
			return ((CombinedConfiguration) factory.getConfiguration()).configurationsAt(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// // 當不存在時,傳回空集合
	// public static List<HierarchicalConfiguration> configurationsAtAndCache(
	// String key) {
	// List<HierarchicalConfiguration> result = null;
	// try {
	// configurationsAtCacheLock.lock();
	// result = configurationsAtCache.get(key);
	// if (result == null) {
	// // 找不到時,會傳回一個empty list
	// try {
	// result = configuration.configurationsAt(key);
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// if (result != null) {
	// configurationsAtCache.put(key, result);
	// } else {
	// result = new LinkedList<HierarchicalConfiguration>();
	// configurationsAtCache.put(key, CollectionHelper.EMPTY_LIST);
	// }
	// } else if (result == CollectionHelper.EMPTY_LIST) {
	// result = new LinkedList<HierarchicalConfiguration>();
	// }
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// } finally {
	// configurationsAtCacheLock.unlock();
	// }
	// return result;
	// }

	public static SubnodeConfiguration configurationAt(String key) {
		return configurationAt(key, false);
	}

	// 先從cache取,若無,再取設定檔
	public static SubnodeConfiguration configurationAt(String key, boolean supportUpdates) {
		// return configuration.configurationAt(key, supportUpdates);
		// return configurationAtAndCache(key);

		try {
			return ((CombinedConfiguration) factory.getConfiguration()).configurationAt(key, supportUpdates);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	// // #fix: 當找不到時, 會太慢, ok
	// public static SubnodeConfiguration configurationAtAndCache(String key) {
	// SubnodeConfiguration result = null;
	// try {
	// configurationAtCacheLock.lock();
	// result = configurationAtCache.get(key);
	// if (result == null) {
	// try {
	// // 當找不到時會有ex
	// result = configuration.configurationAt(key);
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// // 找不到時,會放入一個empty object,之後會用此來判斷null
	// configurationAtCache.put(key, (result != null ? result
	// : EMPTY_SUBNODE_CONFIGURATION));
	// }
	// // by address
	// else if (result == EMPTY_SUBNODE_CONFIGURATION) {
	// result = null;
	// }
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// } finally {
	// configurationAtCacheLock.unlock();
	// }
	// return result;
	// }

	// public static class NullSubnodeConfiguration extends SubnodeConfiguration
	// {
	//
	// private static final long serialVersionUID = -2113995032437300282L;
	//
	// public NullSubnodeConfiguration() {
	// super(new HierarchicalConfiguration(),
	// new DefaultConfigurationNode());
	// }
	// }

	public static boolean isChecksum() {
		return getBoolean(CHECKSUM, false);
	}

	// public static void setChecksum(boolean checksum) {
	// ConfigHelper.checksum = checksum;
	// }

	public static ChecksumType getChecksumType() {
		return EnumHelper.valueOf(ChecksumType.class, getString(CHECKSUM_TYPE, null));
	}

	// public static void setChecksumType(ChecksumType checksumType) {
	// ConfigHelper.checksumType = checksumType;
	// }

	public static String getChecksumKey() {
		return getString(CHECKSUM_KEY, "");
	}

	// public static void setChecksumKey(String checksumKey) {
	// ConfigHelper.checksumKey = checksumKey;
	// }

	public static boolean isSerialize() {
		return getBoolean(SERIALIZE, false);
	}

	// public static void setSerialize(boolean serialize) {
	// ConfigHelper.serialize = serialize;
	// }

	public static SerializeType getSerializeType() {
		return EnumHelper.valueOf(SerializeType.class, getString(SERIALIZE_TYPE, null));
	}

	// public static void setSerializeType(SerializeType serializeType) {
	// ConfigHelper.serializeType = serializeType;
	// }

	public static boolean isSecurity() {
		return getBoolean(SECURITY, false);
	}

	// public static void setSecurity(boolean security) {
	// ConfigHelper.security = security;
	// }

	public static SecurityType getSecurityType() {
		return EnumHelper.valueOf(SecurityType.class, getString(SECURITY_TYPE, null));
	}

	// public static void setSecurityType(SecurityType securityType) {
	// ConfigHelper.securityType = securityType;
	// }

	public static String getSecurityKey() {
		return getString(SECURITY_KEY, "");
	}

	// public static void setSecurityKey(String securityKey) {
	// ConfigHelper.securityKey = securityKey;
	// }

	public static boolean isCompress() {
		return getBoolean(COMPRESS, false);
	}

	// public static void setCompress(boolean compress) {
	// ConfigHelper.compress = compress;
	// }

	public static CompressType getCompressType() {
		return EnumHelper.valueOf(CompressType.class, getString(COMPRESS_TYPE, null));
	}

	// public static void setCompressType(CompressType compressType) {
	// ConfigHelper.compressType = compressType;
	// }

}
