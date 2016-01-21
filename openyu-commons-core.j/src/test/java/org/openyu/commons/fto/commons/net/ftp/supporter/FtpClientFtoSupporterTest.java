package org.openyu.commons.fto.commons.net.ftp.supporter;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.fto.supporter.CommonFtoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FtpClientFtoSupporterTest extends BaseTestSupporter {

	private static CommonFtoSupporter ftpClientFtoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml", //
				"applicationContext-ftp-client.xml",//
		});
		ftpClientFtoSupporter = (CommonFtoSupporter) applicationContext.getBean("ftpClientFtoSupporter");
	}

	@Test
	// 10 times: 12252 mills.
	public void listFiles() throws Exception {
		FTPFile[] result = null;
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ftpClientFtoSupporter.listFiles();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result);
	}

	@Test
	// 10 times: 12061 mills.
	public void listNames() throws Exception {
		String[] result = null;
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ftpClientFtoSupporter.listNames();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result);
	}
}
