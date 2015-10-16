package org.openyu.commons.commons.pool.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SoftReferenceCacheFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static SoftReferenceCacheFactoryFactoryBean<Parser, SoftReferenceCacheFactory<Parser>> softReferenceCacheFactoryFactoryBean;

	private static SoftReferenceCacheFactoryImpl<Parser> softReferenceCacheFactoryImpl;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// softReferenceCacheFactory = new
		// SoftReferenceCacheFactoryImpl<Parser>(
		// new CacheableObjectFactorySupporter<Parser>() {
		//

		// softReferenceCacheFactory = SoftReferenceCacheFactoryImpl
		// .createInstance(new CacheableObjectFactorySupporter<Parser>() {
		//
		// private static final long serialVersionUID = -5161964541145838308L;
		//
		// public Parser makeObject() throws Exception {
		// return new Parser();
		// }
		//
		// public void destroyObject(Parser obj) throws Exception {
		// obj.close();
		// }
		//
		// public boolean validateObject(Parser obj) {
		// return true;
		// }
		//
		// public void activateObject(Parser obj) throws Exception {
		//
		// }
		//
		// public void passivateObject(Parser obj) throws Exception {
		// obj.flush();
		// obj.reset();
		// }
		// });
		//
		// softReferenceCacheFactoryImpl =
		// (SoftReferenceCacheFactoryImpl<Parser>)
		// softReferenceCacheFactory;

		// 改用FactoryBean
		softReferenceCacheFactoryFactoryBean = new SoftReferenceCacheFactoryFactoryBean<Parser, SoftReferenceCacheFactory<Parser>>();
		softReferenceCacheFactoryFactoryBean.setCacheableObjectFactory(new CacheableObjectFactorySupporter<Parser>() {

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
		softReferenceCacheFactoryFactoryBean.start();
		softReferenceCacheFactoryImpl = (SoftReferenceCacheFactoryImpl<Parser>) softReferenceCacheFactoryFactoryBean
				.getObject();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void softReferenceCacheFactoryImpl() {
		System.out.println(softReferenceCacheFactoryImpl);
		assertNotNull(softReferenceCacheFactoryImpl);
		softReferenceCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	// round: 0.10 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.01, time.warmup: 0.00,
	// time.bench: 1.01
	public void openCache() {
		Parser result = null;
		result = softReferenceCacheFactoryImpl.openCache();
		System.out.println(result);
		assertNotNull(result);
		//
		softReferenceCacheFactoryImpl.closeCache();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		softReferenceCacheFactoryImpl.openCache();
		softReferenceCacheFactoryImpl.closeCache();
		softReferenceCacheFactoryImpl.close();
	}

	@Test()
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeWithException() {
		softReferenceCacheFactoryImpl.openCache();
		softReferenceCacheFactoryImpl.closeCache();
		softReferenceCacheFactoryImpl.close();
		softReferenceCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void clear() {
		Parser result = null;
		result = softReferenceCacheFactoryImpl.openCache();
		System.out.println(result);
		softReferenceCacheFactoryImpl.closeCache();
		//
		int numIdle = softReferenceCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);
		softReferenceCacheFactoryImpl.clear();
		//
		numIdle = softReferenceCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(0, numIdle);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCache() {
		Parser result = null;
		result = softReferenceCacheFactoryImpl.openCache();
		// System.out.println(result);
		softReferenceCacheFactoryImpl.closeCache();
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void execute() {
		softReferenceCacheFactoryImpl.execute(new CacheCallback<Parser>() {
			public Object doInAction(Parser cache) throws CacheException {
				Object result = null;
				try {
					result = cache.parse();
					System.out.println(result);
				} catch (Exception ex) {
					throw new CacheException(ex);
				}
				//
				System.out.println("[" + Thread.currentThread().getName() + "] " + cache);
				return result;
			}
		});
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void outOfMemory() {
		Parser result = softReferenceCacheFactoryImpl.openCache();
		assertNotNull(result);
		softReferenceCacheFactoryImpl.closeCache();
		int numIdle = softReferenceCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		result = null;
		System.gc();

		numIdle = softReferenceCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		try {
			mockOutOfMemory();
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			//
			numIdle = softReferenceCacheFactoryImpl.getNumIdle();
			System.out.println("numIdle: " + numIdle);
			assertEquals(0, numIdle);
			//
			result = softReferenceCacheFactoryImpl.openCache();
			assertNotNull(result);
			softReferenceCacheFactoryImpl.closeCache();
			numIdle = softReferenceCacheFactoryImpl.getNumIdle();
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
