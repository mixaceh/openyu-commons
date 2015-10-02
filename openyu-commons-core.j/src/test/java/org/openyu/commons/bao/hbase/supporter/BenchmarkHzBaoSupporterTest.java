package org.openyu.commons.bao.hbase.supporter;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.regionserver.ConstantSizeRegionSplitPolicy;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.PoolMap;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkHzBaoSupporterTest extends BaseTestSupporter {

	private static HzBaoSupporter hzBaoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-init.xml",//
				"applicationContext-hbase-zookeeper.xml",//
		});
		hzBaoSupporter = (HzBaoSupporter) applicationContext
				.getBean("hzBaoSupporter");
		//
	}

	@Test
	public void hzBaoSupporter() {
		System.out.println(hzBaoSupporter);
		assertNotNull(hzBaoSupporter);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static Configuration createConfiguration() {
		Configuration result = new Configuration();
		// DEV
		// stemktp07,stemktp08,stemktp09,stemktp10
		// 172.22.30.12,172.22.30.13,172.22.30.14,172.22.30.15
		// 這是一個4台集群的daily 日常性能測試環境

		result.set("hbase.zookeeper.quorum",
				"172.22.30.11,172.22.30.12,172.22.30.13,172.22.30.14,172.22.30.15");
		// result.set("hbase.zookeeper.quorum",
		// "172.22.30.12");
		result.set("hbase.zookeeper.property.clientPort", "2181");
		result.setInt("hbase.client.retries.number", 3);
		result.setLong("hbase.client.pause", 1000L);
		//
		// result.setInt("hbase.client.rpc.maxattempts", 1);
		result = HBaseConfiguration.create(result);

		// 提高rpc通信時間
		// result.setLong("hbase.rpc.timeout", 30 * 1000);// 1分鐘
		// 設置scan cache
		// result.setLong("hbase.client.scanner.caching", 200);

		return result;
	}

	public static HTablePool createHTablePool(Configuration configuration,
			int maxSize, PoolMap.PoolType poolType) throws Exception {
		return new HTablePool(configuration, maxSize, poolType);
	}

	@Test
	public void htablePool() throws Exception {
		Configuration configuration = createConfiguration();
		HTablePool htablePool = createHTablePool(configuration, 10,
				PoolMap.PoolType.ThreadLocal);
		System.out.println(htablePool);
		assertNotNull(htablePool);
	}

	public static HBaseAdmin createHBaseAdmin(Configuration configuration)
			throws Exception {
		return new HBaseAdmin(configuration);
	}

	@Test
	public void hbaseAdmin() throws Exception {
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		System.out.println(hbaseAdmin);
		assertNotNull(hbaseAdmin);
	}

	@Test
	public void creatTable() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		HTableDescriptor htd = new HTableDescriptor(TABLE_NAME);
		// coprocessor
		htd.addCoprocessor(AggregateImplementation.class.getName());
		hbaseAdmin.createTable(htd);// TableExistsException
		System.out.println(htd);
		assertNotNull(htd);
	}

	@Test
	public void getTable() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		//
		System.out.println(htd);
		assertNotNull(htd);
	}

	@Test
	public void modifyTable() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));
		//
		HTableDescriptor newHtd = new HTableDescriptor(htd);
		newHtd.setValue(HTableDescriptor.SPLIT_POLICY,
				ConstantSizeRegionSplitPolicy.class.getName());
		//
		boolean disabled = false;
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
			disabled = true;
		}
		//
		hbaseAdmin.modifyTable(Bytes.toBytes(TABLE_NAME), newHtd);
		//
		if (disabled) {
			hbaseAdmin.enableTable(TABLE_NAME);
		}
		//
		System.out.println(newHtd);
	}

	@Test
	public void deleteTable() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}
		//
		hbaseAdmin.deleteTable(TABLE_NAME); // TableNotFoundException
		//
		System.out.println("delete table");
	}

	@Test
	public void addColumnFamily() throws Exception {
		String TABLE_NAME = "TEST_CHENG";
		//
		Configuration configuration = createConfiguration();
		HBaseAdmin hbaseAdmin = createHBaseAdmin(configuration);
		//
		// 新增column時,須將table disable
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}
		//
		String COLUMN_FAMILY = "seq";
		HColumnDescriptor hcd = new HColumnDescriptor(COLUMN_FAMILY);
		// hcd.setBloomFilterType(BloomType.ROWCOL);
		hcd.setBlockCacheEnabled(true);
		// hcd.setCompressionType(Algorithm.SNAPPY);
		hcd.setInMemory(true);
		hcd.setMaxVersions(1);
		hcd.setMinVersions(0);
		hcd.setTimeToLive(432000);// 秒為單位
		hbaseAdmin.addColumn(TABLE_NAME, hcd);// TableNotDisabledException,
		System.out.println("add column family [" + COLUMN_FAMILY + "]");
		//
		COLUMN_FAMILY = "id";
		hcd = new HColumnDescriptor(COLUMN_FAMILY);
		// hcd.setBloomFilterType(BloomType.ROWCOL);
		hcd.setBlockCacheEnabled(true);
		// hcd.setCompressionType(Algorithm.SNAPPY);
		hcd.setInMemory(true);
		hcd.setMaxVersions(1);
		hcd.setMinVersions(0);
		hcd.setTimeToLive(432000);// 秒為單位,5d
		hbaseAdmin.addColumn(TABLE_NAME, hcd);// TableNotDisabledException,
		System.out.println("add column family [" + COLUMN_FAMILY + "]");
		//
		COLUMN_FAMILY = "info";
		hcd = new HColumnDescriptor(COLUMN_FAMILY);
		// hcd.setBloomFilterType(BloomType.ROWCOL);
		hcd.setBlockCacheEnabled(true);
		// hcd.setCompressionType(Algorithm.SNAPPY);
		hcd.setInMemory(true);
		hcd.setMaxVersions(1);
		hcd.setMinVersions(0);
		hcd.setTimeToLive(432000);// 秒為單位
		hbaseAdmin.addColumn(TABLE_NAME, hcd);// TableNotDisabledException
		System.out.println("add column family [" + COLUMN_FAMILY + "]");

		// 新增column後,再將table enable
		if (hbaseAdmin.isTableDisabled(TABLE_NAME)) {
			hbaseAdmin.enableTable(TABLE_NAME);
		}
	}

	/**
	 * 列印Result
	 *
	 * @param result
	 */
	public static void printlnResult(Result result) {
		if (result == null) {
			System.out.println(result);
			return;
		}
		//
		StringBuilder buff = new StringBuilder();
		List<KeyValue> values = result.list();
		if (values == null) {
			System.out.println(values);
			return;
		}
		int size = values.size();
		//
		buff.append(ByteHelper.toString(result.getRow()) + ", ");// rowKey
		int i = 0;
		for (KeyValue kv : values) {
			buff.append(ByteHelper.toString(kv.getFamily()));
			buff.append(":");
			buff.append(ByteHelper.toString(kv.getQualifier()));
			buff.append("=");
			buff.append(ByteHelper.toString(kv.getValue()));
			//
			if (i < size - 1) {
				buff.append(", ");
			}
			i++;
		}
		System.out.println(buff.toString());
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkHzBaoSupporterTest {

		@Test
		// put(i): 9269 rows, 94914560 bytes / 215514 ms. = 440.41 BYTES/MS,
		// 430.09 K/S, 0.42 MB/S
		//
		// org.apache.hadoop.hbase.client.RegionOfflineException
		// 寫入時正好處於一個Region發生split時, 造成沒有完全寫入資料
		public void nativePut_Insert() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			Configuration configuration = createConfiguration();
			final HTablePool htablePool = createHTablePool(configuration, 100,
					PoolMap.PoolType.ThreadLocal);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								HTableInterface table = null;
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									long timestamp = System.currentTimeMillis();
									//
									Put put = new Put(ByteHelper
											.toByteArray(ROW_KEY));

									// seq
									put.add(ByteHelper.toByteArray("seq"),
											null, timestamp,
											ByteHelper.toByteArray(ROW_KEY));

									// id, 0_0
									String newId = userId + "_" + i;
									put.add(ByteHelper.toByteArray("id"), null,
											timestamp,
											ByteHelper.toByteArray(newId));

									// info
									put.add(ByteHelper.toByteArray("info"),
											null, timestamp, buff);

									table = htablePool.getTable(TABLE_NAME);
									table.put(put);

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(ByteHelper
											.toByteArray(ROW_KEY).length);
									byteCounter.addAndGet(ByteHelper
											.toByteArray(newId).length);
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (table != null) {
										table.close();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("put(i): " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// get: 10000 rows, 102400000 bytes / 17696 ms. = 5786.62 BYTES/MS,
		// 5650.99 K/S, 5.52 MB/S
		public void nativeGet() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			Configuration configuration = createConfiguration();
			final HTablePool htablePool = createHTablePool(configuration, 100,
					PoolMap.PoolType.ThreadLocal);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								HTableInterface table = null;
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									//
									Get get = new Get(ByteHelper
											.toByteArray(ROW_KEY));

									table = htablePool.getTable(TABLE_NAME);
									Result result = table.get(get);
									//
									long seq = 0;
									String id = null;
									String info = null;
									if (result != null) {
										byte[] row = result.getRow();
										if (row != null) {
											seq = ByteHelper.toLong(row);
											id = new String(result.getValue(
													ByteHelper
															.toByteArray("id"),
													null));
											info = new String(
													result.getValue(
															ByteHelper
																	.toByteArray("info"),
															null));
										}
									}
									System.out.println("I[" + userId + "] R["
											+ i + "], " + seq);
									//
									if (seq > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper
												.toByteArray(seq).length);
										byteCounter.addAndGet(ByteHelper
												.toByteArray(id).length);
										byteCounter.addAndGet(ByteHelper
												.toByteArray(info).length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (table != null) {
										table.close();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("get: " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// put(u): 9772 rows, 100065280 bytes / 217268 ms. = 460.56 BYTES/MS,
		// 449.77 K/S, 0.44 MB/S
		//
		// org.apache.hadoop.hbase.client.RegionOfflineException
		// 寫入時正好處於一個Region發生split時, 造成沒有完全寫入資料
		public void nativePut_Update() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			Configuration configuration = createConfiguration();
			final HTablePool htablePool = createHTablePool(configuration, 100,
					PoolMap.PoolType.ThreadLocal);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								byte[] prefix = ByteHelper
										.toByteArray("UPDATE_");
								buff = ArrayHelper.add(prefix, ByteHelper
										.getByteArray(buff, 0, buff.length
												- prefix.length));
								//
								HTableInterface table = null;
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									long timestamp = System.currentTimeMillis();
									//
									Put put = new Put(ByteHelper
											.toByteArray(ROW_KEY));

									// seq
									put.add(ByteHelper.toByteArray("seq"),
											null, timestamp,
											ByteHelper.toByteArray(ROW_KEY));

									// info
									put.add(ByteHelper.toByteArray("info"),
											null, timestamp, buff);

									table = htablePool.getTable(TABLE_NAME);
									table.put(put);

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (table != null) {
										table.close();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("put(u): " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// delete: 10000 rows, 102400000 bytes / 201840 ms. = 507.33 BYTES/MS,
		// 495.44 K/S, 0.48 MB/S
		public void nativeDelete() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			Configuration configuration = createConfiguration();
			final HTablePool htablePool = createHTablePool(configuration, 100,
					PoolMap.PoolType.ThreadLocal);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								HTableInterface table = null;
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									//
									Delete delete = new Delete(ByteHelper
											.toByteArray(ROW_KEY));
									table = htablePool.getTable(TABLE_NAME);
									table.delete(delete);

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (table != null) {
										table.close();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("delete: " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkHzBaoSupporterTest {

		@Test
		// put(i): 10000 rows, 102400000 bytes / 54120 ms. = 1892.09 BYTES/MS,
		// 1847.75 K/S, 1.8 MB/S
		public void optimizedPut_Insert() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									long timestamp = System.currentTimeMillis();
									//
									Put put = new Put(ByteHelper
											.toByteArray(ROW_KEY));

									// seq
									put.add(ByteHelper.toByteArray("seq"),
											null, timestamp,
											ByteHelper.toByteArray(ROW_KEY));

									// id, 0_0
									String newId = userId + "_" + i;
									put.add(ByteHelper.toByteArray("id"), null,
											timestamp,
											ByteHelper.toByteArray(newId));

									// info
									put.add(ByteHelper.toByteArray("info"),
											null, timestamp, buff);

									hzBaoSupporter.put(TABLE_NAME, put);

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(ByteHelper
											.toByteArray(ROW_KEY).length);
									byteCounter.addAndGet(ByteHelper
											.toByteArray(newId).length);
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("put(i): " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// get: 10000 rows, 102400000 bytes / 22576 ms. = 4535.79 BYTES/MS,
		// 4429.48 K/S, 4.33 MB/S
		public void optimizedGet() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									//
									Get get = new Get(ByteHelper
											.toByteArray(ROW_KEY));
									Result result = hzBaoSupporter.get(
											TABLE_NAME, get);
									//
									long seq = 0;
									String id = null;
									String info = null;
									if (result != null) {
										byte[] row = result.getRow();
										if (row != null) {
											seq = ByteHelper.toLong(row);
											id = new String(result.getValue(
													ByteHelper
															.toByteArray("id"),
													null));
											info = new String(
													result.getValue(
															ByteHelper
																	.toByteArray("info"),
															null));
										}
									}
									System.out.println("I[" + userId + "] R["
											+ i + "], " + seq);
									//
									if (seq > 0) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper
												.toByteArray(seq).length);
										byteCounter.addAndGet(ByteHelper
												.toByteArray(id).length);
										byteCounter.addAndGet(ByteHelper
												.toByteArray(info).length);
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("get: " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// put(u): 10000 rows, 102400000 bytes / 62011 ms. = 1651.32 BYTES/MS,
		// 1612.62 K/S, 1.57 MB/S
		// put(u): 10000 rows, 102400000 bytes / 54563 ms. = 1876.73 BYTES/MS,
		// 1832.74 K/S, 1.79 MB/S
		public void optimizedPut_Update() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								byte[] prefix = ByteHelper
										.toByteArray("UPDATE_");
								buff = ArrayHelper.add(prefix, ByteHelper
										.getByteArray(buff, 0, buff.length
												- prefix.length));
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									long timestamp = System.currentTimeMillis();
									//
									Put put = new Put(ByteHelper
											.toByteArray(ROW_KEY));

									// seq
									put.add(ByteHelper.toByteArray("seq"),
											null, timestamp,
											ByteHelper.toByteArray(ROW_KEY));

									// info
									put.add(ByteHelper.toByteArray("info"),
											null, timestamp, buff);

									hzBaoSupporter.put(TABLE_NAME, put);

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("put(u): " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// delete: 10000 rows, 102400000 bytes / 21133 ms. = 4845.5 BYTES/MS,
		// 4731.94 K/S, 4.62 MB/S
		public void optimizedDelete() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String TABLE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// seq
			final AtomicLong seqCounter = new AtomicLong(1);
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								try {
									long ROW_KEY = seqCounter.getAndIncrement();
									//
									Delete delete = new Delete(ByteHelper
											.toByteArray(ROW_KEY));
									hzBaoSupporter.delete(TABLE_NAME, delete);
									//

									System.out.println("I[" + userId + "] R["
											+ i + "]");
									//
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
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("delete: " + timesCounter.get() + " rows, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}
	}
}
