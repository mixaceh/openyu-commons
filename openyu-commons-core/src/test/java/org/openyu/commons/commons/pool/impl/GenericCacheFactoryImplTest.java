package org.openyu.commons.commons.pool.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.impl.GenericCacheFactoryImplTest.Parser;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class GenericCacheFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static GenericCacheFactoryFactoryBean<Parser, GenericCacheFactory<Parser>> genericCacheFactoryFactoryBean;

	private static GenericCacheFactoryImpl<Parser> genericCacheFactoryImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// genericCacheFactory = new GenericCacheFactoryImpl<Parser>(
		// new CacheableObjectFactorySupporter<Parser>() {
		//
		// 改用newInstance
		// genericCacheFactory = GenericCacheFactoryImpl.createInstance(new
		// CacheableObjectFactorySupporter<Parser>() {
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
		// }
		//
		// public void passivateObject(Parser obj) throws Exception {
		// obj.flush();
		// obj.reset();
		// }
		// });
		// genericCacheFactoryImpl = (GenericCacheFactoryImpl<Parser>)
		// genericCacheFactory;

		// 改用FactoryBean
		genericCacheFactoryFactoryBean = new GenericCacheFactoryFactoryBean<Parser, GenericCacheFactory<Parser>>();
		genericCacheFactoryFactoryBean.setCacheableObjectFactory(new CacheableObjectFactorySupporter<Parser>() {

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
		genericCacheFactoryFactoryBean.start();
		genericCacheFactoryImpl = (GenericCacheFactoryImpl<Parser>) genericCacheFactoryFactoryBean.getObject();

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void genericCacheFactoryImpl() {
		System.out.println(genericCacheFactoryImpl);
		assertNotNull(genericCacheFactoryImpl);
		genericCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	public void openCache() throws Exception {
		Parser result = null;
		result = genericCacheFactoryImpl.openCache();
		System.out.println(result);
		assertNotNull(result);
		//
		genericCacheFactoryImpl.closeCache();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() throws Exception {
		genericCacheFactoryImpl.openCache();
		genericCacheFactoryImpl.closeCache();
		genericCacheFactoryImpl.close();
	}

	@Test()
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeWithException() {
		genericCacheFactoryImpl.openCache();
		genericCacheFactoryImpl.closeCache();
		genericCacheFactoryImpl.close();
		genericCacheFactoryImpl.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void clear() throws Exception {
		Parser result = null;
		result = genericCacheFactoryImpl.openCache();
		System.out.println(result);
		genericCacheFactoryImpl.closeCache();
		//
		int numIdle = genericCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);
		genericCacheFactoryImpl.clear();
		//
		numIdle = genericCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(0, numIdle);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCache() throws Exception {
		Parser result = null;
		result = genericCacheFactoryImpl.openCache();
		// System.out.println(result);
		genericCacheFactoryImpl.closeCache();
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
		Thread.sleep(1 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCacheWithMultiThread() throws Exception {
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
			Thread.sleep(NumberHelper.randomInt(100));
		}
		//
		Thread.sleep(5 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void execute() {
		genericCacheFactoryImpl.execute(new CacheCallback<Parser>() {
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
		Parser result = genericCacheFactoryImpl.openCache();
		assertNotNull(result);
		genericCacheFactoryImpl.closeCache();
		int numIdle = genericCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		result = null;
		System.gc();

		numIdle = genericCacheFactoryImpl.getNumIdle();
		System.out.println("numIdle: " + numIdle);
		assertEquals(1, numIdle);

		try {
			mockOutOfMemory();
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			//
			numIdle = genericCacheFactoryImpl.getNumIdle();
			System.out.println("numIdle: " + numIdle);
			assertEquals(1, numIdle);
			//
			result = genericCacheFactoryImpl.openCache();
			assertNotNull(result);
			genericCacheFactoryImpl.closeCache();
			numIdle = genericCacheFactoryImpl.getNumIdle();
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
