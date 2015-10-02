package org.openyu.commons.misc;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import sun.misc.Unsafe;

public class CounterTest extends BaseTestSupporter {

	protected interface Counter {
		public void increment();

		public long getCounter();
	}

	protected class CounterClient implements Runnable {
		private Counter c;
		private int num;

		public CounterClient(Counter c, int num) {
			this.c = c;
			this.num = num;
		}

		public void run() {
			for (int i = 0; i < num; i++) {
				c.increment();
			}
		}
	}

	@Test
	// Counter result: 98853456
	// Time passed in ms:776
	public void stupidCounter() throws Exception {
		int NUM_OF_THREADS = 1000;
		int NUM_OF_INCREMENTS = 100000;
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Counter counter = new StupidCounter();
		long before = System.currentTimeMillis();
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		long after = System.currentTimeMillis();
		System.out.println("Counter result: " + counter.getCounter());
		System.out.println("Time passed in ms:" + (after - before));
	}

	protected class StupidCounter implements Counter {

		private long counter = 0;

		public void increment() {
			counter++;

		}

		public long getCounter() {
			return counter;
		}
	}

	@Test
	// Counter result: 100000000
	// Time passed in ms:4261
	public void syncCounter() throws Exception {
		int NUM_OF_THREADS = 1000;
		int NUM_OF_INCREMENTS = 100000;
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Counter counter = new SyncCounter();
		long before = System.currentTimeMillis();
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		long after = System.currentTimeMillis();
		System.out.println("Counter result: " + counter.getCounter());
		System.out.println("Time passed in ms:" + (after - before));
	}

	protected class SyncCounter implements Counter {

		private long counter = 0;

		public synchronized void increment() {
			counter++;

		}

		public long getCounter() {
			return counter;
		}
	}

	@Test
	// Counter result: 100000000
	// Time passed in ms:2857
	public void lockCounter() throws Exception {
		int NUM_OF_THREADS = 1000;
		int NUM_OF_INCREMENTS = 100000;
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Counter counter = new LockCounter();
		long before = System.currentTimeMillis();
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		long after = System.currentTimeMillis();
		System.out.println("Counter result: " + counter.getCounter());
		System.out.println("Time passed in ms:" + (after - before));
	}

	protected class LockCounter implements Counter {
		private long counter = 0;
		private WriteLock lock = new ReentrantReadWriteLock().writeLock();

		public void increment() {
			lock.lock();
			try {
				counter++;
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		public long getCounter() {
			return counter;
		}
	}

	@Test
	// Counter result: 100000000
	// Time passed in ms:9063
	public void atomicCounter() throws Exception {
		int NUM_OF_THREADS = 1000;
		int NUM_OF_INCREMENTS = 100000;
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Counter counter = new AtomicCounter();
		long before = System.currentTimeMillis();
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		long after = System.currentTimeMillis();
		System.out.println("Counter result: " + counter.getCounter());
		System.out.println("Time passed in ms:" + (after - before));
	}

	protected class AtomicCounter implements Counter {
		AtomicLong counter = new AtomicLong(0);

		public void increment() {
			counter.incrementAndGet();
		}

		public long getCounter() {
			return counter.get();
		}
	}

	@Test
	// Counter result: 100000000
	// Time passed in ms:8638
	public void casCounter() throws Exception {
		int NUM_OF_THREADS = 1000;
		int NUM_OF_INCREMENTS = 100000;
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		Counter counter = new CASCounter();
		long before = System.currentTimeMillis();
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		long after = System.currentTimeMillis();
		System.out.println("Counter result: " + counter.getCounter());
		System.out.println("Time passed in ms:" + (after - before));
	}

	public static Unsafe getUnsafe() throws Exception {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);
		return unsafe;
	}

	protected class CASCounter implements Counter {
		private volatile long counter = 0;
		private Unsafe unsafe;
		private long offset;

		public CASCounter() throws Exception {
			unsafe = getUnsafe();
			offset = unsafe.objectFieldOffset(CASCounter.class
					.getDeclaredField("counter"));
		}

		public void increment() {
			long before = counter;
			while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
				before = counter;
			}
		}

		public long getCounter() {
			return counter;
		}
	}
}
