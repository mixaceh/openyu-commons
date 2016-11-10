package org.openyu.commons.spring.jdbc.datasource.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DriverManagerDataSourceFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BasicDataSource basicDataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/commons/dbcp/testContext-dbcp.xml",//

		});
		basicDataSource = applicationContext.getBean("basicDataSourceFactoryBean", BasicDataSource.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds =1, warmupRounds = 0, concurrency = 1)
	// round: 0.20 [+- 0.09], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.39, time.warmup: 0.00,
	// time.bench: 0.39
	public void basicDataSource() throws Exception {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
		//
		System.out.println(basicDataSource.getConnection());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
