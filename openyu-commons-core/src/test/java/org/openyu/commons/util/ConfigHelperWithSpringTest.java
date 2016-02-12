package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.thread.ThreadHelper;

/**
 * 使用spring注入
 */
public class ConfigHelperWithSpringTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml",//
		});

		applicationContext.getBean("configHelper");// null
	}

	@Test
	public void getter() {
		// src\test\config\etc\configuration.xml
		System.out.println(ConfigHelper.getConfigurationFile());
		//
		System.out.println(ConfigHelper.getJsonDir());
		System.out.println(ConfigHelper.getKeyDir());
		System.out.println(ConfigHelper.getSerDir());
		System.out.println(ConfigHelper.getXmlDir());
		System.out.println(ConfigHelper.getExcelDir());
		// /log4j2.xml ->
		// /D:/dev/openyu7/trunk/openyu-commons.j/openyu-commons-core.j/target/test-classes/log4j2.xml
		System.out.println(ConfigHelper.getLog4jConfigFile());
		//
		System.out.println(ConfigHelper.getInputDir());
		System.out.println(ConfigHelper.getOutputDir());
		System.out.println(ConfigHelper.getDownloadDir());
		System.out.println(ConfigHelper.getUploadDir());
		System.out.println("debug: " + ConfigHelper.isDebug());
		//
		System.out.println("checksum:" + ConfigHelper.isChecksum());
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

}
