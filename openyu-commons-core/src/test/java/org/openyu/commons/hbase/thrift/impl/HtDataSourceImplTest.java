package org.openyu.commons.hbase.thrift.impl;

import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.thrift.transport.TTransportException;
import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.nio.ByteBufferHelper;

public class HtDataSourceImplTest {

	protected static HtDataSourceImpl htDataSource;

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
		htDataSource.setRetryNumber(3);
		htDataSource.setRetryPauseMills(1000L);

		// localhost
		// htDataSource = new HtDataSourceImpl();
		// htDataSource.setIp("localhost");
		// htDataSource.setPort(9094);
		// htDataSource.setTimeout(5000);
		// htDataSource.setNonblocking(false);
	}

	@Test
	public void htDataSource() {
		System.out.println(htDataSource);
		assertNotNull(htDataSource);
	}

	@Test
	// 10 times: 188 mills.
	public void getTransport() throws Exception {
		int count = 10;
		TTransport result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = htDataSource.getTTransport();
			System.out.println(result);
			assertNotNull(result);
			result.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		htDataSource.getTTransport();
		htDataSource.close();
	}

	@Test(expected = TTransportException.class)
	public void closeException() throws Exception {
		htDataSource.getTTransport();
		htDataSource.close();
		htDataSource.close();
	}

	@Test
	public void mockGetTransport() throws Exception {
		int count = 1;
		TTransport result = null;
		for (int i = 0; i < count; i++) {
			result = htDataSource.getTTransport();
			result.close();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] "
				+ result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	public void mockGetTransportWithMultiThread() throws Exception {
		// PRD blocking: 450-480
		// PRD noblocking: 330-350

		// DEV blocking: 430-450
		// localhost blocking: 2000
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetTransport();
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
	public void client() throws Exception {
		TTransport ttransport = htDataSource.getTTransport();
		TProtocol tprotocol = new TBinaryProtocol(ttransport);
		Hbase.Client client = new Hbase.Client(tprotocol);
		//
		System.out.println(client);
		System.out.println("getInputProtocol: " + client.getInputProtocol());
		System.out.println("getOutputProtocol: " + client.getOutputProtocol());
		assertNotNull(client);
		TTransport ttrans = client.getInputProtocol().getTransport();
		System.out.println(ttrans);
		ttrans.close();
	}

	@Test
	public void getTable() throws Exception {
		TTransport ttransport = htDataSource.getTTransport();
		TProtocol tprotocol = new TBinaryProtocol(ttransport);
		Hbase.Client client = new Hbase.Client(tprotocol);
		//
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		long beg = System.currentTimeMillis();
		Map<ByteBuffer, ColumnDescriptor> hcds = client
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
		ttransport.close();
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println(buffer);
	}

	@Test
	// 50 times: 59110 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			TTransport ttransport = htDataSource.getTTransport();
			TProtocol tprotocol = new TBinaryProtocol(ttransport);
			Hbase.Client client = new Hbase.Client(tprotocol);

			List<ByteBuffer> list = client.getTableNames();
			for (ByteBuffer name : list) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(ByteBufferHelper.toString(name));
				buffer.append("] ");
				Map<ByteBuffer, ColumnDescriptor> hcds = client
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
				ttransport.close();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
