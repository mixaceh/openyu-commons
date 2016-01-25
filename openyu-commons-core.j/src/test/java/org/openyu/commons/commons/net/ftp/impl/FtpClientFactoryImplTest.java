package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.commons.net.ftp.FtpClientFactory;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class FtpClientFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static FtpClientFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = new FtpClientFactoryImpl("127.0.0.1", 21, 15000, 3, 1000L, "root", "1111", 128 * 1024, 2, 2,
				"UTF-8", "inbound/");
	}

	@Test
	// 10 times: 13866 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 0.86 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.86, time.warmup: 0.00,
	// time.bench: 0.86
	public void createFTPClient() throws Exception {
		FTPClient result = null;
		result = factory.createFTPClient();
		System.out.println(result);
		assertNotNull(result);
		result.disconnect();
	}
}
