package org.openyu.commons.bitronix;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BenchmarkDatabaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class BenchmarkBitronixTest extends BenchmarkDatabaseTestSupporter {

	protected static DataSource dataSource2;

	protected static JdbcTemplate jdbcTemplate2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/bitronix/testContext-bitronix.xml",//

		});
		// ---------------------------------------------------
		initialize();
		// ---------------------------------------------------
		dataSource2 = (DataSource) applicationContext.getBean("dataSource2");
		jdbcTemplate2 = (JdbcTemplate) applicationContext.getBean("jdbcTemplate2");
	}

	public static class BeanTest extends BenchmarkBitronixTest {
		@Test
		public void dataSource() throws Exception {
			System.out.println(dataSource);
			assertNotNull(dataSource);
			//
			System.out.println(dataSource.getConnection());
		}

		@Test
		public void dataSource2() throws Exception {
			System.out.println(dataSource2);
			assertNotNull(dataSource2);
			//
			System.out.println(dataSource2.getConnection());
		}

		@Test
		public void jdbcTemplate() {
			System.out.println(jdbcTemplate);
			assertNotNull(jdbcTemplate);
		}

		@Test
		public void jdbcTemplate2() {
			System.out.println(jdbcTemplate2);
			assertNotNull(jdbcTemplate2);
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkBitronixTest {

		@Test
		// 2016/07/26 pc
		// 10000 rows, 102628000 bytes / 51384 ms. = 1997.28 BYTES/MS, 1950.46
		// K/S, 1.9 MB/S
		public void insert() throws Exception {
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
								// byte[] buff =
								// ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								byte[] buff = new byte[LENGTH_OF_BYTES];
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("insert into TEST_BENCHMARK (seq, id, info) ");
									sql.append("values (?, ?, ?)");

									long seq = seqCounter.getAndIncrement();
									// 0_0
									String newId = userId + "_" + i;
									// params
									Object[] params = new Object[] { seq, newId, new String(buff) };
									int inserted = jdbcTemplate.update(sql.toString(), params);

									System.out.println("I[" + userId + "] R[" + i + "], " + inserted);
									//
									if (inserted > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper.toByteArray(seq).length);
										byteCounter.addAndGet(ByteHelper.toByteArray(newId).length);
										byteCounter.addAndGet(buff.length);
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
		// 2016/07/26 pc
		// 10000 rows, 102628000 bytes / 46064 ms. = 2227.94 BYTES/MS, 2175.73
		// K/S, 2.12 MB/S
		public void select() throws Exception {
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
									StringBuilder sql = new StringBuilder();
									sql.append("select seq, id, info from TEST_BENCHMARK ");
									sql.append("where seq=?");

									long seq = seqCounter.getAndIncrement();
									// params
									Object[] params = new Object[] { seq };
									List<TestBenchmark> list = jdbcTemplate.query(sql.toString(), params,
											new TestBenchmarkRowMapper());
									//
									seq = 0;
									String id = null;
									String info = null;
									if (list.size() > 0) {
										TestBenchmark row = list.get(0);
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
		// 2016/07/26 pc
		// 10000 rows, 102400000 bytes / 56351 ms. = 1817.18 BYTES/MS, 1774.59
		// K/S, 1.73 MB/S
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
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								byte[] prefix = ByteHelper.toByteArray("UPDATE_");
								buff = ArrayHelper.add(prefix,
										ByteHelper.getByteArray(buff, 0, buff.length - prefix.length));
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("update TEST_BENCHMARK set info=? ");
									sql.append("where seq=?");

									long seq = seqCounter.getAndIncrement();
									// params
									String info = "UPDATE_" + new String(new byte[LENGTH_OF_BYTES - 7]);
									Object[] params = new Object[] { info, seq };
									int updated = jdbcTemplate.update(sql.toString(), params);

									System.out.println("I[" + userId + "] R[" + i + "], " + updated);
									//
									if (updated > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
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
		// 2016/07/26 pc
		// 10000 rows, 102400000 bytes / 28171 ms. = 3634.94 BYTES/MS, 3549.75
		// K/S, 3.47 MB/S
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
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("delete from TEST_BENCHMARK ");
									sql.append("where seq=?");

									long seq = seqCounter.getAndIncrement();
									// params
									Object[] params = new Object[] { seq };
									int deleted = jdbcTemplate.update(sql.toString(), params);

									System.out.println("I[" + userId + "] R[" + i + "], " + deleted);
									//
									if (deleted > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
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
	}
}
