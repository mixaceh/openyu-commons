package org.openyu.commons.util;

import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 不使用spring注入, 採取預設方式
 */
public class ConfigHelperTest extends BaseTestSupporter {

	@Test
	public void getter() {
		// src\test\config\etc\config.xml
		System.out.println(ConfigHelper.getConfigurationFile());
		// log4j.properties
		System.out.println(ConfigHelper.getLog4jConfigFile());
		//
		System.out.println(ConfigHelper.getJsonDir());
		System.out.println(ConfigHelper.getKeyDir());
		System.out.println(ConfigHelper.getSerDir());
		System.out.println(ConfigHelper.getXmlDir());
		System.out.println(ConfigHelper.getExcelDir());
		//
		System.out.println(ConfigHelper.getOutputDir());
		System.out.println(ConfigHelper.getDownloadDir());
		System.out.println(ConfigHelper.getUploadDir());
		System.out.println("debug: " + ConfigHelper.isDebug());
		//
		System.out.println(ConfigHelper.isChecksum());
		System.out.println(ConfigHelper.getChecksumType());
		System.out.println(ConfigHelper.getChecksumKey());
		//
		System.out.println(ConfigHelper.isSerialize());
		System.out.println(ConfigHelper.getSerializeType());
		//
		System.out.println(ConfigHelper.isSecurity());
		System.out.println(ConfigHelper.getSecurityType());
		System.out.println(ConfigHelper.getSecurityKey());
		//
		System.out.println(ConfigHelper.isCompress());
		System.out.println(ConfigHelper.getCompressType());

		for (int i = 0; i < 100; i++) {
			ThreadHelper.sleep(3 * 1000);
			System.out.println("debug: " + ConfigHelper.isDebug());
			System.out.println("debug: " + ConfigHelper.getBoolean("configHelper.debug"));
			//
			System.out.println("compressType: " + ConfigHelper.getCompressType());
			System.out.println("compressType: " + ConfigHelper.getString("configHelper.compressType"));
		}
	}

	@Test
	public void getterWithSpring() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-ConfigHelper.xml",//
		});
		applicationContext.getBean("configHelper");// null

		// src\test\config\etc\config.xml
		System.out.println(ConfigHelper.getConfigurationFile());
		// log4j.properties
		System.out.println(ConfigHelper.getLog4jConfigFile());
		//
		System.out.println(ConfigHelper.getJsonDir());
		System.out.println(ConfigHelper.getKeyDir());
		System.out.println(ConfigHelper.getSerDir());
		System.out.println(ConfigHelper.getXmlDir());
		System.out.println(ConfigHelper.getExcelDir());
		//
		System.out.println(ConfigHelper.getOutputDir());
		System.out.println(ConfigHelper.getDownloadDir());
		System.out.println(ConfigHelper.getUploadDir());
		System.out.println("debug: " + ConfigHelper.isDebug());
		//
		System.out.println(ConfigHelper.isChecksum());
		System.out.println(ConfigHelper.getChecksumType());
		System.out.println(ConfigHelper.getChecksumKey());
		//
		System.out.println(ConfigHelper.isSerialize());
		System.out.println(ConfigHelper.getSerializeType());
		//
		System.out.println(ConfigHelper.isSecurity());
		System.out.println(ConfigHelper.getSecurityType());
		System.out.println(ConfigHelper.getSecurityKey());
		//
		System.out.println(ConfigHelper.isCompress());
		System.out.println(ConfigHelper.getCompressType());

		for (int i = 0; i < 100; i++) {
			ThreadHelper.sleep(3 * 1000);
			System.out.println("debug: " + ConfigHelper.isDebug());
			System.out.println("debug: " + ConfigHelper.getBoolean("configHelper.debug"));
			//
			System.out.println("compressType: " + ConfigHelper.getCompressType());
			System.out.println("compressType: " + ConfigHelper.getString("configHelper.compressType"));
		}
	}

	@Test
	public void loggerWithLog4j() {
		// log4j
		org.apache.log4j.Logger LOGGER = org.apache.log4j.LogManager.getLogger(ConfigHelperTest.class);
		//
		String confFile = "src/test/resources/bak/log4j.properties";

		// 2016/02/06 01:24:57 INFO ConfigHelper:534 - Using Log4j
		// 2016/02/06 01:24:57 INFO ConfigHelperTest:115 - Config File
		// src/test/resources/bak/log4j.propreties
		ConfigHelper.setLog4jConfigFile(confFile);
		LOGGER.info("Config File " + confFile);
	}

	@Test
	public void loggerWithLog4j2() {
		// log4j2
		org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ConfigHelperTest.class);
		//
		String confFile = "src/test/resources/log4j2.xml";

		// 2016/02/06 01:26:35 INFO ConfigHelper:540 - Using Log4j2
		// 2016/02/06 01:26:35 INFO ConfigHelperTest:126 - Config File
		// src/test/resources/log4j2.xml
		ConfigHelper.setLog4jConfigFile(confFile);
		LOGGER.info("Config File " + confFile);
	}

	@Test
	public void loggerWithSlf4j() {
		// slf4j
		Logger LOGGER = LoggerFactory.getLogger(ConfigHelperTest.class);
		//
		String confFile = "src/test/resources/log4j2.xml";

		// 2016/02/06 01:27:54 INFO ConfigHelper:540 - Using Log4j2
		// 2016/02/06 01:27:54 INFO ConfigHelperTest:141 - Config File
		// src/test/resources/log4j2.xml
		ConfigHelper.setLog4jConfigFile(confFile);
		LOGGER.info("Config File " + confFile);
	}

}
