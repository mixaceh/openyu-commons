package org.openyu.commons.spring.jdbc.core.namedparam;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class NamedParameterJdbcTemplateGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static NamedParameterJdbcTemplate[] namedParameterJdbcTemplates;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/spring/jdbc/core/namedparam/testContext-namedparam.xml",//

		});
		namedParameterJdbcTemplates = applicationContext.getBean("namedParameterJdbcTemplateGroupFactoryBean",
				NamedParameterJdbcTemplate[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void namedParameterJdbcTemplates() throws Exception {
		System.out.println(namedParameterJdbcTemplates);
		assertNotNull(namedParameterJdbcTemplates);
		//
		for (NamedParameterJdbcTemplate e : namedParameterJdbcTemplates) {
			System.out.println(e.getJdbcOperations());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(namedParameterJdbcTemplates);
		assertNotNull(namedParameterJdbcTemplates);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(namedParameterJdbcTemplates);
		assertNotNull(namedParameterJdbcTemplates);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
