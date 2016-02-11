package org.openyu.commons.hbase.thrift.impl;

import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.nio.ByteBufferHelper;
import org.openyu.commons.hbase.thrift.HtSession;
import org.openyu.commons.hbase.thrift.ex.HtException;

public class HtSessionFactoryImplTest {

	protected static HtDataSourceImpl htDataSource;

	protected static HtSessionFactoryImpl htSessionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// PRD
		// htDataSource = new HtDataSourceImpl();
		// htDataSource.setIp("172.16.18.25");
		// htDataSource.setPort(9095);
		// htDataSource.setTimeout(5000);
		// htDataSource.setNonblocking(false);

		// DEV
		htDataSource = new HtDataSourceImpl();
		htDataSource.setIp("172.22.30.12");
		htDataSource.setPort(9090);
		htDataSource.setTimeout(5000);
		htDataSource.setNonblocking(false);
		//
		htDataSource.setMaxActive(2000);
		htDataSource.setMaxIdle(2000);
		//
		//
		htDataSource.setRetryNumber(3);
		htDataSource.setRetryPauseMills(1000L);

		// localhost
		// htDataSource = new ThriftDataSourceImpl();
		// htDataSource.setIp("localhost");
		// htDataSource.setPort(9094);
		// htDataSource.setTimeout(5000);
		// htDataSource.setNonblocking(false);

		//
		htSessionFactory = new HtSessionFactoryImpl();
		htSessionFactory.setHtDataSource(htDataSource);
	}

	@Test
	public void htSessionFactory() {
		System.out.println(htSessionFactory);
		assertNotNull(htSessionFactory);
	}

	@Test
	// 10 times: 44 mills.
	public void openSession() throws Exception {
		int count = 10;
		HtSession result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = htSessionFactory.openSession();
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			htSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		htSessionFactory.openSession();
		htSessionFactory.closeSession();
		htSessionFactory.close();
	}

	@Test(expected = HtException.class)
	public void closeException() throws Exception {
		htSessionFactory.openSession();
		htSessionFactory.closeSession();
		htSessionFactory.close();
		htSessionFactory.close();
	}

	@Test
	public void mockOpenSession() throws Exception {
		int count = 1;
		HtSession result = null;
		for (int i = 0; i < count; i++) {
			result = htSessionFactory.openSession();
			// result.close();
			Thread.sleep(100);
			htSessionFactory.closeSession();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockOpenSessionWithMultiThread() throws Exception {
		// PRD blocking: 450-480
		// PRD noblocking: 330-350

		// DEV blocking: 430-450
		// localhost blocking: 2000
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
		HtSession thriftSession = htSessionFactory.openSession();
		//
		String TABLE_NAME = "UIH_OverallItemInfo";
		long beg = System.currentTimeMillis();
		Map<ByteBuffer, ColumnDescriptor> hcds = thriftSession
				.getColumnDescriptors(ByteBufferHelper.toByteBuffer(TABLE_NAME));
		//
		StringBuilder buffer = new StringBuilder();
		buffer.append("[");
		buffer.append(TABLE_NAME);
		buffer.append("] ");
		int size = hcds.size();
		int i = 0;
		for (ColumnDescriptor column : hcds.values()) {
			buffer.append(ByteHelper.toString(column.getName()));
			buffer.append(", " + column.getTimeToLive());// server沒提供
			if (i < size - 1) {
				buffer.append(", ");
			}
			i++;
		}
		htSessionFactory.closeSession();
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		System.out.println(buffer);
	}

	@Test
	// 50 times: 52851 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			HtSession htSession = htSessionFactory.openSession();
			System.out.println(htSession);
			//
			List<ByteBuffer> list = htSession.getTableNames();
			for (ByteBuffer name : list) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(ByteBufferHelper.toString(name));
				buffer.append("] ");
				Map<ByteBuffer, ColumnDescriptor> hcds = htSession
						.getColumnDescriptors(name);
				int size = hcds.size();
				int j = 0;
				for (ColumnDescriptor column : hcds.values()) {
					buffer.append(ByteHelper.toString(column.getName()));
					if (j < size - 1) {
						buffer.append(", ");
					}
					j++;
				}
				//
				System.out.println(buffer);
			}
			htSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}
}
