package org.openyu.commons.spring.jdbc.datasource;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

public class LazyConnectionDataSourceProxyGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static LazyConnectionDataSourceProxy[] lazyConnectionDataSourceProxys;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/spring/jdbc/datasource/testContext-datasource.xml",//

		});
		lazyConnectionDataSourceProxys = applicationContext.getBean("lazyConnectionDataSourceProxyGroupFactoryBean",
				LazyConnectionDataSourceProxy[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void lazyConnectionDataSourceProxys() throws Exception {
		System.out.println(lazyConnectionDataSourceProxys);
		assertNotNull(lazyConnectionDataSourceProxys);
		//
		for (LazyConnectionDataSourceProxy e : lazyConnectionDataSourceProxys) {
			System.out.println(e.getTargetDataSource().getConnection());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(lazyConnectionDataSourceProxys);
		assertNotNull(lazyConnectionDataSourceProxys);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(lazyConnectionDataSourceProxys);
		assertNotNull(lazyConnectionDataSourceProxys);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
