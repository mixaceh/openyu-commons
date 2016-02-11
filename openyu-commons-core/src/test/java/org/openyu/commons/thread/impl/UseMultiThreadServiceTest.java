package org.openyu.commons.thread.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadService;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class UseMultiThreadServiceTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ThreadService threadService;

	private static ThreadService blockingThreadService;

	private static UseMultiThreadService useMultiThreadService;

	private static UseMultiThreadService2 useMultiThreadService2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/thread/testContext-use-multi-thread.xml", //

		});
		threadService = (ThreadService) applicationContext.getBean("threadService");
		blockingThreadService = (ThreadService) applicationContext.getBean("blockingThreadService");
		//
		useMultiThreadService = (UseMultiThreadService) applicationContext.getBean("useMultiThreadService");
		useMultiThreadService2 = (UseMultiThreadService2) applicationContext.getBean("useMultiThreadService2");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void threadService() {
		System.out.println(threadService);
		assertNotNull(threadService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void blockingThreadService() {
		System.out.println(blockingThreadService);
		assertNotNull(blockingThreadService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void useMultiThreadService() {
		System.out.println(useMultiThreadService);
		assertNotNull(useMultiThreadService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void useMultiThreadService2() {
		System.out.println(useMultiThreadService2);
		assertNotNull(useMultiThreadService2);
	}
}
