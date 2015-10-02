package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;

public class CnfDataSourceImplTest {

	protected static CnfDataSourceImpl cnfDataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// GQC
		cnfDataSource = new CnfDataSourceImpl();
		cnfDataSource.setIp("10.1.24.143");
		cnfDataSource.setPort(21);
		cnfDataSource.setTimeout(5000);
		//
		cnfDataSource.setMaxActive(2000);
		cnfDataSource.setMaxIdle(2000);
		//
		cnfDataSource.setRetryNumber(3);
		cnfDataSource.setRetryPauseMills(1000L);
		//
		cnfDataSource.setUsername("mktftp");
		cnfDataSource.setPassword("Mkt2BestDev");
		cnfDataSource.setBufferSize(128 * 1024);
		cnfDataSource.setClientMode(2);
		cnfDataSource.setFileType(2);
		cnfDataSource.setControlEncoding("UTF-8");
		cnfDataSource.setRemotePath("MKTPLS_Batch/inbound/Inventory/");
	}

	@Test
	public void cnfDataSource() {
		System.out.println(cnfDataSource);
		assertNotNull(cnfDataSource);
	}

	@Test
	// 10 times: 2357 mills.
	public void getFTPClient() throws Exception {
		int count = 10;
		FTPClient result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = cnfDataSource.getFTPClient();
			System.out.println(result);
			assertNotNull(result);
			result.disconnect();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		cnfDataSource.getFTPClient();
		cnfDataSource.close();
	}

	@Test(expected = IOException.class)
	public void closeException() throws Exception {
		cnfDataSource.getFTPClient();
		cnfDataSource.close();
		cnfDataSource.close();
	}

	@Test
	public void mockGetFTPClient() throws Exception {
		int count = 1;
		FTPClient result = null;
		for (int i = 0; i < count; i++) {
			result = cnfDataSource.getFTPClient();
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
		FTPClient ftpClient = cnfDataSource.getFTPClient();
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
			FTPClient client = cnfDataSource.getFTPClient();
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
