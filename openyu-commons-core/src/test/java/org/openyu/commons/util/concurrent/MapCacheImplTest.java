package org.openyu.commons.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.util.concurrent.impl.MapCacheImpl;

public class MapCacheImplTest extends BaseTestSupporter
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{}

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{}

	@Test
	//1000000 times: 1535 mills. 
	//1000000 times: 1534 mills.
	//1000000 times: 1525 mills.
	public void createMapCache()
	{
		MapCache<String, Class<?>> cache = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			cache = new MapCacheImpl<String, Class<?>>();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(cache);
	}

	@Test
	//put
	//1000000 times: 94 mills. 
	//1000000 times: 98 mills.
	//1000000 times: 94 mills.
	//
	//get
	//1000000 times: 31 mills.
	//1000000 times: 33 mills.
	//1000000 times: 32 mills.
	public void putAndGet()
	{
		MapCache<String, Class<?>> cache = new MapCacheImpl<String, Class<?>>();
		String key = String.class.getName();
		Class<?> value = String.class;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			cache.put(key, value);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(cache);

		Class<?> result = null;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = cache.get(key);
		}
		//
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	//put
	//1000000 times: 94 mills. 
	//1000000 times: 98 mills.
	//1000000 times: 94 mills.
	//
	//get
	//1000000 times: 21 mills.
	//1000000 times: 32 mills.
	//1000000 times: 32 mills.
	public void putAndGetByNull()
	{
		MapCache<String, Class<?>> cache = new MapCacheImpl<String, Class<?>>();
		String key = "nullKey";
		Class<?> value = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			cache.put(key, value);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(cache);

		Class<?> result = null;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = cache.get(key);
		}
		//
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	//1000000 times: 93 mills. 
	//1000000 times: 94 mills.
	//1000000 times: 100 mills.
	public void lockInterruptibly()
	{
		MapCache<String, Class<?>> cache = new MapCacheImpl<String, Class<?>>();
		String key = String.class.getName();
		Class<?> value = String.class;
		//
		Class<?> result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			try
			{
				cache.lockInterruptibly();
				try
				{
					if (cache.isNotNullValue(key))
					{
						result = cache.get(key);
						if (result == null)
						{
							cache.put(key, value);
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					cache.unlock();
				}
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		System.out.println("cache: " + cache);
	}

	@Test
	//1000000 times: 78 mills. 
	//1000000 times: 93 mills.
	//1000000 times: 90 mills.
	public void lockInterruptiblyByNull()
	{
		MapCache<String, Class<?>> cache = new MapCacheImpl<String, Class<?>>();
		String key = "nullKey";
		Class<?> value = null;
		//
		Class<?> result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			try
			{
				cache.lockInterruptibly();
				try
				{
					if (cache.isNotNullValue(key))
					{
						result = cache.get(key);
						if (result == null)
						{
							cache.put(key, value);
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					cache.unlock();
				}
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		System.out.println("cache: " + cache);
	}

	@Test
	//1000000 times: 445 mills.
	//1000000 times: 435 mills.
	//1000000 times: 411 mills.
	public void getKeys()
	{
		MapCache<String, Integer> cache = new MapCacheImpl<String, Integer>();
		//
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		//
		List<String> result = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = cache.getKeys();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	//1000000 times: 445 mills.
	//1000000 times: 435 mills.
	//1000000 times: 411 mills.
	public void getValues()
	{
		MapCache<String, Integer> cache = new MapCacheImpl<String, Integer>();
		//
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		cache.put(randomString(), randomInt(100));
		//
		List<Integer> result = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = cache.getValues();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void multiThreadCache() throws Exception
	{
		Thread t1 = new Thread(new Runner());
		Thread t2 = new Thread(new Runner());
		Thread t3 = new Thread(new Runner());
		//
		t1.start();
		Thread.sleep(100);

		t2.start();
		Thread.sleep(100);

		t3.start();
		Thread.sleep(100);
		//
		t1.interrupt();
		Thread.sleep(100);

		//t2.interrupt();
		//Thread.sleep(100);
		//		t3.interrupt();
		//		Thread.sleep(100);
		//

		Thread.sleep(10000);//停10秒
	}

	/**
	 * cache
	 * 
	 */
	public static class Helper
	{
		private static MapCache<String, Integer> mapCache = new MapCacheImpl<String, Integer>();

		public Helper()
		{}

		public static Integer createInteger(String key)
		{
			Integer result = null;
			try
			{
				mapCache.lockInterruptibly();
				try
				{
					if (mapCache.isNotNullValue(key))
					{
						result = mapCache.get(key);
						if (result == null)
						{
							try
							{
								//Thread.sleep(3000);//停3秒
								result = Integer.parseInt(key);
							}
							catch (Exception ex)
							{

							}
							mapCache.put(key, result);
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					mapCache.unlock();
				}
			}
			catch (InterruptedException ex)
			{
				//ex.printStackTrace();
				System.out.println("createInteger thread[" + Thread.currentThread().getId() + "] "
						+ " InterruptedException");
			}
			return result;
		}
	}

	/**
	 * 執行緒
	 * 
	 */
	public static class Runner implements Runnable
	{

		public void run()
		{
			System.out.println("thread[" + Thread.currentThread().getId() + "] " + " start");
			Integer result = null;
			try
			{
				//				while (result == null)
				//				{
				int randomInt = NumberHelper.randomInt(0, 1000000);
				result = Helper.createInteger(randomInt + "a");
				//				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			System.out.println("thread[" + Thread.currentThread().getId() + "] finish. result="
					+ result);
		}
	}

}
