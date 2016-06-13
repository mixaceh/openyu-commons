package org.openyu.commons.commons.dbcp;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BenchmarkDatabaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkDbcpTest extends BenchmarkDatabaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/commons/dbcp/testContext-dbcp.xml",//

		});
		// ---------------------------------------------------
		initialize();
		// ---------------------------------------------------
	}

	public static class BeanTest extends BenchmarkDbcpTest {
		@Test
		public void dataSource() throws Exception {
			System.out.println(dataSource);
			assertNotNull(dataSource);
			//
			System.out.println(dataSource.getConnection());
		}

		@Test
		public void jdbcTemplate() {
			System.out.println(jdbcTemplate);
			assertNotNull(jdbcTemplate);
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkDbcpTest {

		@Test
		// insert: 10000 rows, 102400000 bytes / 29690 ms. = 3448.97 BYTES/MS,
		// 3368.14 K/S, 3.29 MB/S

		// 2015/10/09 nb
		// insert: 10000 rows, 102628000 bytes / 82989 ms. = 1236.65 BYTES/MS,
		// 1207.66 K/S, 1.18 MB/S

		// 2015/11/12
		// 10000 rows, 102628000 bytes / 62640 ms. = 1638.38 BYTES/MS, 1599.98
		// K/S, 1.56 MB/S

		// 2016/06/13 pc
		// 10000 rows, 102628000 bytes / 39808 ms. = 2578.07 BYTES/MS, 2517.65
		// K/S, 2.46 MB/S
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
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("insert into TEST_BENCHMARK (seq, id, info) ");
									sql.append("values (:seq, :id, :info)");

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
		// 10000 rows, 102400000 bytes / 20454 ms. = 5006.36 BYTES/MS,
		// 4889.02 K/S, 4.77 MB/S

		// 2015/10/09 nb
		// 10000 rows, 183460321 bytes / 25246 ms. = 7266.91 BYTES/MS,
		// 7096.59 K/S, 6.93 MB/S

		// 2015/10/12 pc
		// 10000 rows, 183473056 bytes / 16079 ms. = 11410.73 BYTES/MS, 11143.29
		// K/S, 10.88 MB/S

		// 2016/06/13 pc
		// 10000 rows, 183481296 bytes / 18162 ms. = 10102.48 BYTES/MS, 9865.71
		// K/S, 9.63 MB/S
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
									sql.append("where seq=:seq");

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
		// update: 10000 rows, 102400000 bytes / 34485 ms. = 2969.41 BYTES/MS,
		// 2899.81 K/S, 2.83 MB/S

		// 2015/10/09 nb
		// update: 10000 rows, 102400000 bytes / 124789 ms. = 820.59 BYTES/MS,
		// 801.35 K/S, 0.78 MB/S

		// 2016/06/13 pc
		// 10000 rows, 102400000 bytes / 49709 ms. = 2059.99 BYTES/MS, 2011.71
		// K/S, 1.96 MB/S
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
									sql.append("update TEST_BENCHMARK set info=:info ");
									sql.append("where seq=:seq");

									long seq = seqCounter.getAndIncrement();
									// params
									Object[] params = new Object[] { new String(buff), seq };
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
		// delete: 10000 rows, 102400000 bytes / 18315 ms. = 5591.05 BYTES/MS,
		// 5460.01 K/S, 5.33 MB/S

		// 2015/10/09 nb
		// delete: 10000 rows, 102400000 bytes / 26270 ms. = 3897.98 BYTES/MS,
		// 3806.62 K/S, 3.72 MB/S

		// 2016/06/13 pc
		// 10000 rows, 102400000 bytes / 18939 ms. = 5406.83 BYTES/MS, 5280.11
		// K/S, 5.16 MB/S
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
									sql.append("where seq=:seq");

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
