package org.openyu.commons.thread.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThreadServiceFactoryBeanTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ThreadServiceImpl threadServiceImpl;

	private static ThreadServiceImpl blockingThreadServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/thread/testContext-thread.xml",//

		});
		threadServiceImpl = (ThreadServiceImpl) applicationContext.getBean("threadServiceFactoryBean");
		blockingThreadServiceImpl = (ThreadServiceImpl) applicationContext.getBean("blockingThreadServiceFactoryBean");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void threadServiceImpl() {
		System.out.println(threadServiceImpl);
		assertNotNull(threadServiceImpl);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void blockingThreadServiceImpl() {
		System.out.println(blockingThreadServiceImpl);
		assertNotNull(blockingThreadServiceImpl);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(threadServiceImpl);
		assertNotNull(threadServiceImpl);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(threadServiceImpl);
		assertNotNull(threadServiceImpl);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

	/**
	 * CreateInstanceTest
	 */
	public static class CreateInstanceTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void createInstance() throws Exception {
			ThreadServiceFactoryBean<ThreadService> factoryBean = new ThreadServiceFactoryBean<ThreadService>();
			ThreadService service = factoryBean.createInstance();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() throws Exception {
			ThreadServiceFactoryBean<ThreadService> factoryBean = new ThreadServiceFactoryBean<ThreadService>();
			factoryBean.start();
			ThreadService service = factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			service = factoryBean.shutdownInstance();
			assertNull(service);
			// 多次,不會丟出ex
			service = factoryBean.shutdownInstance();
			assertNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() throws Exception {
			ThreadServiceFactoryBean<ThreadService> factoryBean = new ThreadServiceFactoryBean<ThreadService>();
			factoryBean.start();
			ThreadService service = factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			service = factoryBean.restartInstance();
			assertNotNull(service);
			// 多次,不會丟出ex
			service = factoryBean.restartInstance();
			assertNotNull(service);
		}
	}

}
