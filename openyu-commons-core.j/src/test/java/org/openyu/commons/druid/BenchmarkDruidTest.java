package org.openyu.commons.druid;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.hibernate.type.StandardBasicTypes;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkDruidTest extends BaseTestSupporter {

	private static DataSource dataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/druid/testContext-druid.xml",//

		});
		dataSource = (DataSource) applicationContext.getBean("dataSource");
	}

	@Test
	public void dataSource() {
		System.out.println(dataSource);
		assertNotNull(dataSource);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static Connection createConnection() {
		Connection result = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			result = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/commons", "SA", "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Test
	public void connection() {
		Connection connection = createConnection();
		System.out.println(connection);
		assertNotNull(connection);
	}

	@Test
	public void createTable() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE TEST_CHENG ");
		sql.append("(");
		sql.append("seq bigint NOT NULL,");
		sql.append("id varchar(50) NULL,");
		sql.append("info varchar(max) NULL,");
		sql.append("CONSTRAINT PK_test_cheng PRIMARY KEY CLUSTERED ");
		sql.append("(seq ASC) ");
		sql.append(")");
		//
		Connection connection = createConnection();
		Statement stmt = connection.createStatement();
		boolean result = stmt.execute(sql.toString());
		// 無論成功失敗,都傳回false
		System.out.println(result);
	}

	@Test
	public void getTable() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Connection connection = createConnection();
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet rs = databaseMetaData.getColumns(null, null, TABLE_NAME, null);
		assertNotNull(rs);
		//
		while (rs.next()) {
			String columnName = rs.getString(4);// COLUMN_NAME
			int dataType = rs.getInt(5);// DATA_TYPE
			String typeName = rs.getString(6);// TYPE_NAME
			int columnSize = rs.getInt(7); // COLUMN_SIZE

			System.out.println(columnName + ", " + dataType + ", " + typeName + ", " + columnSize);

			// getColumns()欄位說明, 參考 DatabaseMetaData.getColumns()
			// getTables()欄位說明, 參考 DatabaseMetaData.getTables()
		}
	}

	@Test
	public void alterTable() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("ALTER TABLE TEST_CHENG ALTER COLUMN id varchar(255) ");
		//
		Connection connection = createConnection();
		Statement stmt = connection.createStatement();
		boolean result = stmt.execute(sql.toString());
		// 無論成功失敗,都傳回false
		System.out.println(result);
	}

	@Test
	public void deleteTable() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("DROP TABLE TEST_CHENG");
		//
		Connection connection = createConnection();
		Statement stmt = connection.createStatement();
		boolean result = stmt.execute(sql.toString());
		// 無論成功失敗,都傳回false
		System.out.println(result);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkDruidTest {

		@Test
		// insert: 10000 rows, 102400000 bytes / 38545 ms. = 2656.64 BYTES/MS,
		// 2594.37 K/S, 2.53 MB/S
		public void nativeInsert() throws Exception {
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
								//
								Connection connection = createConnection();
								PreparedStatement psmt = null;
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("insert into TEST_CHENG (seq, id, info) ");
									sql.append("values (?, ?, ?)");

									connection = createConnection();
									psmt = connection.prepareStatement(sql.toString());

									long seq = seqCounter.getAndIncrement();
									// 0_0
									String newId = userId + "_" + i;

									// params
									psmt.setLong(1, seq);
									psmt.setString(2, newId);
									psmt.setString(3, new String(buff));
									int inserted = psmt.executeUpdate();

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
									try {
										if (psmt != null) {
											psmt.close();
										}
										if (connection != null) {
											connection.close();
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("insert: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// select: 10000 rows, 102400000 bytes / 31782 ms. = 3221.95 BYTES/MS,
		// 3146.44 K/S, 3.07 MB/S
		public void nativeSelect() throws Exception {
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
								//
								Connection connection = createConnection();
								PreparedStatement psmt = null;
								ResultSet rs = null;
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("select seq, id, info from TEST_CHENG ");
									sql.append("where seq=?");

									connection = createConnection();
									psmt = connection.prepareStatement(sql.toString());

									long seq = seqCounter.getAndIncrement();
									// params
									psmt.setLong(1, seq);
									rs = psmt.executeQuery();
									//
									seq = 0;
									String id = null;
									String info = null;
									if (rs.next()) {
										seq = rs.getLong(1);
										id = rs.getString(2);
										info = rs.getString(3);
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
									try {
										if (rs != null) {
											rs.close();
										}
										if (psmt != null) {
											psmt.close();
										}
										if (connection != null) {
											connection.close();
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("select: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// update: 10000 rows, 102400000 bytes / 41240 ms. = 2483.03 BYTES/MS,
		// 2424.83 K/S, 2.37 MB/S
		public void nativeUpdate() throws Exception {
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
								//
								Connection connection = createConnection();
								PreparedStatement psmt = null;
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("update TEST_CHENG set info=? ");
									sql.append("where seq=?");

									connection = createConnection();
									psmt = connection.prepareStatement(sql.toString());

									long seq = seqCounter.getAndIncrement();
									// params
									psmt.setString(1, new String(buff));
									psmt.setLong(2, seq);
									int inserted = psmt.executeUpdate();

									System.out.println("I[" + userId + "] R[" + i + "], " + inserted);
									//
									if (inserted > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									try {
										if (psmt != null) {
											psmt.close();
										}
										if (connection != null) {
											connection.close();
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("update: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// delete: 10000 rows, 102400000 bytes / 43219 ms. = 2369.33 BYTES/MS,
		// 2313.8 K/S, 2.26 MB/S
		public void nativeDelete() throws Exception {
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
								//
								Connection connection = createConnection();
								PreparedStatement psmt = null;
								try {
									StringBuilder sql = new StringBuilder();
									sql.append("delete from TEST_CHENG ");
									sql.append("where seq=?");

									connection = createConnection();
									psmt = connection.prepareStatement(sql.toString());

									long seq = seqCounter.getAndIncrement();
									// params
									psmt.setLong(1, seq);
									int deleted = psmt.executeUpdate();

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
									try {
										if (psmt != null) {
											psmt.close();
										}
										if (connection != null) {
											connection.close();
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("delete: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkDruidTest {

		@Test
		// insert: 10000 rows, 102400000 bytes / 29690 ms. = 3448.97 BYTES/MS,
		// 3368.14 K/S, 3.29 MB/S
		public void optimizedInsert() throws Exception {
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
									sql.append("insert into TEST_CHENG (seq, id, info) ");
									sql.append("values (:seq, :id, :info)");

									long seq = seqCounter.getAndIncrement();
									// 0_0
									String newId = userId + "_" + i;
									// params
									Map<String, Object> params = new LinkedHashMap<String, Object>();
									params.put("seq", seq);
									params.put("id", newId);
									params.put("info", new String(buff));
									int inserted = ojDaoSupporter.insert(sql.toString(), params);

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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("insert: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// select: 10000 rows, 102400000 bytes / 20454 ms. = 5006.36 BYTES/MS,
		// 4889.02 K/S, 4.77 MB/S
		public void optimizedSelect() throws Exception {
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
									sql.append("select seq, id, info from TEST_CHENG ");
									sql.append("where seq=:seq");

									long seq = seqCounter.getAndIncrement();
									// params
									Map<String, Object> params = new LinkedHashMap<String, Object>();
									params.put("seq", seq);
									// scalars
									Map<String, Object> scalars = new LinkedHashMap<String, Object>();
									scalars.put("seq", StandardBasicTypes.LONG);
									scalars.put("id", StandardBasicTypes.STRING);
									scalars.put("info", StandardBasicTypes.STRING);
									//
									List<Object[]> list = ojDaoSupporter.find(sql.toString(), params, scalars);
									//
									seq = 0;
									String id = null;
									String info = null;
									if (list.size() > 0) {
										Object[] row = list.get(0);
										seq = NumberHelper.safeGet((Long) row[0]);
										id = (String) row[1];
										info = (String) row[2];
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("select: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// update: 10000 rows, 102400000 bytes / 34485 ms. = 2969.41 BYTES/MS,
		// 2899.81 K/S, 2.83 MB/S
		public void optimizedUpdate() throws Exception {
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
									sql.append("update TEST_CHENG set info=:info ");
									sql.append("where seq=:seq");

									long seq = seqCounter.getAndIncrement();
									// params
									Map<String, Object> params = new LinkedHashMap<String, Object>();
									params.put("seq", seq);
									params.put("info", new String(buff));
									int updated = ojDaoSupporter.update(sql.toString(), params);

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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("update: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// delete: 10000 rows, 102400000 bytes / 18315 ms. = 5591.05 BYTES/MS,
		// 5460.01 K/S, 5.33 MB/S
		public void optimizedDelete() throws Exception {
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
									sql.append("delete from TEST_CHENG ");
									sql.append("where seq=:seq");

									long seq = seqCounter.getAndIncrement();
									// params
									Map<String, Object> params = new LinkedHashMap<String, Object>();
									params.put("seq", seq);
									int deleted = ojDaoSupporter.delete(sql.toString(), params);

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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("delete: " + timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

	}

}
