package org.openyu.commons.hbase.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.impl.HzSessionFactoryImpl;
import org.openyu.commons.hbase.ex.HzException;
import org.openyu.commons.lang.NumberHelper;

public class HzSessionFactoryImplTest {

	protected static HzDataSourceImpl hbaseZookeeperDataSource;

	protected static HzSessionFactoryImpl hzSessionFactory;

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
		// configuration.set("hbase.zookeeper.quorum", "172.22.30.12");
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.setInt("hbase.client.retries.number", 3);
		configuration.setLong("hbase.client.pause", 1000L);
		//
		configuration.setInt("hbase.client.scanner.caching", 200);
		//
		// configuration.setInt("hbase.client.rpc.maxattempts", 1);
		configuration = HBaseConfiguration.create(configuration);
		//
		hbaseZookeeperDataSource = new HzDataSourceImpl();
		hbaseZookeeperDataSource.setConfiguration(configuration);
		//
		hbaseZookeeperDataSource.setMaxActive(2000);
		hbaseZookeeperDataSource.setMaxIdle(2000);
		//
		hzSessionFactory = new HzSessionFactoryImpl();
		hzSessionFactory.setHzDataSource(hbaseZookeeperDataSource);
	}

	@Test
	public void hzSessionFactory() {
		System.out.println(hzSessionFactory);
		assertNotNull(hzSessionFactory);
	}

	@Test
	// 10 times: 798 mills.
	public void openSession() throws Exception {
		int count = 10;
		HzSession result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = hzSessionFactory.openSession();
			System.out.println(result);
			assertNotNull(result);
			//
			result.close();
			hzSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		hzSessionFactory.openSession();
		hzSessionFactory.closeSession();
		hzSessionFactory.close();
	}

	@Test(expected = HzException.class)
	public void closeException() throws Exception {
		hzSessionFactory.openSession();
		hzSessionFactory.closeSession();
		hzSessionFactory.close();
		hzSessionFactory.close();
	}

	@Test
	public void mockOpenSession() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";

		int count = 1;
		HzSession result = null;
		for (int i = 0; i < count; i++) {
			result = hzSessionFactory.openSession();
			HTableInterface table = result.getTable(TABLE_NAME);
			System.out.println(table);
			table.close();
			// result.close();
			Thread.sleep(100);
			hzSessionFactory.closeSession();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockOpenSessionWithMultiThread() throws Exception {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockOpenSession();
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
	public void getTable() throws Exception {
		HzSession zookeeperSession = hzSessionFactory.openSession();
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		long beg = System.currentTimeMillis();
		HTableDescriptor htd = zookeeperSession.getTableDescriptor(Bytes
				.toBytes(TABLE_NAME));// TableNotFoundException
		hzSessionFactory.closeSession();
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println(htd);// {NAME => 'student', FAMILIES => []}
		assertNotNull(htd);
	}

	@Test
	// 50 times: 3545 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			HzSession zookeeperSession = hzSessionFactory
					.openSession();
			System.out.println(zookeeperSession);
			//
			HTableDescriptor[] htds = zookeeperSession.listTables();
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
			hzSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 10 times: 505 mills.
	public void getPoolableTable() throws Exception {
		HzSession zookeeperSession = hzSessionFactory.openSession();
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		int count = 10;
		HTableInterface result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = zookeeperSession.getTable(TABLE_NAME);
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			zookeeperSession.closeTable(result);
		}
		//
		hzSessionFactory.closeSession();
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}
}
