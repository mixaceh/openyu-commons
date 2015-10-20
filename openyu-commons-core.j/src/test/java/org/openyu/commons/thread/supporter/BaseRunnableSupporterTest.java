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

	private static Runner runner;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		executorService = Executors.newFixedThreadPool(10);
		runner = new Runner(executorService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void runner() {
		System.out.println(runner);
		assertNotNull(runner);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void start() {
		runner.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void startWithException() {
		// 多次,會中斷
		runner.start();
		ThreadHelper.sleep(3000L);
		//
		runner.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdown() {
		runner.start();
		ThreadHelper.sleep(3000L);
		//
		runner.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownWithException() {
		runner.start();
		ThreadHelper.sleep(3000L);
		// 多次,會中斷
		runner.shutdown();
		ThreadHelper.sleep(3000L);
		//
		runner.shutdown();
		ThreadHelper.sleep(3000L);
	}
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restart() {
		runner.start();
		ThreadHelper.sleep(3000L);
		//
		runner.shutdown();
		ThreadHelper.sleep(3000L);
		//
		runner.start();
		ThreadHelper.sleep(3000L);
	}

	protected static class Runner extends BaseRunnableSupporter {

		public Runner(ExecutorService executorService) {
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
