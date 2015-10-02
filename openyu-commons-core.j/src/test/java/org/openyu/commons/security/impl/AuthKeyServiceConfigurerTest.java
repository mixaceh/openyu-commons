package org.openyu.commons.security.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.security.AuthKeyService;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class AuthKeyServiceConfigurerTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static AuthKeyService authKeyServiceConfigurer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/thread/applicationContext-thread.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/security/applicationContext-security.xml",//
		});
		authKeyServiceConfigurer = (AuthKeyService) applicationContext.getBean("authKeyServiceConfigurer");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void authKeyServiceConfigurer() {
		System.out.println(authKeyServiceConfigurer);
		assertNotNull(authKeyServiceConfigurer);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(authKeyServiceConfigurer);
		assertNotNull(authKeyServiceConfigurer);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(authKeyServiceConfigurer);
		assertNotNull(authKeyServiceConfigurer);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

}
