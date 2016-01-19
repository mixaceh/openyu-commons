package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;

public class FtpClientConnectionFactoryImplTest {

	protected static FtpClientConnectionFactoryImpl ftpClientConnectionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ftpClientConnectionFactory = new FtpClientConnectionFactoryImpl();
		ftpClientConnectionFactory.setIp("127.0.0.1");
		ftpClientConnectionFactory.setPort(21);
		ftpClientConnectionFactory.setConnectTimeout(5000);
		//
		ftpClientConnectionFactory.setMaxActive(2000);
		ftpClientConnectionFactory.setMaxIdle(2000);
		//
		ftpClientConnectionFactory.setRetryNumber(3);
		ftpClientConnectionFactory.setRetryPauseMills(1000L);
		//
		ftpClientConnectionFactory.setUsername("root");
		ftpClientConnectionFactory.setPassword("1111");
		ftpClientConnectionFactory.setBufferSize(128 * 1024);
		ftpClientConnectionFactory.setClientMode(2);
		ftpClientConnectionFactory.setFileType(2);
		ftpClientConnectionFactory.setControlEncoding("UTF-8");
		ftpClientConnectionFactory.setRemotePath("inbound/");
	}

	@Test
	public void ftpClientConnectionFactory() {
		System.out.println(ftpClientConnectionFactory);
		assertNotNull(ftpClientConnectionFactory);
	}

	@Test
	// 10 times: 2357 mills.
	public void getFTPClient() throws Exception {
		int count = 10;
		FTPClient result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ftpClientConnectionFactory.getFTPClient();
			System.out.println(result);
			assertNotNull(result);
			result.disconnect();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		ftpClientConnectionFactory.getFTPClient();
		ftpClientConnectionFactory.close();
	}

	@Test(expected = IOException.class)
	public void closeException() throws Exception {
		ftpClientConnectionFactory.getFTPClient();
		ftpClientConnectionFactory.close();
		ftpClientConnectionFactory.close();
	}

	@Test
	public void mockGetFTPClient() throws Exception {
		int count = 1;
		FTPClient result = null;
		for (int i = 0; i < count; i++) {
			result = ftpClientConnectionFactory.getFTPClient();
			// result.disconnect();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockGetFTPClientWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetFTPClient();
					} catch (Exception ex) {
					}
				}
			});
			thread.setName("T-" + i);
			thread.start();
			Thread.sleep(NumberHelper.randomInt(100));
		}
		//
		Thread.sleep(1 * 60 * 60 * 1000);
	}

	@Test
	public void ftpClient() throws Exception {
		FTPClient ftpClient = ftpClientConnectionFactory.getFTPClient();
		//
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
		ftpClient.disconnect();
	}

	@Test
	// 10 times: 12094 mills.
	public void listNames() throws Exception {
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			FTPClient client = ftpClientConnectionFactory.getFTPClient();
			System.out.println(client);
			//
			String[] result = client.listNames();
			//
			SystemHelper.println(result);
			client.disconnect();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
