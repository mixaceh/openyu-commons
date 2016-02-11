package org.openyu.commons.hbase.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.hbase.impl.HzDataSourceImpl;
import org.openyu.commons.lang.NumberHelper;

public class HzDataSourceImplTest {

	protected static HzDataSourceImpl hzDataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Configuration configuration = HBaseConfiguration.create();
		// DEV
		// stemktp07,stemktp08,stemktp09,stemktp10
		// 172.22.30.12,172.22.30.13,172.22.30.14,172.22.30.15
		// 這是一個4台集群的daily 日常性能測試環境
		configuration
				.set("hbase.zookeeper.quorum",
						"172.22.30.11,172.22.30.12,172.22.30.13,172.22.30.14,172.22.30.15");
		// configuration.set("hbase.zookeeper.quorum",
		// "172.22.30.12");
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.setInt("hbase.client.retries.number", 3);
		configuration.setLong("hbase.client.pause", 1000L);
		//
		// configuration.setInt("hbase.client.rpc.maxattempts", 1);
		configuration = HBaseConfiguration.create(configuration);
		//
		hzDataSource = new HzDataSourceImpl();
		hzDataSource.setConfiguration(configuration);
		//
		hzDataSource.setMaxActive(2000);
		hzDataSource.setMaxIdle(2000);
	}

	@Test
	public void hzDataSource() {
		System.out.println(hzDataSource);
		assertNotNull(hzDataSource);
	}

	@Test
	// 10 times: 751 mills.
	public void getHConnection() throws Exception {
		int count = 10;
		HConnection result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = hzDataSource.getHConnection();
			System.out.println(result);
			assertNotNull(result);
			result.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		hzDataSource.getHConnection();
		hzDataSource.close();
	}

	@Test(expected = ZooKeeperConnectionException.class)
	public void closeException() throws Exception {
		hzDataSource.getHConnection();
		hzDataSource.close();
		hzDataSource.close();
	}

	@Test
	public void mockGetHConnection() throws Exception {
		int count = 1;
		HConnection result = null;
		for (int i = 0; i < count; i++) {
			result = hzDataSource.getHConnection();
			result.close();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockGetHConnectionWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetHConnection();
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
	public void admin() throws Exception {
		HConnection hConnection = hzDataSource.getHConnection();
		HBaseAdmin admin = new HBaseAdmin(hConnection);
		//
		System.out.println(admin);
		assertNotNull(admin);
		HConnection hconn = admin.getConnection();
		System.out.println(hconn);
		hconn.close();
	}

	@Test
	public void getTable() throws Exception {
		HConnection hconnection = hzDataSource.getHConnection();
		HBaseAdmin admin = new HBaseAdmin(hconnection);
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		long beg = System.currentTimeMillis();
		HTableDescriptor htd = admin.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		admin.close();
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println(htd);// {NAME => 'student', FAMILIES => []}
		assertNotNull(htd);
	}

	@Test
	// 50 times: 3376 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			HConnection hconnection = hzDataSource.getHConnection();
			HBaseAdmin admin = new HBaseAdmin(hconnection);
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

	@Test
	// 10 times: 505 mills.
	public void getPoolableTable() throws Exception {
		PoolableHConnection hConnection = (PoolableHConnection) hzDataSource
				.getHConnection();

		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		int count = 10;
		HTableInterface result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = hConnection.getTable(TABLE_NAME);
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			hConnection.closeTable(result);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
