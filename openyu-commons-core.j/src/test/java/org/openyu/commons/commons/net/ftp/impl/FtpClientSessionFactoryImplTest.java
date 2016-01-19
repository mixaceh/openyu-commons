package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.commons.net.ftp.ex.FtpClientException;

public class FtpClientSessionFactoryImplTest {

	protected static FtpClientConnectionFactoryImpl ftpClientConnectionFactory;

	protected static FtpClientSessionFactoryImpl ftpClientSessionFactory;

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
		//
		ftpClientSessionFactory = new FtpClientSessionFactoryImpl();
		ftpClientSessionFactory.setFtpClientConnectionFactory(ftpClientConnectionFactory);
	}

	@Test
	public void ftpClientSessionFactory() {
		System.out.println(ftpClientSessionFactory);
		assertNotNull(ftpClientSessionFactory);
	}

	@Test
	// 10 times: 2571 mills.
	public void openSession() throws Exception {
		int count = 10;
		FtpClientSession result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ftpClientSessionFactory.openSession();
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			ftpClientSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		ftpClientSessionFactory.openSession();
		ftpClientSessionFactory.closeSession();
		ftpClientSessionFactory.close();
	}

	@Test(expected = FtpClientException.class)
	public void closeException() throws Exception {
		ftpClientSessionFactory.openSession();
		ftpClientSessionFactory.closeSession();
		ftpClientSessionFactory.close();
		ftpClientSessionFactory.close();
	}

	@Test
	public void mockOpenSession() throws Exception {
		int count = 1;
		FtpClientSession result = null;
		for (int i = 0; i < count; i++) {
			result = ftpClientSessionFactory.openSession();
			// result.close();
			ftpClientSessionFactory.closeSession();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockOpenSessionWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockOpenSession();
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
	// 10 times: 12454 mills.
	public void listNames() throws Exception {
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			FtpClientSession ftpSession = ftpClientSessionFactory.openSession();
			System.out.println(ftpSession);
			//
			String[] result = ftpSession.listNames();
			//
			SystemHelper.println(result);
			ftpClientSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
