package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.commons.net.ftp.FtpClientFactory;

public class FtpClientFactoryImplTest {

	protected static FtpClientFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = new FtpClientFactoryImpl("127.0.0.1", 21, 0, 3, 1000L,
				"root", "1111", 128 * 1024, 2, 2, "UTF-8",
				"inbound/");
	}

	@Test
	// 10 times: 13866 mills.
	public void createFTPClient() throws Exception {
		int count = 10;
		FTPClient result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = factory.createFTPClient();
			System.out.println(result);
			assertNotNull(result);
			result.disconnect();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
