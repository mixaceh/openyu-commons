package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ClassHelper;

/**
 * 使用spring注入
 */
public class ConfigHelperSpringTest extends BaseTestSupporter {

	private static ConfigHelper configHelper;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml",//
				});

		configHelper = (ConfigHelper) applicationContext
				.getBean("configHelper");
	}

	@Test
	public void getConfigLocation() {
		Resource result = null;
		result = configHelper.getConfigurationLocation();
		// URL [file:src/test/config/etc/config.xml]
		System.out.println(result);
		//
		try {
			// src/test/config/etc/config.xml
			// classpath ->
			// D:/dev/openyu/trunk/openyu-commons-core.j/src/test/config/etc/config.xml
			System.out.println("getFile(): " + result.getFile());
		} catch (Exception ex) {
			System.out.println("getFile(): null");// null
		}
		//
		System.out.println("getFilename(): " + result.getFilename());// config.xml
		//
		try {
			// src/test/config/etc/config.xml
			// classpath ->
			// file:D:/dev/openyu/trunk/openyu-commons-core.j/src/test/config/etc/config.xml
			System.out.println("getURL(): " + result.getURL());
		} catch (Exception ex) {
			System.out.println("getURL(): null");// null
		}
		//
		try {
			// src/test/config/etc/xxx.xml
			// classpath ->
			// file:D:/dev/openyu/trunk/openyu-commons-core.j/src/test/config/etc/xxx.xml
			System.out.println("getURI(): " + result.getURI());
		} catch (Exception ex) {
			System.out.println("getURI(): null");// null
		}
		//
		try {
			// java.io.BufferedInputStream@1fafb02
			System.out.println("getInputStream(): " + result.getInputStream());
		} catch (Exception ex) {
			System.out.println("getInputStream(): null");// null
		}

		// UrlResource,org.springframework.core.io.DefaultResourceLoader$ClassPathContextResource
		System.out.println(result.getClass());
		System.out.println("getConfigFile(): " + ConfigHelper.getConfigurationFile());
		System.out.println("getConfigLocation(): "
				+ configHelper.getConfigurationLocation());
		System.out.println("isDebug(): " + ConfigHelper.isDebug());
		//
		assertNotNull(result);

		// 改設定檔位置
		ConfigHelper.setConfigurationFile("src/test/config/etc/config.xml");
		System.out.println("getConfigFile(): " + ConfigHelper.getConfigurationFile());
		System.out.println("getConfigLocation(): "
				+ configHelper.getConfigurationLocation());
		System.out.println("isDebug(): " + ConfigHelper.isDebug());
		//
		ConfigHelper.setConfigurationFile("src/test/config/etc/config.xml");
	}

	@Test
	public void getXmlDir() {
		Resource result = null;
		result = configHelper.getXmlDirLocation();
		System.out.println(result);// URL [file:data/xml]
		//
		try {
			System.out.println("getFile(): " + result.getFile());// data\xml
		} catch (Exception ex) {
			System.out.println("getFile(): null");// null
		}
		//
		System.out.println("getFilename(): " + result.getFilename());// xml
		//
		try {
			System.out.println("getURL(): " + result.getURL());// file:data/xml
		} catch (Exception ex) {
			System.out.println("getURL(): null");// null
		}
		//
		try {
			System.out.println("getURI(): " + result.getURI());// file:data/xml
		} catch (Exception ex) {
			System.out.println("getURI(): null");// null
		}
		// s
		try {
			System.out.println("getInputStream(): " + result.getInputStream());// null
		} catch (Exception ex) {
			System.out.println("getInputStream(): null");// null
		}
		//
		System.out.println(result.getClass());// UrlResource
		System.out.println("getXmlDir(): " + ConfigHelper.getXmlDir());
		//
		assertNotNull(result);
	}

	@Test
	public void getXmlDirByStatic() {
		// data/xml
		System.out.println("getXmlDir(): " + ConfigHelper.getXmlDir());
	}

	@Test
	public void getter() {
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			System.out.println(ConfigHelper.getJsonDir());
			System.out.println(ConfigHelper.getKeyDir());
			System.out.println(ConfigHelper.getSerDir());
			System.out.println(ConfigHelper.getXmlDir());
			System.out.println(ConfigHelper.getExcelDir());
			//
			System.out.println(ConfigHelper.getOutputDir());
			System.out.println(ConfigHelper.getDownloadDir());
			System.out.println(ConfigHelper.getUploadDir());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 381 mills.
	// 1000000 times: 385 mills.
	// 1000000 times: 386 mills.
	public void getPropertyAndCache() {
		String result = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getString("configHelper.debug");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// no cache
	// 1000000 times: 1385 mills.
	// 1000000 times: 1377 mills.
	// 1000000 times: 1379 mills.
	//
	// cache
	// 1000000 times: 265 mills.
	// 1000000 times: 278 mills.
	// 1000000 times: 279 mills.
	//
	// cache when null 會變慢
	// 1000000 times: 860 mills.
	// 1000000 times: 861 mills.
	// 1000000 times: 861 mills.
	//
	// fix: cache when null, ok
	// 1000000 times: 282 mills.
	// 1000000 times: 279 mills.
	// 1000000 times: 294 mills.
	public void getString() {
		String result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getString("configHelper.debug", "false");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals("true", result);
		//
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getString("configHelper.xxxdebug", "false");
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals("false", result);
	}

	@Test
	// no cache
	// 1000000 times: 1792 mills.
	// 1000000 times: 2094 mills.
	// 1000000 times: 2083 mills.
	//
	// wrap
	// 1000000 times: 1469 mills.
	//
	// cache
	// 1000000 times: 290 mills.
	public void getBoolean() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getBoolean("configHelper.debug", false);
			// object = ConfigHelper.getInt("agentHelper.interval");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals(true, result);

		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getBoolean("configHelper.xxxdebug", false);
			// object = ConfigHelper.getInt("agentHelper.interval");
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals(false, result);
	}

	@Test
	// applicationContext-init.xml
	//
	// <classHelper>
	// <!-- po 應對表 -->
	// <poMapping>
	// <entry>
	// <key>org.openyu.commons.lang.ClassHelperTest$PlayerPo</key>
	// <value>org.openyu.commons.lang.ClassHelperTest$Player</value>
	// </entry>
	// <entry>
	// <key>org.openyu.commons.lang.ClassHelperTest$PlayerPoName</key>
	// <value>org.openyu.commons.lang.ClassHelperTest$PlayerName</value>
	// </entry>
	// </poMapping>
	// </classHelper>
	//
	// no cache
	// 1000000 times: 4579 mills.
	// 1000000 times: 4584 mills.
	// 1000000 times: 4658 mills.
	//
	// cache
	// 1000000 times: 3432 mills.
	// 1000000 times: 3571 mills.
	// 1000000 times: 3389 mills.
	public void configurationsAt() {
		List<HierarchicalConfiguration> result = null;
		String key = null;
		String value = null;
		Class<?> poClass = null;
		Class<?> voClass = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper
					.configurationsAt("classHelper.poMapping.entry");
			for (HierarchicalConfiguration node : result) {
				key = node.getString("key");
				poClass = ClassHelper.forName(key);

				// poClass =
				// ClassHelper.forName("org.openyu.commons.lang.ClassHelperTest$PlayerPo");

				value = node.getString("value");
				voClass = ClassHelper.forName(value);

				// voClass =
				// ClassHelper.forName("org.openyu.commons.lang.ClassHelperTest$Player");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("[" + key + "]: " + poClass);
		System.out.println("[" + value + "]: " + voClass);

	}

	@Test
	// no cache
	// 1000000 times: 2055 mills.
	// 1000000 times: 2184 mills.
	// 1000000 times: 2189 mills.
	//
	// cache
	// 1000000 times: 283 mills.
	// 1000000 times: 283 mills.
	// 1000000 times: 281 mills.
	public void configurationsAt2() {
		// HierarchicalConfiguration
		// +-HierarchicalReloadableConfiguration
		// +-SubnodeConfiguration

		// configurationsAt -> List<HierarchicalConfiguration>
		// configurationAt -> SubnodeConfiguration

		// classHelper.poMapping.entry
		List<HierarchicalConfiguration> result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// list =
			// ConfigHelper.configurationsAt("classHelper.poMapping.entry");
			result = ConfigHelper
					.configurationsAt("xxxclassHelper.poMapping.entry");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	// no cache
	// 1000000 times: 2340 mills.
	// 1000000 times: 2357 mills.
	// 1000000 times: 2342 mills.
	//
	// cache
	// 1000000 times: 283 mills.
	// 1000000 times: 280 mills.
	// 1000000 times: 274 mills.
	//
	// cache when null 會變慢
	// 1000000 times: 4020 mills.
	// 1000000 times: 4035 mills.
	// 1000000 times: 4092 mills.
	//
	// #fix: cache when null 會變慢, ok
	// 1000000 times: 268 mills.
	// 1000000 times: 282 mills.
	// 1000000 times: 278 mills.
	public void configurationAt() {
		// classHelper.poMapping.entry
		SubnodeConfiguration result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// sub =
			// ConfigHelper.configurationAt("classHelper.poMapping.entry(0)");
			try {
				result = ConfigHelper
						.configurationAt("xxxclassHelper.poMapping.entry(0)");
			} catch (Exception ex) {
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertNull(result);
	}

	@Test
	// no cache
	// 1000000 times: 1256 mills.
	// 1000000 times: 1250 mills.
	// 1000000 times: 1263 mills.
	//
	// cache
	// 1000000 times: 333 mills.
	// 1000000 times: 285 mills.
	// 1000000 times: 290 mills.
	public void getList() {
		// color
		List<Object> result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getList("color");
			// list = ConfigHelper.getList("xxxcolor");
			// list = ConfigHelper.getList("year");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	// no cache
	// 1000000 times: 1256 mills.
	// 1000000 times: 1250 mills.
	// 1000000 times: 1263 mills.
	//
	// cache
	// 1000000 times: 289 mills.
	// 1000000 times: 275 mills.
	// 1000000 times: 275 mills.
	// private static Map<String, String> poMapping =
	// ConfigHelper.getMap("classHelper.poMapping");
	public void getMap() {
		// classHelper.poMapping
		Map<String, String> result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.getMap("classHelper.poMapping");
			// map = ConfigHelper.getMap("xxxclassHelper.poMapping");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	public void isDebug() {
		// configHelper.debug
		boolean result = false;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ConfigHelper.isDebug();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

}
