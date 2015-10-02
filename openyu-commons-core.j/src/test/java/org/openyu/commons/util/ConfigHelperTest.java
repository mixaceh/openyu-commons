package org.openyu.commons.util;

import org.junit.Test;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 不使用spring注入, 採取預設方式
 */
public class ConfigHelperTest {

	@Test
	public void getter() {
		// src\test\config\etc\config.xml
		System.out.println(ConfigHelper.getConfigFile());
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
			System.out.println("debug: "
					+ ConfigHelper.getBoolean("configHelper.debug"));
			//
			System.out.println("compressType: "
					+ ConfigHelper.getCompressType());
			System.out.println("compressType: "
					+ ConfigHelper.getString("configHelper.compressType"));
		}
	}

	@Test
	public void getterWithSpring() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml",//
				});
		ConfigHelper bean = (ConfigHelper) applicationContext
				.getBean("configHelper");
		System.out.println(bean);

		// src\test\config\etc\config.xml
		System.out.println(bean.getConfigFile());
		// log4j.properties
		System.out.println(bean.getLog4jConfigFile());
		//
		System.out.println(bean.getJsonDir());
		System.out.println(bean.getKeyDir());
		System.out.println(bean.getSerDir());
		System.out.println(bean.getXmlDir());
		System.out.println(bean.getExcelDir());
		//
		System.out.println(bean.getOutputDir());
		System.out.println(bean.getDownloadDir());
		System.out.println(bean.getUploadDir());
		System.out.println("debug: " + bean.isDebug());
		//
		System.out.println(bean.isChecksum());
		System.out.println(bean.getChecksumType());
		System.out.println(bean.getChecksumKey());
		//
		System.out.println(bean.isSerialize());
		System.out.println(bean.getSerializeType());
		//
		System.out.println(bean.isSecurity());
		System.out.println(bean.getSecurityType());
		System.out.println(bean.getSecurityKey());
		//
		System.out.println(bean.isCompress());
		System.out.println(bean.getCompressType());

		for (int i = 0; i < 100; i++) {
			ThreadHelper.sleep(3 * 1000);
			System.out.println("debug: " + bean.isDebug());
			System.out.println("debug: "
					+ bean.getBoolean("configHelper.debug"));
			//
			System.out.println("compressType: " + bean.getCompressType());
			System.out.println("compressType: "
					+ bean.getString("configHelper.compressType"));
		}
	}

}
