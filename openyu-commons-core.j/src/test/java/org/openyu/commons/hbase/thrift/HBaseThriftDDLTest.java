package org.openyu.commons.hbase.thrift;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.nio.ByteBufferHelper;
import org.openyu.commons.lang.ByteHelper;

public class HBaseThriftDDLTest {

	/**
	 * blockingTransport/nonblockingTransport
	 */
	protected static boolean blockingTransport = true;

	/**
	 * binaryProtocol/compactProtocol
	 */
	protected static boolean binaryProtocol = true;

	protected static TTransport ttransport;

	protected static TProtocol tprotocol;

	/**
	 * 客戶端
	 */
	protected static Hbase.Client client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// binaryProtocol
		if (binaryProtocol) {
			System.out.println("[blockingTransport]");
			ttransport = createTTransport();
			System.out.println("[binaryProtocol]");
			tprotocol = createTBinaryProtocol(ttransport);
		} else {
			System.out.println("[noblockingTransport]");
			ttransport = createTFramedTransport();
			System.out.println("[compactProtocol]");
			tprotocol = createTCompactProtocol(ttransport);
		}
		client = new Hbase.Client(tprotocol);
	}

	public static TTransport createTTransport() throws Exception {
		// new TSocket("172.16.18.25", 9095);// PRD
		TTransport result = new TSocket("172.22.30.12", 9090);// DEV
		result.open();
		return result;
	}

	public static TTransport createTFramedTransport() throws Exception {
		// new TFramedTransport(new TSocket("172.16.18.25", 9094));// PRD
		TTransport result = new TFramedTransport(new TSocket("172.22.30.12",
				9092));// DEV
		result.open();
		return result;
	}

	public static TProtocol createTBinaryProtocol(TTransport ttransport)
			throws Exception {
		return new TBinaryProtocol(ttransport);
	}

	public static TProtocol createTCompactProtocol(TTransport ttransport)
			throws Exception {
		return new TCompactProtocol(ttransport);
	}

	public static Hbase.Client createClient(TProtocol tprotocol)
			throws Exception {
		return new Hbase.Client(tprotocol);
	}

	/**
	 * create client
	 *
	 * @throws Exception
	 */
	@Test
	public void client() throws Exception {
		System.out.println(client);
		System.out.println(client.getInputProtocol());
		System.out.println(client.getOutputProtocol());
		assertNotNull(client);
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
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");

		System.out.println(buffer);
	}

	/**
	 * list all tables
	 *
	 * @throws Exception
	 */
	@Test
	// 50 times: 59193 mills.
	public void listTables() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			TTransport ttransport = createTTransport();
			TProtocol tprotocol = createTBinaryProtocol(ttransport);
			Hbase.Client client = createClient(tprotocol);
			System.out.println(client);
			//
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
			}
			ttransport.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
