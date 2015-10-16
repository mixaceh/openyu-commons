package org.openyu.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.io.FileHelper.DecryptDirResult;
import org.openyu.commons.io.FileHelper.DecryptFileResult;
import org.openyu.commons.io.FileHelper.EncryptDirResult;
import org.openyu.commons.io.FileHelper.EncryptFileResult;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.util.ConfigHelper;

public class FileHelperTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isExist() {
		boolean result = false;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.isExist("src");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result);
		//
		result = FileHelper.isExist("scr123");
		System.out.println(result);
		assertFalse(result);
		//
		result = FileHelper.isExist(new File("data/conf/config.xml"));
		System.out.println(result);
		assertTrue(result);
		//
		result = FileHelper.isExist((String) null);
		System.out.println(result);
		assertFalse(result);
	}

	@Test
	public void isNotExist() {
		boolean result = false;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.isNotExist("src");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertFalse(result);
		//
		result = FileHelper.isNotExist("scr123");
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	public void toUrl() {
		URL result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// file:/D:/dev/openyu/trunk/openyu-commons-core.j/src/
			result = FileHelper.toUrl("src");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("/D:/dev/openyu7/trunk/openyu-commons.j/openyu-commons-core.j/src/",
				result.getFile());
		//
		result = FileHelper.toUrl("scr123");
		System.out.println(result);
		assertNull(result);
		//
		result = FileHelper.toUrl(new File("src/test/config/etc/config.xml"));
		System.out.println(result);
		assertEquals(
				"/D:/dev/openyu7/trunk/openyu-commons.j/openyu-commons-core.j/src/test/config/etc/config.xml",
				result.getFile());
		//
		result = FileHelper.toUrl((String) null);
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void toFile() throws Exception {
		File result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// file:/D:/dev/openyu/trunk/openyu-commons-core.j/src/
			result = FileHelper
					.toFile("file:/D:/dev/openyu/trunk/openyu-commons-core.j/src");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		System.out.println("getName: " + result.getName());// src
		System.out.println("getPath: " + result.getPath());// D:\dev\openyu\trunk\openyu-commons-core.j\src
		System.out.println("getParent: " + result.getParent());// D:\dev\openyu\trunk\openyu-commons-core.j
		//
		System.out.println("getAbsolutePath: " + result.getAbsolutePath());// D:\dev\openyu\trunk\openyu-commons-core.j\src
		System.out.println("getCanonicalPath: " + result.getCanonicalPath());// D:\dev\openyu\trunk\openyu-commons-core.j\src

		assertEquals("D:\\dev\\openyu\\trunk\\openyu-commons-core.j\\src",
				result.toString());
		//
		result = FileHelper
				.toFile("file:/D:/dev/openyu/trunk/openyu-commons-core.j/src123");
		System.out.println(result);
		assertNotNull(result);
		//
		result = FileHelper.toFile((URL) null);
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void getDirByClass() {
		String result = null;
		// eclipse: D:\dev\openyu\trunk\openyu-commons-core.j
		// tomcat: tomcat\bin
		System.out.println("user.dir: " + System.getProperty("user.dir"));

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// D:\dev\openyu\trunk\openyu-commons-core.j\src\org\openyu\commons\io
			result = FileHelper.getDirByClass("src", FileHelper.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(
				"D:\\dev\\openyu\\trunk\\openyu-commons-core.j\\src\\org\\openyu\\commons\\io",
				result);
	}

	@Test
	public void getExtension() {
		String result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.getExtension("log4j.properties");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// properties
		System.out.println(result);
		assertEquals("properties", result);
	}

	@Test
	public void getResource() {
		String value = "/log4j.properties";
		URL result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.getResource(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		// file:/D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties
		System.out.println(result);
		assertEquals(
				"/D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties",
				result.getFile());

		// 少了個 "/"
		value = "log4j.properties";
		result = FileHelper.getResource(value);
		// null
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void getResourceFile() {
		String value = "/log4j.properties";
		String result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.getResourceFile(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		// /D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties
		System.out.println(result);
		assertEquals(
				"/D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties",
				result);

		// 少了個 "/"
		value = "log4j.properties";
		result = FileHelper.getResourceFile(value);
		// null
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void getResourceStream() {
		String value = "/log4j.properties";
		InputStream result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.getResourceStream(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		// java.io.BufferedInputStream@1103d94
		System.out.println(result);
		assertNotNull(result);

		// 少了個 "/"
		value = "log4j.properties";
		result = FileHelper.getResourceStream(value);
		// null
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void getProperties() {
		String value = "/log4j.properties";
		Properties result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.getProperties(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		// {log4j.appender.stdout=org.apache.log4j.ConsoleAppender,...}
		System.out.println(result);
		assertNotNull(result);

		// 少了個 "/"
		value = "log4j.properties";
		result = FileHelper.getProperties(value);
		// null
		System.out.println(result);
		assertNull(result);
	}

	@Test
	public void md() {
		boolean result = false;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.md("data/xml");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		//
		result = FileHelper.md("custom/output/x/y/z/aaa.log", true);
		System.out.println(result);
		//
		result = FileHelper.md("custom/output/a/b/c", false);
		System.out.println(result);
		//
		result = FileHelper.md("custom/output/aaa/test.log", true);
		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void dir() {
		File[] result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.dir("custom/output");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// custom\output\a...custom\output\aaa\test.log

		SystemHelper.println(result);
		assertNotNull(result);

		// custom\output\aaa\test.log
		result = FileHelper.dir("custom/output/aaa");
		SystemHelper.println(result);
		assertNotNull(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void encryptDir() {
		String value = "custom/output";
		String assignKey = "abcdefgh";
		String algorithm = "DES"; // DESede
		//
		EncryptDirResult result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.encryptDir(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void decryptDir() {
		String value = "59f34e71138c393d\\d625568940797122";
		String assignKey = "abcdefgh";
		String algorithm = "DES";// DESede
		//
		DecryptDirResult result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.decryptDir(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void encryptFile() {
		String value = "custom/output/aaa/test.log";
		String assignKey = "abcdefgh";
		String algorithm = "DES";// DESede
		//
		EncryptFileResult result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.encryptFile(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void decryptFile() {
		String value = "59f34e71138c393d\\d625568940797122\\402284862fdf8a86\\11a598c6462fcda7241bc91e5e55b3d6";
		String assignKey = "abcdefgh";
		String algorithm = "DES";
		DecryptFileResult result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.decryptFile(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	// 1 times: 140 mills.
	public void zipDir() {
		String value = "custom/output";
		String zipName = "output.zip";
		//
		File result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.zipDir(value, zipName);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getAbsolutePath());
	}

	@Test
	// 1 times: 130 mills.
	public void unzip() {
		String value = "output.zip";
		String destDir = "output-unzip";
		String result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.unzip(value, destDir);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void encryptPng() {
		String value = "png";
		String algorithm = "DESede";
		String assignKey = "abcdefgh01234567abcdefgh";
		//
		EncryptDirResult result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.encryptDir(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getBaseDir());
	}

	@Test
	// 1 times: 7719 mills.
	public void zipPng() {
		String value = "feb30414e47d3cde";
		String zipName = "test.png";
		//
		File result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.zipDir(value, zipName);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getAbsolutePath());
	}

	@Test
	public void $encryptZipPng() {
		// encrypt
		String value = "png";
		String algorithm = "DESede";
		String assignKey = "abcdefgh01234567abcdefgh";
		//
		EncryptDirResult encryptDirResult = null;

		encryptDirResult = FileHelper.encryptDir(value, assignKey, algorithm);
		System.out.println(encryptDirResult.getBaseDir());

		// zip
		value = "feb30414e47d3cde";
		String zipName = "test.png";
		//
		File result = null;
		//
		result = FileHelper.zipDir(value, zipName);

		System.out.println(result.getAbsolutePath());
	}

	@Test
	// 1 times: 7719 mills.
	public void unzipPng() {
		String value = "test.png";
		// String destDir = "png-unzip";
		String destDir = null;
		//
		String result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.unzip(value, destDir);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 10 times: 78 mills.
	// 10 times: 78 mills.
	// 10 times: 98 mills.
	public void decryptPng() {
		String value = "feb30414e47d3cde";
		String algorithm = "DESede";
		String assignKey = "abcdefgh01234567abcdefgh";
		//
		DecryptDirResult result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.decryptDir(value, assignKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getBaseDir());
	}

	@Test
	// 1 times: 7719 mills.
	public void $unzipDecryptPng() {
		// unzip
		String value = "test.png";
		// String destDir = "png-unzip";
		String destDir = null;
		//
		String result = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = FileHelper.unzip(value, destDir);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);

		// decrypt
		value = "feb30414e47d3cde";
		String algorithm = "DESede";
		String assignKey = "abcdefgh01234567abcdefgh";
		//
		DecryptDirResult decryptDirResult = null;
		count = 1;// 100w
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			decryptDirResult = FileHelper.decryptDir(value, assignKey,
					algorithm);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(decryptDirResult.getBaseDir());
	}
}
