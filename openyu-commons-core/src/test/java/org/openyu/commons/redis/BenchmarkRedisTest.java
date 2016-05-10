package org.openyu.commons.redis;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.junit.supporter.BenchmarkDatabaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.redis.serializer.impl.JdkRedisSerializer;
import org.openyu.commons.redis.serializer.impl.KryoRedisSerializer;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import redis.clients.jedis.JedisPoolConfig;

public class BenchmarkRedisTest extends BaseTestSupporter {

	private static KryoRedisSerializer kryoRedisSerializer;

	private static JdkRedisSerializer jdkRedisSerializer;

	private static JedisPoolConfig jedisPoolConfig;

	private static JedisConnectionFactory jedisConnectionFactory;

	private static RedisTemplate redisTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/redis/testContext-redis.xml",//

		});
		//
		kryoRedisSerializer = (KryoRedisSerializer) applicationContext.getBean("kryoRedisSerializer");
		jdkRedisSerializer = (JdkRedisSerializer) applicationContext.getBean("jdkRedisSerializer");
		jedisPoolConfig = (JedisPoolConfig) applicationContext.getBean("jedisPoolConfig");
		jedisConnectionFactory = (JedisConnectionFactory) applicationContext.getBean("jedisConnectionFactory");
		redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
	}

	public static class BeanTest extends BenchmarkRedisTest {
		@Test
		public void kryoRedisSerializer() {
			System.out.println(kryoRedisSerializer);
			assertNotNull(kryoRedisSerializer);
		}

		@Test
		public void jdkRedisSerializer() {
			System.out.println(jdkRedisSerializer);
			assertNotNull(jdkRedisSerializer);
		}

		@Test
		public void jedisPoolConfig() {
			System.out.println(jedisPoolConfig);
			assertNotNull(jedisPoolConfig);
		}

		@Test
		public void jedisConnectionFactory() {
			System.out.println(jedisConnectionFactory);
			assertNotNull(jedisConnectionFactory);
			//
			System.out.println(jedisConnectionFactory.getConnection());
		}

		@Test
		public void redisTemplate() {
			System.out.println(redisTemplate);
			assertNotNull(redisTemplate);
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkRedisTest {

		@Test
		// 10000 rows, 102628000 bytes / 19510 ms. = 5260.28 BYTES/MS, 5136.99
		// K/S, 5.02 MB/S
		public void set() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					//
					@SuppressWarnings("unchecked")
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								try {
									StringBuilder sql = new StringBuilder();
									sql.append(userId);

									long seq = seqCounter.getAndIncrement();
									// 0_0
									String newId = userId + "_" + i;
									//
									TestBenchmark value = new TestBenchmark();
									value.setSeq(seq);
									value.setId(newId);
									value.setInfo(new String(buff));
									//
									redisTemplate.opsForValue().set(String.valueOf(seq), value);
									System.out.println("I[" + userId + "] R[" + i + "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(ByteHelper.toByteArray(seq).length);
									byteCounter.addAndGet(ByteHelper.toByteArray(newId).length);
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			printResult(beg, byteCounter, timesCounter);
		}

		@Test
		// 10000 rows, 183456704 bytes / 21263 ms. = 8627.98 BYTES/MS, 8425.76
		// K/S, 8.23 MB/S
		public void get() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								try {
									long seq = seqCounter.getAndIncrement();

									TestBenchmark value = (TestBenchmark) redisTemplate.opsForValue()
											.get(String.valueOf(seq));
									//
									seq = 0;
									String id = null;
									String info = null;
									if (value != null) {
										TestBenchmark row = value;
										seq = row.getSeq();
										id = row.getId();
										info = row.getInfo();
									}
									System.out.println("I[" + id + "] R[" + i + "], " + seq);
									//
									if (seq > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper.toByteArray(seq).length);
										byteCounter.addAndGet(ByteHelper.toByteArray(id).length);
										byteCounter.addAndGet(ByteHelper.toByteArray(info).length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			printResult(beg, byteCounter, timesCounter);
		}

		@Test
		// 10000 rows, 102400000 bytes / 16412 ms. = 6239.34 BYTES/MS, 6093.1
		// K/S, 5.95 MB/S
		public void update() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					//
					@SuppressWarnings("unchecked")
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								byte[] prefix = ByteHelper.toByteArray("UPDATE_");
								buff = ArrayHelper.add(prefix,
										ByteHelper.getByteArray(buff, 0, buff.length - prefix.length));
								try {

									long seq = seqCounter.getAndIncrement();
									String newId = userId + "_" + i;

									TestBenchmark value = new TestBenchmark();
									value.setSeq(seq);
									value.setId(newId);
									value.setInfo(new String(buff));
									//
									redisTemplate.opsForValue().set(String.valueOf(seq), value);
									//
									System.out.println("I[" + userId + "] R[" + i + "]");
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			printResult(beg, byteCounter, timesCounter);
		}

		@Test
		// 10000 rows, 102400000 bytes / 17920 ms. = 5714.29 BYTES/MS, 5580.36
		// K/S, 5.45 MB/S
		public void delete() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					//
					@SuppressWarnings("unchecked")
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								try {

									long seq = seqCounter.getAndIncrement();
									redisTemplate.delete(String.valueOf(seq));
									//
									System.out.println("I[" + userId + "] R[" + i + "]");
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			printResult(beg, byteCounter, timesCounter);
		}
	}

	/**
	 * bean
	 */
	protected static class TestBenchmark implements Serializable {

		private static final long serialVersionUID = 3859603353500064626L;

		private long seq;

		private String id;

		private String info;

		public TestBenchmark() {
		}

		public long getSeq() {
			return seq;
		}

		public void setSeq(long seq) {
			this.seq = seq;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

	}
}
