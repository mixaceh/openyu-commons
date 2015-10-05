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

	private static ThreadServiceImpl threadServiceImpl;

	private static ThreadServiceImpl blockingThreadServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/thread/applicationContext-thread.xml",//

		});
		threadServiceImpl = (ThreadServiceImpl) applicationContext.getBean("threadService");
		blockingThreadServiceImpl = (ThreadServiceImpl) applicationContext.getBean("blockingThreadService");
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

	/**
	 * CreateInstanceTest
	 */
	public static class CreateInstanceTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void newInstance() throws Exception {
			ThreadServiceImpl impl = new ThreadServiceImpl();
			// 啟動
			impl.start();
			System.out.println(impl);
			assertNotNull(impl);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void createInstance() {
			ThreadService service = ThreadServiceImpl.createInstance();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() {
			ThreadService service = ThreadServiceImpl.createInstance();
			System.out.println(service);
			assertNotNull(service);
			//
			service = ThreadServiceImpl.shutdownInstance(service);
			assertNull(service);
			// 多次,不會丟出ex
			service = ThreadServiceImpl.shutdownInstance(service);
			assertNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() {
			ThreadService service = ThreadServiceImpl.createInstance();
			System.out.println(service);
			assertNotNull(service);
			//
			service = ThreadServiceImpl.restartInstance(service);
			assertNotNull(service);
			// 多次,不會丟出ex
			service = ThreadServiceImpl.restartInstance(service);
			assertNotNull(service);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0, concurrency = 1)
	public void nextExecutor() {
		ThreadPoolTaskExecutor executor = threadServiceImpl.nextExecutor();
		System.out.println(executor);
		assertNotNull(executor);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submit() {
		Runner runner = new Runner();
		threadServiceImpl.submit(runner);// thread1
		threadServiceImpl.submit(runner);// thread2
		//
		ThreadHelper.sleep(5 * 1000);
	}

	protected class Runner implements Runnable {

		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println("T[" + Thread.currentThread().getId() + "] i = " + i);
			}
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submitWithBlocking() throws Exception {
		Callable<Boolean> caller = new Caller<Boolean>();
		Future<?> future = threadServiceImpl.submit(caller);// thread1

		boolean result = (Boolean) future.get(); // thread1跑完後,才會繼續往下走
		System.out.println(result);

		threadServiceImpl.submit(caller);// thread2
		//
		ThreadHelper.sleep(5 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void submitWithNonBlocking() throws Exception {
		Callable<Boolean> caller = new Caller<Boolean>();
		Future<?> future = threadServiceImpl.submit(caller);// thread1
		System.out.println(future);

		// 若無此future.get(),不管thread1是否跑完,都會繼續往下走
		// boolean result = (Boolean) future.get();
		// System.out.println(result);

		threadServiceImpl.submit(caller);// thread2
		//
		ThreadHelper.sleep(5 * 1000);
	}

	protected class Caller<E> implements Callable<Boolean> {

		public Boolean call() throws Exception {
			for (int i = 0; i < 5; i++) {
				System.out.println("T[" + Thread.currentThread().getId() + "] i = " + i);
			}
			ThreadHelper.sleep(5 * 1000);
			return Boolean.TRUE;
		}
	}
}
