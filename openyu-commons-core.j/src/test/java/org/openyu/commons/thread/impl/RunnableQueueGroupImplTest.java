package org.openyu.commons.thread.impl;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.BaseRunnableQueue;
import org.openyu.commons.thread.LoopQueue;
import org.openyu.commons.thread.RunnableQueueGroup;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.thread.supporter.LoopQueueSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class RunnableQueueGroupImplTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static ExecutorService executorService;

	private static RunnableQueueGroup<String> runnableQueueGroup;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		executorService = Executors.newFixedThreadPool(10);
		//
		@SuppressWarnings("unchecked")
		LoopQueue<String>[] queues = new PrintQueue[3];
		for (int i = 0; i < queues.length; ++i) {
			PrintQueue<String> queue = new PrintQueue<String>(executorService);
			queue.setListenMills(3 * 1000L);
			queues[i] = queue;
		}
		runnableQueueGroup = new RunnableQueueGroupImpl<String>(queues);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void queue() throws Exception {
		System.out.println(runnableQueueGroup);
		assertNotNull(runnableQueueGroup);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void start() throws Exception {
		runnableQueueGroup.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void startWithException() throws Exception {
		// 多次,會中斷
		runnableQueueGroup.start();
		ThreadHelper.sleep(3000L);
		//
		runnableQueueGroup.start();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdown() throws Exception {
		runnableQueueGroup.start();
		ThreadHelper.sleep(3000L);
		//
		runnableQueueGroup.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownWithException() throws Exception {
		runnableQueueGroup.start();
		ThreadHelper.sleep(3000L);
		// 多次,會中斷
		runnableQueueGroup.shutdown();
		ThreadHelper.sleep(3000L);
		//
		runnableQueueGroup.shutdown();
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void offer() throws Exception {
		runnableQueueGroup.start();
		//
		runnableQueueGroup.offer("aaa");
		runnableQueueGroup.offer("bbb");
		runnableQueueGroup.offer("ccc");
		ThreadHelper.sleep(3000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void offerWithException() throws Exception {
		runnableQueueGroup.offer("aaa");
		ThreadHelper.sleep(3000L);
	}

	protected static class PrintQueue<E> extends LoopQueueSupporter<String> {

		public PrintQueue(ExecutorService executorService) {
			super(executorService);
		}

		@Override
		protected void doExecute(String e) throws Exception {
			System.out.println("T[" + Thread.currentThread().getId() + "] " + e);
		}
	}
}
