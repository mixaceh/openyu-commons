package org.openyu.commons.hbase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.PoolMap;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;

public class HTablePoolTest {

	protected static HTablePool htablePool = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Configuration configuration = createConfiguration();
		//
		htablePool = new HTablePool(configuration, 10,
				PoolMap.PoolType.ThreadLocal);
	}

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
			int maxSize) throws Exception {
		return new HTablePool(configuration, maxSize);
	}

	@Test
	public void htablePool() throws Exception {
		System.out.println(htablePool);
		assertNotNull(htablePool);
	}

	@Test
	// 連線->關閉(斷線)->重新連線
	// #issue: 沒效率
	public void getTable() throws Exception {
		String TABLE_NAME = "student";
		Configuration configuration = createConfiguration();

		HTable table = new HTable(configuration, TABLE_NAME);
		System.out.println(table);
		table.close();
		//
		table = new HTable(configuration, TABLE_NAME);
		System.out.println(table);
		table.close();
	}

	@Test
	public void mockGetTable() throws Exception {
		String TABLE_NAME = "student";
		Configuration configuration = createConfiguration();
		//
		int count = 1;
		HTable result = null;
		for (int i = 0; i < count; i++) {
			// unthread safe
			result = new HTable(configuration, TABLE_NAME);
			result.close();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	// #issue 多緒拿到同一條connection, unthread safe
	public void mockGetTableWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetTable();
					} catch (Exception ex) {
					}
				}
			});
			thread.setName("T-" + i);
			thread.start();
			Thread.sleep(NumberHelper.randomInt(100));
		}
		//
		Thread.sleep(1 * 60 * 60 * 1000);
	}

	@Test
	// 連線->關閉(放回pool)->從pool拿出
	public void getTableByTablePool() throws Exception {
		String TABLE_NAME = "student";

		HTableInterface table = htablePool.getTable(TABLE_NAME);
		System.out.println(table);
		table.close();
		//
		table = htablePool.getTable(TABLE_NAME);
		System.out.println(table);
		table.close();
	}

	@Test
	public void mockGetTableByTablePool() throws Exception {
		String TABLE_NAME = "student";
		//
		int count = 1;
		HTableInterface result = null;
		for (int i = 0; i < count; i++) {
			// unthread safe
			result = htablePool.getTable(TABLE_NAME);
			result.close();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	// #issue 多緒拿到同一條connection, unthread safe
	public void mockGetTableWithTablePoolWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetTableByTablePool();
					} catch (Exception ex) {
					}
				}
			});
			thread.setName("T-" + i);
			thread.start();
			Thread.sleep(NumberHelper.randomInt(100));
		}
		//
		Thread.sleep(1 * 60 * 60 * 1000);
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
		buff.append(Bytes.toString(result.getRow()) + ", ");// rowKey
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

	// System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s, timestamp:%s.",
	// Bytes.toString(kv.getRow()),
	// Bytes.toString(kv.getFamily()),
	// Bytes.toString(kv.getQualifier()),
	// Bytes.toString(kv.getValue()),
	// kv.getTimestamp()));

	// 注意一下：Put Get Delete Scan等操作的物件 都提供一個空的構造函數, 一般不要直接使用,
	// 他們存在主要是在rpc傳輸的反序列化的時候要用到(瞭解Java RMI的應該很清楚)

	/**
	 * put
	 *
	 * @throws Exception
	 */
	@Test
	public void put() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException

		// 批量操作table.put(List<Put>),table.batch(List<Row>) 共兩種
		// 底層都是調用 HConnection的processBatch方法(
		// table.batch(List<Put>) 和table.flushCommits()會直接調用)

		// 首先 自動flush 關閉 就像 JDBC中的 auto_commit, 否則 加每一條 提交
		// 一次,影響性能 不過table.put(List<Put>) table.batch(List<Row>)不受這
		// 個影響, 設置false,只有當put總大小超過writeBufferSize 才提交 或者手工
		// table.flushCommits() （table.put(List<Put>)操作完成後會手工提交一次）,
		// writeBufferSize 也可以調整

		// 指的是在每次調用HBase的Put操作，是否提交到HBase
		// Server。默認是true,每次會提交。如果此時是單條插入，就會有更多的IO,從而降低性能.
		// table.setAutoFlush(false);

		// writeBufferSize 預設為2M ,調大可以增加批量處理的輸送量, 丟失資料的風險也會加大
		// table.setWriteBufferSize(1024 * 1024 * 5);
		// 這樣可以看到 當前用戶端緩存了多少put
		// ArrayList<Put> putx = table.getWriteBuffer();

		// 批量操作方法一,單一操作的批量 比如Htable.put delete get 都提供了List作
		// 為參數的批次處理. 預設每10條 或List<Put>資料量 超過writeBufferSize 提交
		// 如果AutoFlush為true 一次性table.put(List<Put>)只提交一次
		List<Put> puts = new ArrayList<Put>(10);

		String ROW_KEY = "Jack";
		long timestamp = System.currentTimeMillis();

		// 一般不要直接使用空的構造函數
		Put put = new Put(ByteHelper.toByteArray(ROW_KEY));
		// 這裡可以自訂添加時間戳記, 預設就是當前時間(RegionServer伺服器端的
		// 時間) 也可以自己定義, 多版本時候(預設version=3)比如想插入一條比現在最新的記
		// 錄老的, 一些特殊情況下可能會有這種需求

		// #1
		// family,qualifier,vaule
		put.add(ByteHelper.toByteArray("id"), null, timestamp,
				ByteHelper.toByteArray(ROW_KEY));// NoSuchColumnFamilyException

		// #2
		// family,qualifier,vaule
		put.add(ByteHelper.toByteArray("grad"), null, timestamp,
				ByteHelper.toByteArray("5"));// NoSuchColumnFamilyException

		// #3
		// 也可以直接加入一個KeyValue,實際上底層就是存儲為KeyValue的
		// 如果對底層較熟悉, 這種操行更加高效, 一般上面的就可以完成日常工作了
		KeyValue kv = new KeyValue(ByteHelper.toByteArray(ROW_KEY),
				ByteHelper.toByteArray("course"),
				ByteHelper.toByteArray("math"), timestamp,
				ByteHelper.toByteArray("20"));
		put.add(kv);

		// #4
		// family,qualifier,timestamp,vaule
		put.add(ByteHelper.toByteArray("course"),
				ByteHelper.toByteArray("art"), timestamp,
				ByteHelper.toByteArray("87"));// NoSuchColumnFamilyException
		puts.add(put);

		// 寫操作日誌 這個對性能影響比較大, 但有很重要, 如果設為true, 只要寫
		// 成功, 就算機器掛掉 也不會丟失,預設=true
		put.setWriteToWAL(true);

		// put 當前在記憶體中的大小 這個在setWriteBufferSize 可能會用到
		System.out.println("heapSize: " + put.heapSize());

		// put 中 每次調用add 底層都會添加一個KeyValue,這個是添加的KeyValue數量
		System.out.println("size: " + put.size());

		/**
		 * 一些需要注意的地方：
		 *
		 * 1. 提交到伺服器 處理如果出現問題 會從伺服器端返回RetriesExhaustedWithDetailsException
		 * 包含出錯的原因 和重試的次數 如果 伺服器端還是操作失敗 , 這些put還會緩存在用戶端 等到下次buffer 被flush, 注意
		 * 如果用戶端掛掉了 這些資料是會丟失的 當然如果是NoSuchColumnFamilyException只會重試一次 並且不會恢復
		 * 下面的情況要注意了 table.put(puts); 是會拋出異常的,而且不會再提交 這樣資料會丟失的
		 * 捕獲這個異常手工table.flushCommits() 可以確保已經寫入緩存的還可以有可能寫入成功
		 * table.flushCommits(); 也會有異常也要捕獲
		 *
		 * 2. 還時候 啟用緩存 正常操作發生異常時候並不會被正常報出來, 有時候 會等到buffer被flush後才報出來 這也是要注意的地方
		 *
		 * 3.在緩存中的puts 被發送到伺服器端的順序和伺服器處理的順序是控制不到的, 如果想指定順序 , 只能使用較小的批次處理
		 * 強制他們按照批次處理的循序執行
		 */

		long beg = System.currentTimeMillis();
		try {
			table.put(puts);// table.put(puts)需手工提交table.flushCommits()
			if (!table.isAutoFlush()) {
				table.flushCommits();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				if (!table.isAutoFlush()) {
					table.flushCommits();
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");

		/**
		 * 完備的一條記錄就是一個KeyValue 一個rowkey可能有多個KeyValue（比如 多個版本, 一個版本是一條） rowkey
		 * ColumnFamily Column TimeStamp Type Value 其中的Type就是區別Put和Delete等操作的類別,
		 * 實際上Delete也是添加一條記錄 （Hbase存儲的HDFS檔是唯讀的, 更新用 添加+刪除 組合完成, 刪除實際上
		 * 也是添加一條刪除,實際操作都是添加,在Hbase Compact時候 合併資料時候會剔除標記為刪除的rowkey） 這種 增 改
		 * 刪的一致性操作 在用戶端給我們的操作帶來了便利
		 *
		 * 實際上ColumnFamily Column的名字是會以byte的形式存儲在資料中的, 因此, 它們在設計的時候名字應該盡可能的短
		 * 這樣可以節省不少的空間
		 */
		//
		Result result = table.get(new Get(Bytes.toBytes(ROW_KEY)));

		printlnResult(result);
		//
		result = table.get(new Get(Bytes.toBytes(ROW_KEY)));
		printlnResult(result);
	}

	@Test
	public void batchPutDelete() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// table.setAutoFlush(false);

		// 批量操作方法一, 使用batch,可以混合各種操作 ( Put Delete Get 都是介面Row的實現)
		// 主要 這個如果處理Put操作 是不會使用用戶端緩存的 會直接非同步的發送到伺服器端
		List<Row> rows = new ArrayList<Row>(10);
		int count = 10;
		for (int i = 0; i < count; i++) {
			Put put = new Put(Bytes.toBytes(("batch-" + i)));
			put.add(Bytes.toBytes("course"), Bytes.toBytes("math"),
					Bytes.toBytes(String.valueOf(i)));
			put.add(Bytes.toBytes("course"), Bytes.toBytes("art"),
					Bytes.toBytes(String.valueOf(i)));
			rows.add(put);
		}
		// 可以添加刪除操作 但是 最好不要把對同一行的Put, Delete用batch操作 ,
		// 因為為了更好的性能 發到伺服器端操作的順序,是會改變的,很有可能不是放入的順序
		Delete delete = new Delete(Bytes.toBytes("batch-0"));
		rows.add(delete);

		// 注意:這只是為了測試,將put,delete放在同各batch操作,最好不要這樣做
		long beg = System.currentTimeMillis();
		table.batch(rows);// table.batch(rows)不需手工提交table.flushCommits()
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");

		// 看吧,刪了還在,很有可能不是放入的順序
		Result result = table.get(new Get(Bytes.toBytes("batch-0")));
		printlnResult(result);
	}

	/**
	 * get 讀取所有column
	 *
	 * @throws Exception
	 */
	@Test
	public void get() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		String ROW_KEY = "Jack";
		// 讀取所有column
		Get get = new Get(ByteHelper.toByteArray(ROW_KEY));
		//
		long beg = System.currentTimeMillis();
		Result result = table.get(get);
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		//
		printlnResult(result);
	}

	/**
	 * get 讀取特定column
	 *
	 * @throws Exception
	 */
	@Test
	public void getOneColumn() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		String ROW_KEY = "004qp00004;qFIGB20461";

		// 讀取2個column
		Get get = new Get(ByteHelper.toByteArray(ROW_KEY));
		// 預設 get 只會取得最新的記錄, 使用下面的方法可以獲取其他的版本
		// 有兩個方法 一個帶參數的可以指定版本數量, 可能會拋出異常;另外一個沒有
		// 參數, 默認Integer.MAX_VALUE, 不會拋出異常
		// get.setMaxVersions();

		// get.setFilter(filter); get 一般資料比較少比較少使用filter, 在Scan的時候會詳細介紹Filter
		// 通過get.addColumn提供了各種重載方法, 可以過濾只獲取哪些ColumnFamily
		// 和Column,get實現這種過濾只能使用這種方法, 接下來的Scan還可以使用Filter來實現
		//
		get.addFamily(ByteHelper.toByteArray("grad"));

		long beg = System.currentTimeMillis();
		Result result = table.get(get);
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		//
		printlnResult(result);
	}

	@Test
	public void getNavigableMap() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		String ROW_KEY = "004qp00004;qFIGB20461";
		// 讀取所有column
		Get get = new Get(ByteHelper.toByteArray(ROW_KEY));
		//
		long beg = System.currentTimeMillis();
		Result result = table.get(get);
		// 這是另外一種獲取返回結果的方式, 這種在Scan的返回多個Result的時候
		// 相對實用, 一個rowkey的都在一起, 一個ColumnFamily的也聚合在一起
		NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> nMap = result
				.getMap();
		System.out.print(Bytes.toString(result.getRow()) + ", ");
		for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : nMap
				.entrySet()) {
			// entry.getKey()為family key
			String family = Bytes.toString(entry.getKey());
			System.out.print(family + ":");
			for (Map.Entry<byte[], NavigableMap<Long, byte[]>> entry2 : entry
					.getValue().entrySet()) {
				// entry2.getKey()為qualifier 當然qualifier有可能為空 這個不是問題
				// 但為null的只能有一個
				String qualifier = Bytes.toString(entry2.getKey());
				System.out.print(qualifier + "=");
				for (Map.Entry<Long, byte[]> entry3 : entry2.getValue()
						.entrySet()) {
					// entry3.getKey()為 timestamp
					// entry3.getValue()為 value
					System.out.print(Bytes.toString(entry3.getValue()) + "; ");
				}
			}
		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}

	/**
	 * delete
	 *
	 * Delete與Put一致 把全部的Put改成Delete table.put -->table.delete 就可以了, 不過有些需要注意,
	 * 看下面
	 *
	 * @throws Exception
	 */
	@Test
	public void delete() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException

		List<Delete> deletes = new LinkedList<Delete>();
		String ROW_KEY = "Jack";

		// 如果上面介紹的KeyValue 有點印象, 通過delete提供的構造函數可以知道
		// 不指定會刪除所有的版本
		Delete delete = new Delete(ByteHelper.toByteArray(ROW_KEY));
		delete.deleteColumn(ByteHelper.toByteArray("id"),
				ByteHelper.toByteArray(""));
		deletes.add(delete);
		//
		long beg = System.currentTimeMillis();
		// 用批次delete
		table.delete(deletes);// NoSuchColumnFamilyException
		long end = System.currentTimeMillis();

		// 若用單各delete
		// table.delete(delete);// NoSuchColumnFamilyException
		// table.flushCommits();
		//
		System.out.println((end - beg) + " at mills.");
	}

	@Test
	public void deleteMutiple() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException

		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		FirstKeyOnlyFilter firstKeyFilter = new FirstKeyOnlyFilter();
		filterList.addFilter(firstKeyFilter);
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setFilter(filterList);

		long beg = System.currentTimeMillis();
		int count = 100;
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			byte[] row = result.getRow();
			Delete delete = new Delete(row);
			table.delete(delete);
			//
			rowCount++;
			if (rowCount >= count) {
				break;
			}
		}
		scanner.close();// 一定要關閉
		System.out.println("rowCount: " + rowCount);
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
	}

	/**
	 * 一些原子性操作 對於java併發工具包有所瞭解的 應該會知道 輕量級鎖的核心就是CAS機制(Compare and swap),
	 * 這裡在概念上有些類似, 也可以類似於 SQL中 select 出來然後 insert or update的 操作
	 * Hbase這裡可以保證他們在一個原子操作 這個在高併發 場景下 更新值 是個好的選擇 table.checkAndPut(row, family,
	 * qualifier, value, put) table.checkAndDelete(row, family, qualifier,
	 * value, delete)
	 *
	 * @throws Exception
	 */
	@Test
	public void checkAndPut() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		String ROW_KEY = "Jack";

		// 操作成功會返回 true,否則false; 如果是個不存在的qualifier, 把value置為null check是會成功的
		byte[] row = ByteHelper.toByteArray(ROW_KEY);
		byte[] family = Bytes.toBytes("course");
		byte[] qualifier = Bytes.toBytes("math");

		Put put = new Put(row);
		put.add(family, qualifier, ByteHelper.toByteArray("99"));// NoSuchColumnFamilyException

		// checkAndPut(byte row, byte family, byte qualifier, byte value, Put
		// put)
		// check一下，如果改列有沒有值，則執行put操作。
		boolean result1 = table.checkAndPut(row, family, qualifier, null, put);
		System.out.println(result1);// true

		// 再次put一條同樣的記錄
		boolean result2 = table.checkAndPut(row, family, qualifier, null, put);
		System.out.println(result2);// false

		Put put2 = new Put(row);
		put2.add(family, qualifier, ByteHelper.toByteArray("10"));

		// check一下，如果之前99錄入成功，則錄入新值
		boolean result3 = table.checkAndPut(row, family, qualifier,
				ByteHelper.toByteArray("99"), put2);

		// 輸出執行結果，看是否執行了Put
		System.out.println(result3);// true

		byte[] row2 = ByteHelper.toByteArray("Jack02");
		Put put3 = new Put(row2);
		put3.add(family, qualifier, Bytes.toBytes("20"));

		// check一下，row1的值是否存在，如果存在則插入row2
		// 會拋出異常(org.apache.hadoop.hbase.DoNotRetryIOException: Action's getRow
		// must match the passed row)
		boolean result4 = false;
		try {
			result4 = table.checkAndPut(row, family, qualifier,
					Bytes.toBytes("10"), put3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 輸出執行結果，看是否執行了Put
		System.out.println(result4);

		// 可以看到我們第一次check庫裡的Jack:course:math是否有值，結果是沒有，成功插入資料Jack:course:math:99。
		//
		// 第二次做了與第一次同樣的操作，因為剛才已經插入。Jack:course:math:99，所以這次check不通過，沒能插入資料。
		//
		// 第三次new了另一個put物件，插入資料Jack:course:math:10，插入之前check
		// Jack:course:math:99，同樣可以成功插入資料。
		//
		// 第四次new了一個put物件，插入資料Jack02:course:math:20，先check
		// Jack:course:math:10，但這時拋出異常org.apache.hadoop.hbase.DoNotRetryIOException:
		// Action's getRow must match the passed
		// row，可以看出checkAndPut和其他dml操作一樣，都屬於行級原子操作，只對單行有效。
	}

	/**
	 * 上面的一些操作有些方法可能涉及到Row Locks 但並沒有說明 這裡詳細介紹下
	 *
	 * 一些會使資料發生變化的操作 比如like put(), delete(), checkAndPut()等等 , 操作都是以一個row為單位的,
	 * 使用row lock 可以保證 一次性只能有一個用戶端修改一個row 雖然 實踐中 用戶端應用程式 並沒有明確的使用lock,
	 * 但服務端會在適當的時機保護每一個獨立的操作
	 *
	 * 如果可能應當儘量避免使用lock, 就像RSBMS一樣會有鎖死問題
	 *
	 * @throws IOException
	 */
	// @Test
	// public void lockRow() throws Exception {
	// String TABLE_NAME = "student";
	// HTableInterface table = tables.getTable(TABLE_NAME);//
	// TableNotFoundException
	// String ROW_KEY = "Jack";
	//
	// byte[] row = ByteHelper.toByteArray(ROW_KEY);
	// RowLock lock = table.lockRow(row);
	// // .....相關操作
	// table.unlockRow(lock);
	// // 鎖有效時間 默認時間是1分鐘
	// }

	/**
	 * scan 計數
	 *
	 * @throws Exception
	 */
	@Test
	// hbase.client.scanner.caching=1
	// 54236 at mills.
	// 55341 at mills.
	// 53698 at mills.
	//
	// hbase.client.scanner.caching=200
	// 833 at mills.
	// 825 at mills.
	// 898 at mills.
	public void scan() {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		ResultScanner scanner = null;
		int rowCount = 0;
		long beg = System.currentTimeMillis();
		try {
			scanner = table.getScanner(scan);
			for (Result result : scanner) {
				// printlnResult(result);
				rowCount++;

				// 太多筆會很慢,先算個50就好
				if (rowCount > 49) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();// 一定要關閉
			}
		}

		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	/**
	 * scan 讀取特定column
	 *
	 * @throws Exception
	 */
	@Test
	// 54236 at mills.
	// 55341 at mills.
	// 53698 at mills.
	public void scanOneColumn() {
		String TABLE_NAME = "student";
		String rowKey = "004qp00004;qFIGB20461";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		scan.setStartRow(ByteHelper.toByteArray(rowKey));
		scan.addFamily(ByteHelper.toByteArray("grad"));
		// scan.addColumn(Bytes.toBytes("course"), Bytes.toBytes("math"));
		//
		ResultScanner scanner = null;
		int rowCount = 0;
		long beg = System.currentTimeMillis();
		try {
			scanner = table.getScanner(scan);
			for (Result result : scanner) {
				printlnResult(result);
				rowCount++;
				//
				if (rowCount > 9) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);

	}

	/**
	 * scan by next with object hbase mapping (OHM)
	 *
	 * @throws Exception
	 */
	@Test
	public void scanMapping() throws Exception {
		String TABLE_NAME = "student";
		//
		int count = 10;
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		int i = 0;
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		for (Result result : scanner) {
			StringBuilder buff = new StringBuilder();
			buff.append("orig: " + new String(result.getRow()) + ", ");
			//
			Student student = new Student();
			for (KeyValue entry : result.raw()) {
				String cfkey = new String(entry.getFamily()) + ":"
						+ new String(entry.getQualifier());
				buff.append(cfkey + "=");
				buff.append(new String(entry.getValue()) + ", ");
				// Jack, course:art=87, course:math=97, grad:=5,
				//
				student.setKey(new String(entry.getRow()));
				//
				Field field = null;
				String fieldName = new String(entry.getQualifier());
				if (fieldName.length() > 0) {
					field = student.getClass().getDeclaredField(fieldName);
				} else {
					fieldName = new String(entry.getFamily());
					try {

						field = student.getClass().getDeclaredField(fieldName);
					} catch (Exception ex) {
						System.err.println("field name: " + fieldName
								+ " is not exist");
					}
				}
				// System.out.println(fieldName + " " + field);
				if (field == null) {
					continue;
				}
				//
				boolean origAccessible = field.isAccessible();
				if (!origAccessible) {
					field.setAccessible(true);
				}
				//
				field.set(student, new String(entry.getValue()));
				field.setAccessible(origAccessible);

				// 時間戳
				// entry.getTimestamps().put(field.getName(),
				// kv.getTimestamp());
			}
			System.out.println(buff);
			System.out.println("entry: " + student);
			//
			i++;
			if (i >= count) {
				break;
			}
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}

	@Test
	// hbase.client.scanner.caching=1
	// 609 at mills.
	// 645 at mills.
	// 643 at mills.
	//
	// hbase.client.scanner.caching=200
	// 191 at mills.
	// 207 at mills.
	// 190 at mills.
	//
	// rowCount: 19251
	// 1677 at mills.
	public void singleColumnValueFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// 注意: 只有當COLUMNS中包含SingleColumnValueFilter提到的欄位時,
		// 該SingleColumnValueFilter才有效的

		// teacherId
		SingleColumnValueFilter columnFilter = new SingleColumnValueFilter(
				ByteHelper.toByteArray("teacherId"),
				ByteHelper.toByteArray(""), CompareOp.GREATER_OR_EQUAL,
				ByteHelper.toByteArray("5"));
		// new SubstringComparator("55")
		columnFilter.setLatestVersionOnly(true);
		// columnFilter.setFilterIfMissing(true);
		filterList.addFilter(columnFilter);
		//
		columnFilter = new SingleColumnValueFilter(
				ByteHelper.toByteArray("teacherId"),
				ByteHelper.toByteArray(""), CompareOp.LESS_OR_EQUAL,
				ByteHelper.toByteArray("9"));
		// new SubstringComparator("55")
		columnFilter.setLatestVersionOnly(true);
		// columnFilter.setFilterIfMissing(true);
		filterList.addFilter(columnFilter);

		// id
		columnFilter = new SingleColumnValueFilter(
				ByteHelper.toByteArray("id"), ByteHelper.toByteArray(""),
				CompareOp.GREATER_OR_EQUAL, ByteHelper.toByteArray(""));
		columnFilter.setLatestVersionOnly(true);
		// columnFilter.setFilterIfMissing(true);
		filterList.addFilter(columnFilter);
		//
		columnFilter = new SingleColumnValueFilter(
				ByteHelper.toByteArray("id"), ByteHelper.toByteArray(""),
				CompareOp.LESS_OR_EQUAL, ByteHelper.toByteArray("9999999999"));
		columnFilter.setLatestVersionOnly(true);
		// columnFilter.setFilterIfMissing(true);
		filterList.addFilter(columnFilter);

		// grad
		columnFilter = new SingleColumnValueFilter(
				ByteHelper.toByteArray("grad"), ByteHelper.toByteArray(""),
				CompareOp.GREATER_OR_EQUAL, ByteHelper.toByteArray("3"));
		columnFilter.setLatestVersionOnly(true);
		// columnFilter.setFilterIfMissing(true);
		filterList.addFilter(columnFilter);
		//
		Scan scan = new Scan();
		scan.setCaching(200);
		scan.setFilter(filterList);
		scan.addFamily(ByteHelper.toByteArray("teacherId"));
		scan.addFamily(ByteHelper.toByteArray("id"));
		scan.addFamily(ByteHelper.toByteArray("grad"));
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			printlnResult(result);
			rowCount++;
			//
			if (rowCount > 0) {
				break;
			}
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 2515 at mills.
	// rowCount: 31815
	// rowKeyScan > rowFilter > singleColumnValueFilter
	public void rowKeyScan() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setStartRow(ByteHelper.toByteArray(""));
		scan.setStopRow(ByteHelper.toByteArray("9999999999"));
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		// for (Result result = scanner.next(); result != null; result =
		// scanner
		// .next()) {
		for (Result result : scanner) {
			// printlnResult(result);
			//
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 2274 at mills.
	// rowCount: 31815
	public void rowFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		RowFilter rowFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				new BinaryPrefixComparator(ByteHelper.toByteArray("")));
		filterList.addFilter(rowFilter);

		rowFilter = new RowFilter(
				CompareOp.LESS_OR_EQUAL,
				new BinaryPrefixComparator(ByteHelper.toByteArray("9999999999")));
		filterList.addFilter(rowFilter);
		//
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setFilter(filterList);
		scan.addFamily(ByteHelper.toByteArray("grad"));
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			// printlnResult(result);
			//
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 13567 at mills.
	// rowCount: 213956
	public void prefixFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		PrefixFilter prefixFilter = new PrefixFilter(ByteHelper.toByteArray(""));
		filterList.addFilter(prefixFilter);
		//
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setFilter(filterList);
		scan.addFamily(ByteHelper.toByteArray("grad"));
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			// printlnResult(result);
			//
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 189788 at mills.
	// rowCount: 213956
	public void pageFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		PageFilter pageFilter = new PageFilter(20);
		filterList.addFilter(pageFilter);
		//
		long beg = System.currentTimeMillis();
		final byte[] POSTFIX = new byte[] { 0x00 };
		byte[] lastRow = null;
		int rowCount = 0;
		while (true) {
			Scan scan = new Scan();
			scan.setCaching(200);
			scan.setFilter(filterList);
			if (lastRow != null) {
				// 注意這裡添加了POSTFIX操作，不然就死循環了
				byte[] startRow = Bytes.add(lastRow, POSTFIX);
				scan.setStartRow(startRow);
			}
			ResultScanner scanner = table.getScanner(scan);
			int localRows = 0;
			Result result;
			while ((result = scanner.next()) != null) {
				localRows++;
				rowCount++;
				lastRow = result.getRow();
				// printlnResult(result);
			}
			scanner.close();
			if (localRows == 0) {
				break;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	/**
	 * 只傳回key值,value屏蔽掉
	 *
	 * @throws Exception
	 */
	@Test
	// rowCount: 8321
	// 1193 at mills.
	public void keyOnlyFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();
		filterList.addFilter(keyOnlyFilter);

		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setFilter(filterList);
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			// printlnResult(result);
			//
			rowCount++;
			//
			if (rowCount > 9) {
				break;
			}
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	/**
	 * 只傳回第一個keyValue
	 *
	 * @throws Exception
	 */
	@Test
	// rowCount: 8321
	// 1152 at mills.
	public void firstKeyOnlyFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		FirstKeyOnlyFilter firstKeyFilter = new FirstKeyOnlyFilter();
		filterList.addFilter(firstKeyFilter);

		Scan scan = new Scan();
		scan.setCaching(200);
		//
		scan.setFilter(filterList);
		//
		long beg = System.currentTimeMillis();
		ResultScanner scanner = table.getScanner(scan);
		int rowCount = 0;
		for (Result result : scanner) {
			// printlnResult(result);
			//
			rowCount++;
			//
			if (rowCount > 9) {
				break;
			}
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// hbase shell: count 'student', CACHE => 1000
	public void rowCountByNoCaching() throws Exception {
		String TABLE_NAME = "student";
		//
		long beg = System.currentTimeMillis();
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		int rowCount = 0;
		Scan scan = new Scan();

		// scan.setCaching(200);

		ResultScanner scanner = table.getScanner(scan);
		for (Result result : scanner) {
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 7894 at mills.
	// 7942 at mills.
	// 7910 at mills.
	public void rowCountByCaching() throws Exception {
		String TABLE_NAME = "student";
		//
		long beg = System.currentTimeMillis();
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		int rowCount = 0;
		Scan scan = new Scan();
		//
		scan.setCaching(200);
		//
		ResultScanner scanner = table.getScanner(scan);
		for (Result result : scanner) {
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// coprocessor
	// 228 at mills.
	// 329 at mills.
	// 339 at mills.
	public void rowCountByCoprocessor() throws Throwable {
		String TABLE_NAME = "student";
		//
		Configuration configuration = createConfiguration();
		AggregationClient client = new AggregationClient(configuration);
		Scan scan = new Scan();
		String COLUMN_FAMILY = "id";
		scan.addFamily(ByteHelper.toByteArray(COLUMN_FAMILY));
		//
		long rowCount = 0;
		long beg = System.currentTimeMillis();
		int count = 1;
		for (int i = 0; i < count; i++) {
			rowCount = client.rowCount(ByteHelper.toByteArray(TABLE_NAME),
					null, scan);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// 7487 at mills.
	// 7461 at mills.
	// 7492 at mills.
	public void rowCountByKeyFilter() throws Exception {
		String TABLE_NAME = "student";
		//
		long beg = System.currentTimeMillis();
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		// filterList
		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		// filter
		FirstKeyOnlyFilter firstKeyFilter = new FirstKeyOnlyFilter();
		filterList.addFilter(firstKeyFilter);

		// filter
		KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();
		filterList.addFilter(keyOnlyFilter);

		int rowCount = 0;
		Scan scan = new Scan();
		//
		scan.setCaching(200);
		scan.setFilter(filterList);
		ResultScanner scanner = table.getScanner(scan);
		for (Result result : scanner) {
			// printlnResult(result);
			rowCount++;
		}
		scanner.close();// 一定要關閉
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	public static class Student implements Serializable {

		private static final long serialVersionUID = -5918408712731196699L;
		private String key;
		private String grad;
		private String math;
		private String art;

		//
		// private Map<String, Long> timestamps = new LinkedHashMap<String,
		// Long>();

		public Student() {
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getGrad() {
			return grad;
		}

		public void setGrad(String grad) {
			this.grad = grad;
		}

		public String getMath() {
			return math;
		}

		public void setMath(String math) {
			this.math = math;
		}

		public String getArt() {
			return art;
		}

		public void setArt(String art) {
			this.art = art;
		}

		// public Map<String, Long> getTimestamps() {
		// return timestamps;
		// }
		//
		// public void setTimestamps(Map<String, Long> timestamps) {
		// this.timestamps = timestamps;
		// }

		public String toString() {
			// return key + ", " + art + ", " + math + ", " + grad + ", "
			// + timestamps;
			return key + ", " + art + ", " + math + ", " + grad;
		}
	}

	/**
	 * crud
	 *
	 * @throws Exception
	 */
	@Test
	public void crud() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			String ROW_KEY = "TEST_STUDENT_"
					+ NumberHelper.randomInt(count * count);
			long timestamp = System.currentTimeMillis();

			List<Put> puts = new LinkedList<Put>();

			// insert
			Put put = new Put(ByteHelper.toByteArray(ROW_KEY));
			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("grad"), null, timestamp, ByteHelper
					.toByteArray(String.valueOf(NumberHelper.randomInt(10))));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("course"), ByteHelper
					.toByteArray("math"), timestamp, ByteHelper
					.toByteArray(String.valueOf(NumberHelper.randomInt(100))));// NoSuchColumnFamilyException
			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("course"), ByteHelper
					.toByteArray("art"), timestamp, ByteHelper
					.toByteArray(String.valueOf(NumberHelper.randomInt(100))));// NoSuchColumnFamilyException

			puts.add(put);
			table.put(puts);
			System.out.println("insert: " + ROW_KEY);

			// retrive
			Get get = new Get(ByteHelper.toByteArray(ROW_KEY));
			Result findPut = table.get(get);
			String findRowKey = new String(findPut.getRow());
			assertEquals(ROW_KEY, findRowKey);
			System.out.print("retrive: ");
			printlnResult(findPut);

			// update
			puts = new LinkedList<Put>();
			put = new Put(ByteHelper.toByteArray(ROW_KEY));
			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("course"), ByteHelper
					.toByteArray("math"), timestamp, ByteHelper
					.toByteArray(String.valueOf(NumberHelper.randomInt(100))));// NoSuchColumnFamilyException
			puts.add(put);
			//
			table.put(puts);
			System.out.print("update: ");
			findPut = table.get(get);
			printlnResult(findPut);

			// delete
			List<Delete> deletes = new LinkedList<Delete>();
			Delete delete = new Delete(ByteHelper.toByteArray(ROW_KEY));
			delete.setTimestamp(timestamp);
			deletes.add(delete);
			//
			table.delete(deletes);
			System.out.println("delete: " + ROW_KEY);

		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}

	@Test
	public void mockPut() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		//
		int rowCount = 1000;
		long beg = System.currentTimeMillis();
		String spliter = StringHelper.SEMICOLON;
		for (int i = 0; i < rowCount; i++) {
			// pifyt00004;P7AJK00001
			String ROW_KEY = "";

			// txId,JG5aXV25uv0000000009
			String txId = StringHelper.randomString(10)
					+ StringHelper.leftPad(
							String.valueOf(NumberHelper.randomInt(1, 50)), 10,
							"0");
			// ROW_KEY += txId;

			// teacherId,pifyt00004
			String teacherId = StringHelper.randomString(5)
					+ StringHelper.leftPad(
							String.valueOf(NumberHelper.randomInt(1, 5)), 5,
							"0");
			// ROW_KEY += StringHelper.SEMICOLON;
			ROW_KEY += teacherId;

			// id,P7AJK00001
			String id = StringHelper.randomString(5)
					+ StringHelper.leftPad(String.valueOf(NumberHelper
							.randomInt(1, rowCount * 5)), 5, "0");
			ROW_KEY += spliter;
			ROW_KEY += id;
			//
			long timestamp = System.currentTimeMillis();

			List<Put> puts = new LinkedList<Put>();

			// insert
			Put put = new Put(ByteHelper.toByteArray(ROW_KEY));
			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("id"), null, timestamp,
					ByteHelper.toByteArray(id));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String grad = String.valueOf(NumberHelper.randomInt(10));
			put.add(ByteHelper.toByteArray("grad"), null, timestamp,
					ByteHelper.toByteArray(grad));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String math = String.valueOf(NumberHelper.randomInt(100));
			put.add(ByteHelper.toByteArray("course"),
					ByteHelper.toByteArray("math"), timestamp,
					ByteHelper.toByteArray(math));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String art = String.valueOf(NumberHelper.randomInt(100));
			put.add(ByteHelper.toByteArray("course"),
					ByteHelper.toByteArray("art"), timestamp,
					ByteHelper.toByteArray(art));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("teacherId"), null, timestamp,
					ByteHelper.toByteArray(teacherId));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("txId"), null, timestamp,
					ByteHelper.toByteArray(txId));// NoSuchColumnFamilyException

			put.setWriteToWAL(true);// wal log
			puts.add(put);
			table.put(puts);
			System.out.println(i + " insert: " + ROW_KEY);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	public void mockDelete() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		ResultScanner scanner = null;
		int rowCount = 0;
		long beg = System.currentTimeMillis();
		try {
			scanner = table.getScanner(scan);
			for (Result result : scanner) {
				// printlnResult(result);
				List<Delete> deletes = new LinkedList<Delete>();
				Delete delete = new Delete(result.getRow());
				delete.setWriteToWAL(true);// wal log
				deletes.add(delete);
				table.delete(deletes);
				//
				rowCount++;
				System.out.println(rowCount + " delete: "
						+ ByteHelper.toString(result.getRow()));

				if (rowCount > 999) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	public void mockUpdate() throws Exception {
		String TABLE_NAME = "student";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		Scan scan = new Scan();
		scan.setCaching(200);
		//
		ResultScanner scanner = null;
		int rowCount = 0;
		long beg = System.currentTimeMillis();
		try {
			scanner = table.getScanner(scan);
			for (Result result : scanner) {
				long timestamp = System.currentTimeMillis();
				// printlnResult(result);
				List<Put> puts = new LinkedList<Put>();
				// 重複update次數
				for (int i = 0; i < 5; i++) {
					Put put = new Put(result.getRow());
					String grad = String.valueOf(NumberHelper.randomInt(10));
					put.add(ByteHelper.toByteArray("grad"), null, timestamp,
							ByteHelper.toByteArray(grad));// NoSuchColumnFamilyException

					// family,qualifier,vaule
					String math = String.valueOf(NumberHelper.randomInt(100));
					put.add(ByteHelper.toByteArray("course"),
							ByteHelper.toByteArray("math"), timestamp,
							ByteHelper.toByteArray(math));// NoSuchColumnFamilyException

					// family,qualifier,vaule
					String art = String.valueOf(NumberHelper.randomInt(100));
					put.add(ByteHelper.toByteArray("course"),
							ByteHelper.toByteArray("art"), timestamp,
							ByteHelper.toByteArray(art));// NoSuchColumnFamilyException

					// put.setWriteToWAL(false);
					puts.add(put);
					table.put(puts);
				}
				//
				rowCount++;
				System.out.println(rowCount + " update: "
						+ ByteHelper.toString(result.getRow()));
				if (rowCount > 999) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	public void mockLongRowKeyPut() throws Exception {
		String TABLE_NAME = "student_txid";
		HTableInterface table = htablePool.getTable(TABLE_NAME);// TableNotFoundException
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		// String spliter ="";
		String spliter = StringHelper.SEMICOLON;
		for (int i = 0; i < count; i++) {
			// JG5aXV25uv0000000009;pifyt00004;P7AJK00001
			String ROW_KEY = "";

			// txId,JG5aXV25uv0000000009
			String txId = StringHelper.randomString(10)
					+ StringHelper.leftPad(
							String.valueOf(NumberHelper.randomInt(1, 50)), 10,
							"0");
			ROW_KEY += StringHelper.leftPad(txId, 22, "0");

			// teacherId,pifyt00004
			String teacherId = StringHelper.randomString(5)
					+ StringHelper.leftPad(
							String.valueOf(NumberHelper.randomInt(1, 5)), 5,
							"0");
			ROW_KEY += spliter;
			ROW_KEY += teacherId;

			// id,P7AJK00001
			String id = StringHelper.randomString(5)
					+ StringHelper.leftPad(String.valueOf(NumberHelper
							.randomInt(1, count * 5)), 5, "0");
			ROW_KEY += spliter;
			ROW_KEY += id;

			// 再多加一次txId
			ROW_KEY += spliter;
			ROW_KEY += txId;
			//
			long timestamp = System.currentTimeMillis();

			List<Put> puts = new LinkedList<Put>();

			// insert
			Put put = new Put(ByteHelper.toByteArray(ROW_KEY));
			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("id"), null, timestamp,
					ByteHelper.toByteArray(id));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String grad = String.valueOf(NumberHelper.randomInt(10));
			put.add(ByteHelper.toByteArray("grad"), null, timestamp,
					ByteHelper.toByteArray(grad));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String math = String.valueOf(NumberHelper.randomInt(100));
			put.add(ByteHelper.toByteArray("course"),
					ByteHelper.toByteArray("math"), timestamp,
					ByteHelper.toByteArray(math));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			String art = String.valueOf(NumberHelper.randomInt(100));
			put.add(ByteHelper.toByteArray("course"),
					ByteHelper.toByteArray("art"), timestamp,
					ByteHelper.toByteArray(art));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("teacherId"), null, timestamp,
					ByteHelper.toByteArray(teacherId));// NoSuchColumnFamilyException

			// family,qualifier,vaule
			put.add(ByteHelper.toByteArray("txId"), null, timestamp,
					ByteHelper.toByteArray(txId));// NoSuchColumnFamilyException

			puts.add(put);
			table.put(puts);
			System.out.println("insert: " + ROW_KEY);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
	}
}
