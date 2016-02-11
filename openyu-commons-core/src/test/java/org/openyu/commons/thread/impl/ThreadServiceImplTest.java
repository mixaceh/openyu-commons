package org.openyu.commons.thread.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.thread.ThreadService;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ThreadServiceImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ThreadService threadService;

	private static ThreadService blockingThreadService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/thread/testContext-thread.xml", //

		});
		threadService = (ThreadService) applicationContext.getBean("threadService");
		blockingThreadService = (ThreadService) applicationContext.getBean("blockingThreadService");
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
	public void close() {
		System.out.println(threadService);
		assertNotNull(threadService);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(threadService);
		assertNotNull(threadService);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

	/**
	 * GetInstanceTest
	 */
	public static class GetInstanceTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
		public void getInstance() {
			ThreadService instance = ThreadServiceImpl.getInstance(3, 150, 600, 150, 75);
			System.out.println(instance);
			assertNotNull(instance);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() {
			ThreadService instance = ThreadServiceImpl.getInstance(3, 150, 600, 150, 75);
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = ThreadServiceImpl.shutdownInstance();
			assertNull(instance);
			// 多次,不會丟出ex
			instance = ThreadServiceImpl.shutdownInstance();
			assertNull(instance);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() {
			ThreadService instance = ThreadServiceImpl.getInstance(3, 150, 600, 150, 75);
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = ThreadServiceImpl.restartInstance();
			assertNotNull(instance);
			// 多次,不會丟出ex
			instance = ThreadServiceImpl.restartInstance();
			assertNotNull(instance);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0, concurrency = 1)
	public void nextExecutor() {
		ThreadPoolTaskExecutor executor = ((ThreadServiceImpl) threadService).nextExecutor();
		System.out.println(executor);
		assertNotNull(executor);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submit() {
		Runner runner = new Runner();
		threadService.submit(runner);// t1
		threadService.submit(runner);// t2
		threadService.submit(runner);// t3
		threadService.submit(runner);// t4
		threadService.submit(runner);// t5
		threadService.submit(runner);// t6
		threadService.submit(runner);// t7
		threadService.submit(runner);// t8
		threadService.submit(runner);// t9
		threadService.submit(runner);// t10
		//
		ThreadHelper.sleep(3 * 1000);
	}

	protected class Runner implements Runnable {

		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println("T[" + Thread.currentThread().getId() + "] i = " + i);
			}
			ThreadHelper.sleep(3 * 1000);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submitWithBlocking() throws Exception {
		Caller caller = new Caller();
		Future<Boolean> future = threadService.submit(caller);// thread1
		boolean result = future.get(); // thread1跑完後,才會繼續往下走
		System.out.println(result);
		//
		threadService.submit(caller);// thread2
		ThreadHelper.sleep(3 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submitWithNonBlocking() throws Exception {
		Caller caller = new Caller();
		Future<Boolean> future = threadService.submit(caller);// thread1
		System.out.println(future);

		// 若無此future.get(),不管thread1是否跑完,都會繼續往下走
		// boolean result = (Boolean) future.get();
		// System.out.println(result);

		threadService.submit(caller);// thread2
		//
		ThreadHelper.sleep(3 * 1000);
	}

	protected class Caller implements Callable<Boolean> {

		public Boolean call() throws Exception {
			for (int i = 0; i < 5; i++) {
				System.out.println("T[" + Thread.currentThread().getId() + "] i = " + i);
			}
			ThreadHelper.sleep(3 * 1000);
			return Boolean.TRUE;
		}
	}
}
