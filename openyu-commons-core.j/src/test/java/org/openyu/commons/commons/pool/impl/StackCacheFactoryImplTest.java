package org.openyu.commons.commons.pool.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.StackCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class StackCacheFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static StackCacheFactory<Parser> stackCacheFactory;

	private static StackCacheFactoryImpl<Parser> stackCacheFactoryImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// stackCacheFactory = new StackCacheFactoryImpl<Parser>(
		// new CacheableObjectFactorySupporter<Parser>() {
		//
		// 改用newInstance
		stackCacheFactory = StackCacheFactoryImpl
				.createInstance(new CacheableObjectFactorySupporter<Parser>() {

					private static final long serialVersionUID = -5161964541145838308L;

					public Parser makeObject() throws Exception {
						return new Parser();
					}

					public void destroyObject(Parser obj) throws Exception {
						obj.close();
					}

					public boolean validateObject(Parser obj) {
						return true;
					}

					public void activateObject(Parser obj) throws Exception {
					}

					public void passivateObject(Parser obj) throws Exception {
						obj.flush();
						obj.reset();
					}
				});

		stackCacheFactoryImpl = (StackCacheFactoryImpl<Parser>) stackCacheFactory;

		// stackCacheFactoryImpl.initialize();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void stackCacheFactoryImpl() {
		System.out.println(stackCacheFactoryImpl);
		assertNotNull(stackCacheFactoryImpl);
		stackCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	public void openCache() {
		Parser result = null;
		result = stackCacheFactoryImpl.openCache();
		System.out.println(result);
		assertNotNull(result);
		//
		stackCacheFactoryImpl.closeCache();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		stackCacheFactoryImpl.openCache();
		stackCacheFactoryImpl.closeCache();
		stackCacheFactoryImpl.close();
	}

	@Test()
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeWithException() {
		stackCacheFactoryImpl.openCache();
		stackCacheFactoryImpl.closeCache();
		stackCacheFactoryImpl.close();
		stackCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void clear() {
		Parser result = null;
		result = stackCacheFactoryImpl.openCache();
		System.out.println(result);
		stackCacheFactoryImpl.closeCache();
		//
		int numIdle = stackCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);
		stackCacheFactoryImpl.clear();
		//
		numIdle = stackCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(0, numIdle);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCache() {
		Parser result = null;
		result = stackCacheFactoryImpl.openCache();
		// System.out.println(result);
		stackCacheFactoryImpl.closeCache();
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		ThreadHelper.sleep(1 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCacheWithMultiThread() {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockOpenCache();
					} catch (Exception ex) {
					}
				}
			});
			thread.setName("T-" + i);
			thread.start();
			ThreadHelper.sleep(NumberHelper.randomInt(100));
		}
		//
		ThreadHelper.sleep(5 * 1000);
	}

	@Test
	public void execute() {
		stackCacheFactoryImpl.execute(new CacheCallback<Parser>() {
			public Object doInAction(Parser cache) throws CacheException {
				Object result = null;
				try {
					result = cache.parse();
					System.out.println(result);
				} catch (Exception ex) {
					throw new CacheException(ex);
				}
				//
				System.out.println("[" + Thread.currentThread().getName()
						+ "] " + cache);
				return result;
			}
		});
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void outOfMemory() {
		Parser result = stackCacheFactoryImpl.openCache();
		assertNotNull(result);
		stackCacheFactoryImpl.closeCache();
		int numIdle = stackCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		result = null;
		System.gc();

		numIdle = stackCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		try {
			mockOutOfMemory();
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			//
			numIdle = stackCacheFactoryImpl.getNumIdle();
			System.out.println("numIdle: " + numIdle);
			assertEquals(1, numIdle);
			//
			result = stackCacheFactoryImpl.openCache();
			assertNotNull(result);
			stackCacheFactoryImpl.closeCache();
			numIdle = stackCacheFactoryImpl.getNumIdle();
			System.out.println("numIdle: " + numIdle);
			assertEquals(1, numIdle);
		}
	}

	protected static class Parser {
		public Parser() {
		}

		public String parse() {
			return "parse done";
		}

		public void flush() {
			System.out.println("flush");
		}

		public void reset() {
			System.out.println("reset");
		}

		public void close() {
			System.out.println("close");
		}
	}
}
