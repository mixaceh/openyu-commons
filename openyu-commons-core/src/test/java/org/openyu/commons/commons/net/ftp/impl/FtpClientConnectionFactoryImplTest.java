package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class FtpClientConnectionFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static FtpClientConnectionFactoryImpl ftpClientConnectionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ftpClientConnectionFactory = new FtpClientConnectionFactoryImpl();
		ftpClientConnectionFactory.setIp("127.0.0.1");
		ftpClientConnectionFactory.setPort(21);
		ftpClientConnectionFactory.setConnectTimeout(15000);
		//
		ftpClientConnectionFactory.setMaxActive(10);
		ftpClientConnectionFactory.setMaxIdle(10);
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
		ftpClientConnectionFactory.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.02 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void ftpClientConnectionFactory() {
		System.out.println(ftpClientConnectionFactory);
		assertNotNull(ftpClientConnectionFactory);
	}

	@Test
	// 10 times: 2357 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 2.67 [+- 0.27], round.block: 1.68 [+- 0.56], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 2.76, time.warmup: 0.01,
	// time.bench: 2.75
	public void getFTPClient() throws Exception {
		FTPClient result = null;
		result = ftpClientConnectionFactory.getFTPClient();
		System.out.println(result);
		assertNotNull(result);
		result.disconnect();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.74 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.74, time.warmup: 0.00,
	// time.bench: 1.74
	public void close() throws Exception {
		ftpClientConnectionFactory.getFTPClient();
		ftpClientConnectionFactory.close();
	}

	@Test(expected = IOException.class)
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.83 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.83, time.warmup: 0.00,
	// time.bench: 1.83
	public void closeException() throws Exception {
		ftpClientConnectionFactory.getFTPClient();
		ftpClientConnectionFactory.close();
		ftpClientConnectionFactory.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockGetFTPClient() throws Exception {
		int count = 1;
		FTPClient result = null;
		for (int i = 0; i < count; i++) {
			result = ftpClientConnectionFactory.getFTPClient();
			// result.disconnect();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.75 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.75, time.warmup: 0.00,
	// time.bench: 1.75
	public void ftpClient() throws Exception {
		FTPClient ftpClient = ftpClientConnectionFactory.getFTPClient();
		//
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
		ftpClient.disconnect();
	}

	@Test
	// 10 times: 12094 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 3.33 [+- 0.25], round.block: 1.59 [+- 0.53], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 3.42, time.warmup: 0.00,
	// time.bench: 3.42
	public void listNames() throws Exception {
		FTPClient client = ftpClientConnectionFactory.getFTPClient();
		System.out.println(client);
		//
		String[] result = client.listNames();
		//
		SystemHelper.println(result);
		client.disconnect();
	}

}
