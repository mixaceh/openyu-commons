package org.openyu.commons.atomikos;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AtomikosDataSourceBeanFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static AtomikosDataSourceBean atomikosDataSourceBean;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/atomikos/testContext-atomikos.xml",//

		});
		atomikosDataSourceBean = applicationContext.getBean("atomikosDataSourceBeanFactoryBean", AtomikosDataSourceBean.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void atomikosDataSourceBean() {
		System.out.println(atomikosDataSourceBean);
		assertNotNull(atomikosDataSourceBean);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(atomikosDataSourceBean);
		assertNotNull(atomikosDataSourceBean);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(atomikosDataSourceBean);
		assertNotNull(atomikosDataSourceBean);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
