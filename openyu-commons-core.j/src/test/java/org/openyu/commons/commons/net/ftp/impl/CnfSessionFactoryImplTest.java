package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.commons.net.ftp.ex.CnfException;

public class CnfSessionFactoryImplTest {

	protected static CnfDataSourceImpl cnfDataSource;

	protected static CnfSessionFactoryImpl cnfSessionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cnfDataSource = new CnfDataSourceImpl();
		cnfDataSource.setIp("127.0.0.1");
		cnfDataSource.setPort(21);
		cnfDataSource.setConnectTimeout(5000);
		//
		cnfDataSource.setMaxActive(2000);
		cnfDataSource.setMaxIdle(2000);
		//
		cnfDataSource.setRetryNumber(3);
		cnfDataSource.setRetryPauseMills(1000L);
		//
		cnfDataSource.setUsername("root");
		cnfDataSource.setPassword("1111");
		cnfDataSource.setBufferSize(128 * 1024);
		cnfDataSource.setClientMode(2);
		cnfDataSource.setFileType(2);
		cnfDataSource.setControlEncoding("UTF-8");
		cnfDataSource.setRemotePath("inbound/");
		//
		cnfSessionFactory = new CnfSessionFactoryImpl();
		cnfSessionFactory.setCnfDataSource(cnfDataSource);
	}

	@Test
	public void cnfSessionFactory() {
		System.out.println(cnfSessionFactory);
		assertNotNull(cnfSessionFactory);
	}

	@Test
	// 10 times: 2571 mills.
	public void openSession() throws Exception {
		int count = 10;
		CnfSession result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = cnfSessionFactory.openSession();
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			cnfSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		cnfSessionFactory.openSession();
		cnfSessionFactory.closeSession();
		cnfSessionFactory.close();
	}

	@Test(expected = CnfException.class)
	public void closeException() throws Exception {
		cnfSessionFactory.openSession();
		cnfSessionFactory.closeSession();
		cnfSessionFactory.close();
		cnfSessionFactory.close();
	}

	@Test
	public void mockOpenSession() throws Exception {
		int count = 1;
		CnfSession result = null;
		for (int i = 0; i < count; i++) {
			result = cnfSessionFactory.openSession();
			// result.close();
			cnfSessionFactory.closeSession();
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
			CnfSession ftpSession = cnfSessionFactory.openSession();
			System.out.println(ftpSession);
			//
			String[] result = ftpSession.listNames();
			//
			SystemHelper.println(result);
			cnfSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
