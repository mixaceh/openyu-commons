package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.SystemHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.commons.net.ftp.ex.FtpClientException;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class FtpClientSessionFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static FtpClientConnectionFactoryImpl ftpClientConnectionFactory;

	protected static FtpClientSessionFactoryImpl ftpClientSessionFactory;

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
		//
		ftpClientSessionFactory = new FtpClientSessionFactoryImpl();
		ftpClientSessionFactory.setFtpClientConnectionFactory(ftpClientConnectionFactory);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.02 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void ftpClientSessionFactory() {
		System.out.println(ftpClientSessionFactory);
		assertNotNull(ftpClientSessionFactory);
	}

	@Test
	// 10 times: 2571 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 2.55 [+- 0.26], round.block: 1.58 [+- 0.53], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 2.63, time.warmup: 0.00,
	// time.bench: 2.63
	public void openSession() throws Exception {
		FtpClientSession result = null;
		result = ftpClientSessionFactory.openSession();
		System.out.println(result);
		assertNotNull(result);
		// result.close();
		ftpClientSessionFactory.closeSession();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.83 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.83, time.warmup: 0.00,
	// time.bench: 1.83
	public void close() throws Exception {
		ftpClientSessionFactory.openSession();
		ftpClientSessionFactory.closeSession();
		ftpClientSessionFactory.close();
	}

	@Test(expected = FtpClientException.class)
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.76 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.76, time.warmup: 0.00,
	// time.bench: 1.76
	public void closeException() throws Exception {
		ftpClientSessionFactory.openSession();
		ftpClientSessionFactory.closeSession();
		ftpClientSessionFactory.close();
		ftpClientSessionFactory.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenSession() throws Exception {
		int count = 1;
		FtpClientSession result = null;
		for (int i = 0; i < count; i++) {
			result = ftpClientSessionFactory.openSession();
			// result.close();
			// ftpClientSessionFactory.closeSession();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
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
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 3.26 [+- 0.23], round.block: 1.57 [+- 0.52], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 3.34, time.warmup: 0.00,
	// time.bench: 3.34
	public void listNames() throws Exception {
		FtpClientSession ftpSession = ftpClientSessionFactory.openSession();
		System.out.println(ftpSession);
		//
		String[] result = ftpSession.listNames();
		//
		SystemHelper.println(result);
		ftpClientSessionFactory.closeSession();
	}

}
