package org.openyu.commons.aop.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NonBlockingThreadAdviceTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static NonBlockingThreadAdvice nonBlockingThreadAdvice;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"org/openyu/commons/service/applicationContext-service.xml",//
				"org/openyu/commons/thread/applicationContext-thread.xml",//
				"org/openyu/commons/aop/applicationContext-aop.xml",//

		});

		nonBlockingThreadAdvice = (NonBlockingThreadAdvice) applicationContext
				.getBean("nonBlockingThreadAdvice");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void nonBlockingThreadAdvice() {
		System.out.println(nonBlockingThreadAdvice);
		assertNotNull(nonBlockingThreadAdvice);
		applicationContext.close();
	}

}
