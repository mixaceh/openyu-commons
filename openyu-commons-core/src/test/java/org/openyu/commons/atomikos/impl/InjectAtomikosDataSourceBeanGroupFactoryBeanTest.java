package org.openyu.commons.atomikos.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InjectAtomikosDataSourceBeanGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static AtomikosDataSourceBean[] atomikosDataSourceBeans;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/atomikos/testContext-atomikos-group-inject.xml",//

		});
		atomikosDataSourceBeans = applicationContext.getBean("atomikosDataSourceBeanGroupFactoryBean", AtomikosDataSourceBean[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void atomikosDataSourceBeans() throws Exception {
		System.out.println(atomikosDataSourceBeans);
		assertNotNull(atomikosDataSourceBeans);
		//
		for (AtomikosDataSourceBean dataSource : atomikosDataSourceBeans) {
			System.out.println(dataSource.getConnection());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(atomikosDataSourceBeans);
		assertNotNull(atomikosDataSourceBeans);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(atomikosDataSourceBeans);
		assertNotNull(atomikosDataSourceBeans);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
