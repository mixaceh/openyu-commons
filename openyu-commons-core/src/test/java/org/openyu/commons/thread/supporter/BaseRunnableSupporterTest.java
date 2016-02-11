package org.openyu.commons.thread.supporter;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BaseRunnableSupporterTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ExecutorService executorService;

	private static TestRunner testRunner;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		executorService = Executors.newFixedThreadPool(10);
		testRunner = new TestRunner(executorService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void testRunner() {
		System.out.println(testRunner);
		assertNotNull(testRunner);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void start() throws Exception {
		testRunner.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void startWithException() throws Exception {
		// 多次,會中斷
		testRunner.start();
		ThreadHelper.sleep(3000L);
		//
		testRunner.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdown() throws Exception {
		testRunner.start();
		ThreadHelper.sleep(3000L);
		//
		testRunner.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownWithException() throws Exception {
		testRunner.start();
		ThreadHelper.sleep(3000L);
		// 多次,會中斷
		testRunner.shutdown();
		ThreadHelper.sleep(3000L);
		//
		testRunner.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restart() throws Exception {
		testRunner.start();
		ThreadHelper.sleep(3000L);
		//
		testRunner.shutdown();
		ThreadHelper.sleep(3000L);
		//
		testRunner.start();
		ThreadHelper.sleep(3000L);
	}

	public static class MultiThreadTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void start() throws Exception {
			ExecutorService executorService = Executors.newFixedThreadPool(2);
			//
			TestRunner runner = new TestRunner(executorService);
			runner.start();
			//
			runner = new TestRunner(executorService);
			runner.start();
			ThreadHelper.sleep(10 * 1000L);
		}
	}

	protected static class TestRunner extends BaseRunnableSupporter {

		public TestRunner(ExecutorService executorService) {
			super(executorService);
		}

		@Override
		protected void doRun() throws Exception {
			while (true) {
				try {
					if (isShutdown()) {
						break;
					}
					//
					System.out.println("T[" + Thread.currentThread().getId() + "] ");
					ThreadHelper.sleep(1000L);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
	}
}
