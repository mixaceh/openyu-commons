package org.openyu.commons.aop.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThreadAdviceTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ThreadAdvice threadAdvice;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/thread/testContext-thread.xml", //
				"org/openyu/commons/aop/applicationContext-aop.xml",//

		});

		threadAdvice = (ThreadAdvice) applicationContext.getBean("threadAdvice");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void threadAdvice() {
		System.out.println(threadAdvice);
		assertNotNull(threadAdvice);
	}

}
