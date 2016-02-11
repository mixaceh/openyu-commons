package org.openyu.commons.hbase;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.BeforeClass;
import org.junit.Test;

//student
//name	grad	course
//				math	art
//Jack	5		97		87
//Rose	4		89		80
//Mary	3		89		80

public class HBaseAdminTest {

	/**
	 * 伺服器端緩存用戶端的連接 是以conf為單位的（可能不準確：通常一個用戶端 連接過來, 伺服器端會有一個執行緒與之對應,
	 * 緩存的是這個伺服器端的執行緒）, 所以最好不要到處創建conf實例, 一個就夠了, 所有共用conf創建的到Hbase 的連接和操作,
	 * 會共用一個連接 這樣可以提高性能, 也會減小伺服器端的壓力 實際上創建Htable pool
	 * admin都是通過HConnection介面的實現類（ HConnectionImplementation）來完成的,
	 * 多個HConnection會由 HConnectionManager來管理, 而conf是HConnectionImplementation的最
	 * 重要的構造參數 , 上面就以conf 來 標識和替代Hconnection 可能會帶來歧義 以為conf就是連接本身
	 * 
	 * #issue 不能共用
	 * 
	 * #fix 需每個thread都獨自擁有
	 */
	// protected static Configuration conf = null;

	/**
	 * 管理員
	 */
	protected static HBaseAdmin hbaseAdmin = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Configuration configuration = createConfiguration();
		//
		hbaseAdmin = createHBaseAdmin(configuration);
	}

	public static Configuration createConfiguration() throws Exception {
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

	@Test
	public void configuration() throws Exception {
		Configuration configuration = createConfiguration();
		System.out.println(configuration);
		assertNotNull(configuration);
	}

	@Test
	public void checkHBaseAvailable() throws Exception {
		Configuration configuration = createConfiguration();
		HBaseAdmin.checkHBaseAvailable(configuration);
	}

	public static HBaseAdmin createHBaseAdmin(Configuration configuration)
			throws Exception {
		return new HBaseAdmin(configuration);
	}

	@Test
	public void hbaseAdmin() throws Exception {
		System.out.println(hbaseAdmin);
		assertNotNull(hbaseAdmin);
	}

	/**
	 * print cluster details
	 *
	 * @throws Exception
	 */
	@Test
	public void clusterDetails() throws Exception {
		ClusterStatus status = hbaseAdmin.getClusterStatus();
		// status.getServerInfo(); //deprecated
		for (ServerName server : status.getServers()) {
			System.out.print("serverName: " + server.getServerName() + ", ");
			System.out.print("hostname: " + server.getHostname() + ", ");
			System.out.print("port: " + server.getPort() + ", ");
			System.out.println("hostAndPort: " + server.getHostAndPort());
		}
		assertNotNull(status);
	}

	// =================================================
	// table
	// =================================================
	/**
	 * creating a table
	 *
	 * @throws Exception
	 */
	@Test
	public void creatTable() throws Exception {
		String TABLE_NAME = "student";
		HTableDescriptor htd = new HTableDescriptor(TABLE_NAME);
		// coprocessor
		htd.addCoprocessor(AggregateImplementation.class.getName());
		hbaseAdmin.createTable(htd);// TableExistsException
		System.out.println(htd);
		assertNotNull(htd);
	}

	/**
	 * table exist
	 *
	 * @throws Exception
	 */
	@Test
	public void tableExists() throws Exception {
		String TABLE_NAME = "student";
		//
		boolean result = hbaseAdmin.tableExists(TABLE_NAME);
		//
		System.out.println(result);// true
	}

	/**
	 * getting a table
	 *
	 * @throws Exception
	 */
	@Test
	public void getTable() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		long beg = System.currentTimeMillis();
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		//
		System.out.println(htd);// {NAME => 'student', FAMILIES => []}
		assertNotNull(htd);
	}

	/**
	 * disable a table
	 *
	 * @throws Exception
	 */
	@Test
	public void disableTable() throws Exception {
		String TABLE_NAME = "student";
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			// disable
			hbaseAdmin.disableTable(TABLE_NAME);// TableNotEnabledException
			System.out.println("[" + TABLE_NAME + "]" + " enabled: "
					+ hbaseAdmin.isTableEnabled(TABLE_NAME));
			// enable
			hbaseAdmin.enableTable(TABLE_NAME);
			System.out.println("[" + TABLE_NAME + "]" + " enabled: "
					+ hbaseAdmin.isTableEnabled(TABLE_NAME));
		}
	}

	@Test
	// 50 times: 2995 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			HBaseAdmin admin = createHBaseAdmin(createConfiguration());
			System.out.println(admin);
			//
			HTableDescriptor[] htds = admin.listTables();
			for (HTableDescriptor htd : htds) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(htd.getNameAsString());
				buffer.append("] ");
				HColumnDescriptor[] hcds = htd.getColumnFamilies();
				for (int j = 0; j < hcds.length; j++) {
					buffer.append(hcds[j].getNameAsString());
					if (j < hcds.length - 1) {
						buffer.append(", ");
					}
				}
				//
				System.out.println(buffer);
			}
			admin.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	/**
	 * modifying a table
	 *
	 * @throws Exception
	 */
	@Test
	public void modifyTable() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));
		System.out.println(htd.getNameAsString());
		hbaseAdmin.modifyTable(Bytes.toBytes(TABLE_NAME), htd);

		System.out.println("modify table");
	}

	/**
	 * deleting a table
	 *
	 * @throws Exception
	 */
	@Test
	public void deleteTable() throws Exception {
		String TABLE_NAME = "student";

		// 先disable才能刪除table
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}
		hbaseAdmin.deleteTable(TABLE_NAME); // TableNotFoundException

		System.out.println("delete table");
	}

	/**
	 * compaction
	 *
	 * 合併離散小檔案
	 *
	 * @throws Exception
	 */
	@Test
	public void compactTable() throws Exception {
		String TABLE_NAME = "student";

		// Major compaction
		hbaseAdmin.majorCompact(TABLE_NAME);
		// Minor compaction
		hbaseAdmin.compact(TABLE_NAME);
		System.out.println("compact table");
	}

	/**
	 * split based on tablename
	 *
	 * @throws Exception
	 */
	@Test
	public void splitTable() throws Exception {
		String TABLE_NAME = "student";

		hbaseAdmin.split(TABLE_NAME);
		System.out.println("split table");
	}

	// =================================================
	// column family
	// =================================================
	/**
	 * adding a column family to an existing table
	 *
	 * @throws Exception
	 */
	@Test
	public void addColumnFamily() throws Exception {
		String TABLE_NAME = "student";

		// 新增column時,須將table disable
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}
		//
		String COLUMN_FAMILY = "id";
		HColumnDescriptor hcd = new HColumnDescriptor(COLUMN_FAMILY);
		// hcd.setBloomFilterType(BloomType.ROWCOL);
		hcd.setBlockCacheEnabled(true);
		// hcd.setCompressionType(Algorithm.SNAPPY);
		hcd.setInMemory(true);
		hcd.setMaxVersions(1);
		hcd.setMinVersions(0);
		hcd.setTimeToLive(432000);// 秒為單位
		hbaseAdmin.addColumn(TABLE_NAME, hcd);// TableNotDisabledException,
		// InvalidFamilyOperationException:Column family 'grad99' does not exist
		System.out.println("add column family [" + COLUMN_FAMILY + "]");
		//
		COLUMN_FAMILY = "grad";
		hcd = new HColumnDescriptor(COLUMN_FAMILY);
		// hcd.setBloomFilterType(BloomType.ROWCOL);
		hcd.setBlockCacheEnabled(true);
		// hcd.setCompressionType(Algorithm.SNAPPY);
		hcd.setInMemory(true);
		hcd.setMaxVersions(1);
		hcd.setMinVersions(0);
		hcd.setTimeToLive(432000);// 秒為單位,5d
		hbaseAdmin.addColumn(TABLE_NAME, hcd);// TableNotDisabledException,
		// InvalidFamilyOperationException:Column family 'grad99' does not exist
		System.out.println("add column family [" + COLUMN_FAMILY + "]");
		//
		COLUMN_FAMILY = "course";
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
		//
		COLUMN_FAMILY = "teacherId";
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
		//
		COLUMN_FAMILY = "txId";
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
	 * column family exist
	 *
	 * @throws Exception
	 */
	@Test
	public void columnFamilyExists() throws Exception {
		String TABLE_NAME = "student";
		//
		String COLUMN_FAMILY = "grad";
		boolean result = false;
		//
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		//
		result = htd.hasFamily(Bytes.toBytes(COLUMN_FAMILY));
		System.out.println(result);// true
	}

	/**
	 * getting a column family
	 *
	 * @throws Exception
	 */
	@Test
	public void getColumnFamily() throws Exception {
		String TABLE_NAME = "student";
		//
		String COLUMN_FAMILY = "grad";
		HColumnDescriptor hcd = null;
		//
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		//
		hcd = htd.getFamily(Bytes.toBytes(COLUMN_FAMILY));
		System.out.println(hcd);// {NAME => 'grad', BLOOMFILTER => 'NONE',
								// REPLICATION_SCOPE => '0', COMPRESSION =>
								// 'NONE', VERSIONS => '3', TTL => '2147483647',
								// MIN_VERSIONS => '0', BLOCKSIZE => '65536',
								// IN_MEMORY => 'false', BLOCKCACHE => 'true'}
	}

	/**
	 * list all column familys
	 *
	 * @throws Exception
	 */
	@Test
	public void listColumnFamilys() throws Exception {
		String TABLE_NAME = "student";
		//
		Collection<HColumnDescriptor> result = null;
		//
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		//
		result = htd.getFamilies();
		System.out.println(result);// [{NAME => 'course', BLOOMFILTER => 'NONE',
									// REPLICATION_SCOPE => '0', COMPRESSION =>
									// 'NONE', VERSIONS => '3', TTL =>
									// '2147483647', MIN_VERSIONS => '0',
									// BLOCKSIZE => '65536', IN_MEMORY =>
									// 'false', BLOCKCACHE => 'true'}, {NAME =>
									// 'grad', BLOOMFILTER => 'NONE',
									// REPLICATION_SCOPE => '0', COMPRESSION =>
									// 'NONE', VERSIONS => '3', TTL =>
									// '2147483647', MIN_VERSIONS => '0',
									// BLOCKSIZE => '65536', IN_MEMORY =>
									// 'false', BLOCKCACHE => 'true'}]
	}

	/**
	 * modifying a column family to an existing table
	 *
	 * @throws Exception
	 */
	@Test
	public void modifyColumnFamily() throws Exception {
		String TABLE_NAME = "student";
		//
		HTableDescriptor htd = hbaseAdmin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));
		//
		String COLUMN_FAMILY = "id";
		// 修改column時,須將table disable
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}
		//
		HColumnDescriptor hcd = htd.getFamily(Bytes.toBytes(COLUMN_FAMILY));
		hbaseAdmin.modifyColumn(TABLE_NAME, hcd);// TableNotDisabledException
		// NullPointerException

		System.out.println("modify column family [" + COLUMN_FAMILY + "]");

		// 修改column後,再將table enable
		if (hbaseAdmin.isTableDisabled(TABLE_NAME)) {
			hbaseAdmin.enableTable(TABLE_NAME);
		}

	}

	/**
	 * deleting a column family to an existing table
	 *
	 * @throws Exception
	 */
	@Test
	public void deleteColumnFamily() throws Exception {
		String TABLE_NAME = "student";
		//
		String COLUMN_FAMILY = "id";
		// 刪除column時,須將table disable
		if (hbaseAdmin.isTableEnabled(TABLE_NAME)) {
			hbaseAdmin.disableTable(TABLE_NAME);
		}

		hbaseAdmin.deleteColumn(TABLE_NAME, COLUMN_FAMILY);// TableNotDisabledException
		// InvalidFamilyOperationException:Column family 'grad99' does not exist

		System.out.println("delete column family [" + COLUMN_FAMILY + "]");

		// 刪完column後,再將table enable
		if (hbaseAdmin.isTableDisabled(TABLE_NAME)) {
			hbaseAdmin.enableTable(TABLE_NAME);
		}
	}

}
