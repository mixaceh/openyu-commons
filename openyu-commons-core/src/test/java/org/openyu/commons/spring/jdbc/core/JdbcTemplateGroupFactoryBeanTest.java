package org.openyu.commons.spring.jdbc.core;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static JdbcTemplate[] jdbcTemplates;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/spring/jdbc/core/testContext-core-group.xml",//

		});
		jdbcTemplates = applicationContext.getBean("jdbcTemplateGroupFactoryBean",
				JdbcTemplate[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void jdbcTemplates() throws Exception {
		System.out.println(jdbcTemplates);
		assertNotNull(jdbcTemplates);
		//
		for (JdbcTemplate jdbcTemplate : jdbcTemplates) {
			System.out.println(jdbcTemplate.getDataSource().getConnection());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(jdbcTemplates);
		assertNotNull(jdbcTemplates);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(jdbcTemplates);
		assertNotNull(jdbcTemplates);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
