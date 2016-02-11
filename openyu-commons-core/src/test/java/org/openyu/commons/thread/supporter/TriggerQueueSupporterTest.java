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

public class TriggerQueueSupporterTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ExecutorService executorService;

	private static Queue<String> queue;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		executorService = Executors.newFixedThreadPool(10);
		queue = new Queue<String>(executorService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void queue() throws Exception {
		System.out.println(queue);
		assertNotNull(queue);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void start() throws Exception {
		queue.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void startWithException() throws Exception {
		// 多次,會中斷
		queue.start();
		ThreadHelper.sleep(3000L);
		//
		queue.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdown() throws Exception {
		queue.start();
		ThreadHelper.sleep(3000L);
		//
		queue.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownWithException() throws Exception {
		queue.start();
		ThreadHelper.sleep(3000L);
		// 多次,會中斷
		queue.shutdown();
		ThreadHelper.sleep(3000L);
		//
		queue.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void offer() throws Exception {
		queue.start();
		//
		queue.offer("aaa");
		queue.offer("bbb");
		queue.offer("ccc");
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void offerWithException() {
		queue.offer("aaa");
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
			Queue<String> queue = new Queue<String>(executorService);
			queue.start();
			queue.offer("aaa");
			//
			queue = new Queue<String>(executorService);
			queue.start();
			queue.offer("bbb");
			ThreadHelper.sleep(10 * 1000L);
		}
	}

	protected static class Queue<E> extends TriggerQueueSupporter<String> {

		public Queue(ExecutorService executorService) {
			super(executorService);
		}

		@Override
		protected void doExecute(String e) throws Exception {
			System.out.println("T[" + Thread.currentThread().getId() + "] " + e);
		}
	}
}
