package org.openyu.commons.commons.net.ftp.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FtpClientConnectionFactoryFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static FtpClientConnectionFactory ftpClientConnectionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/commons/net/ftp/testContext-net-ftp.xml",//

		});
		ftpClientConnectionFactory = applicationContext.getBean("ftpClientConnectionFactoryFactoryBean",
				FtpClientConnectionFactory.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 1.77 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.77, time.warmup: 0.00,
	// time.bench: 1.77
	public void ftpClientConnectionFactory() throws Exception {
		System.out.println(ftpClientConnectionFactory);
		assertNotNull(ftpClientConnectionFactory);
		//
		System.out.println(ftpClientConnectionFactory.getFTPClient());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(ftpClientConnectionFactory);
		assertNotNull(ftpClientConnectionFactory);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(ftpClientConnectionFactory);
		assertNotNull(ftpClientConnectionFactory);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
