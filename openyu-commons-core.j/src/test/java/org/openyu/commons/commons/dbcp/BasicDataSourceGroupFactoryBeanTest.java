package org.openyu.commons.commons.dbcp;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicDataSourceGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BasicDataSource[] basicDataSources;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/commons/dbcp/testContext-dbcp.xml",//

		});
		basicDataSources = applicationContext.getBean("basicDataSourceGroupFactoryBean", BasicDataSource[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void basicDataSources() {
		System.out.println(basicDataSources);
		assertNotNull(basicDataSources);
		//
		SystemHelper.println(basicDataSources);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(basicDataSources);
		assertNotNull(basicDataSources);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(basicDataSources);
		assertNotNull(basicDataSources);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
