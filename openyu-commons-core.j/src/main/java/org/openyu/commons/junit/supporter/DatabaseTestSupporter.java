package org.openyu.commons.junit.supporter;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.dbcp.BenchmarkDbcpTest;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DatabaseTestSupporter extends BaseTestSupporter {

	protected static DataSource dataSource;

	protected static JdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// applicationContext = new ClassPathXmlApplicationContext(new String[]
		// { //
		// "applicationContext-init.xml", //
		//
		// });
		// // ---------------------------------------------------
		// initialize();
		// // ---------------------------------------------------
	}

	protected static void initialize() {
		dataSource = (DataSource) applicationContext.getBean("dataSource");
		jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
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
		// sql.append(useMysql());
		sql.append(useHsql());
		//
		Connection connection = createConnection();
		Statement stmt = connection.createStatement();
		boolean result = stmt.execute(sql.toString());
		// 無論成功失敗,都傳回false
		System.out.println(result);
	}

	/**
	 * mysql 語法
	 * 
	 * @return
	 */
	protected static String useMysql() {
		StringBuilder sql = new StringBuilder();
		// mysql
		sql.append("CREATE TABLE TEST_CHENG ");
		sql.append("(");
		sql.append("seq bigint NOT NULL,");
		sql.append("id varchar(50) NULL,");
		sql.append("info varchar(max) NULL,");
		sql.append("CONSTRAINT PK_test_cheng PRIMARY KEY CLUSTERED ");
		sql.append("(seq ASC) ");
		sql.append(")");
		return sql.toString();
	}

	/**
	 * hsql 語法
	 * 
	 * @return
	 */
	protected static String useHsql() {
		StringBuilder sql = new StringBuilder();
		// hsql
		sql.append("CREATE TABLE TEST_CHENG ");
		sql.append("(");
		sql.append("seq bigint NOT NULL,");
		sql.append("id varchar(50) NULL,");
		sql.append("info varchar(10240) NULL,");
		sql.append("PRIMARY KEY (seq) ");
		sql.append(")");
		return sql.toString();
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
	public void dropTable() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("DROP TABLE TEST_CHENG");
		//
		Connection connection = createConnection();
		Statement stmt = connection.createStatement();
		boolean result = stmt.execute(sql.toString());
		// 無論成功失敗,都傳回false
		System.out.println(result);
	}

	protected static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception ex) {
		}
	}

	protected static void close(PreparedStatement psmt) {
		try {
			if (psmt != null) {
				psmt.close();
			}
		} catch (Exception ex) {
		}
	}

	protected static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * 印出結果
	 * 
	 * @param beg
	 * @param byteCounter
	 * @param timesCounter
	 */
	protected static void printResult(long beg, AtomicLong byteCounter, AtomicLong timesCounter) {
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

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkDbcpTest {

		@Test
		// insert: 10000 rows, 102400000 bytes / 38545 ms. = 2656.64 BYTES/MS,
		// 2594.37 K/S, 2.53 MB/S

		// 2015/10/09
		// insert: 10000 rows, 102628000 bytes / 124588 ms. = 823.74 BYTES/MS,
		// 804.43 K/S, 0.79 MB/S
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
									close(psmt);
									close(connection);
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
		// select: 10000 rows, 102400000 bytes / 31782 ms. = 3221.95 BYTES/MS,
		// 3146.44 K/S, 3.07 MB/S

		// 2015/10/09
		// select: 10000 rows, 183462421 bytes / 64106 ms. = 2861.86 BYTES/MS,
		// 2794.79 K/S, 2.73 MB/S
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
									close(rs);
									close(psmt);
									close(connection);
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
		// update: 10000 rows, 102400000 bytes / 41240 ms. = 2483.03 BYTES/MS,
		// 2424.83 K/S, 2.37 MB/S

		// 2015/10/09
		// update: 10000 rows, 102400000 bytes / 175518 ms. = 583.42 BYTES/MS,
		// 569.74 K/S, 0.56 MB/S
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
									close(psmt);
									close(connection);
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
		// delete: 10000 rows, 102400000 bytes / 43219 ms. = 2369.33 BYTES/MS,
		// 2313.8 K/S, 2.26 MB/S
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
									close(psmt);
									close(connection);
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
	 * mapping
	 */
	protected class ChengRowMapper implements RowMapper<Cheng> {
		
		public ChengRowMapper() {
		}

		public Cheng mapRow(ResultSet rs, int rowNum) throws SQLException {
			Cheng customer = new Cheng();
			customer.setSeq(rs.getLong("seq"));
			customer.setId(rs.getString("id"));
			customer.setInfo(rs.getString("info"));
			return customer;
		}
	}

	/**
	 * bean
	 */
	protected class Cheng {

		private long seq;

		private String id;

		private String info;

		public Cheng() {
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
