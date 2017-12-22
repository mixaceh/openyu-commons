package org.openyu.commons.commons.pool.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.CacheTemplate;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.lang.NumberHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class GenericCacheTemplateImplTest extends GenericCacheFactoryImplTest {

	private static CacheTemplate<Parser> cacheTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GenericCacheFactoryImplTest.setUpBeforeClass();
		//
		cacheTemplate = new CacheTemplateImpl<Parser>();
		cacheTemplate.setCacheFactory(genericCacheFactoryImpl);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void cacheTemplate() {
		System.out.println(cacheTemplate);
		assertNotNull(cacheTemplate);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	public void openCache() throws Exception {
		Parser result = null;
		result = cacheTemplate.openCache();
		System.out.println(result);
		assertNotNull(result);
		//
		cacheTemplate.closeCache();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenCache() throws Exception {
		Parser result = null;
		result = cacheTemplate.openCache();
		// System.out.println(result);
		cacheTemplate.closeCache();
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
		cacheTemplate.execute(new CacheCallback<Parser>() {
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
}
