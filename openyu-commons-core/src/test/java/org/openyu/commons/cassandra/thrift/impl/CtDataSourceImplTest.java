package org.openyu.commons.cassandra.thrift.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.KsDef;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;

public class CtDataSourceImplTest {

	protected static CtDataSourceImpl ctDataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// DEV
		ctDataSource = new CtDataSourceImpl();
		ctDataSource.setIp("172.22.29.13");
		ctDataSource.setPort(9160);
		ctDataSource.setTimeout(5000);
		ctDataSource.setNonblocking(true);
		//
		ctDataSource.setMaxActive(2000);
		ctDataSource.setMaxIdle(2000);
		//
		ctDataSource.setRetryNumber(3);
		ctDataSource.setRetryPauseMills(1000L);
	}

	@Test
	public void ctDataSource() {
		System.out.println(ctDataSource);
		assertNotNull(ctDataSource);
	}

	@Test
	// 10 times: 136 mills.
	public void getTransport() throws Exception {
		int count = 10;
		TTransport result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ctDataSource.getTTransport();
			System.out.println(result);
			assertNotNull(result);
			result.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		ctDataSource.getTTransport();
		ctDataSource.close();
	}

	@Test(expected = TTransportException.class)
	public void closeException() throws Exception {
		ctDataSource.getTTransport();
		ctDataSource.close();
		ctDataSource.close();
	}

	@Test
	public void mockGetTransport() throws Exception {
		int count = 1;
		TTransport result = null;
		for (int i = 0; i < count; i++) {
			result = ctDataSource.getTTransport();
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
		// 127.0.0.1 blocking: 2000
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
		TTransport ttransport = ctDataSource.getTTransport();
		TProtocol tprotocol = new TBinaryProtocol(ttransport);
		Cassandra.Client client = new Cassandra.Client(tprotocol);
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
	public void getKeyspace() throws Exception {
		TTransport ttransport = ctDataSource.getTTransport();
		TProtocol tprotocol = new TBinaryProtocol(ttransport);
		Cassandra.Client client = new Cassandra.Client(tprotocol);
		//
		long beg = System.currentTimeMillis();
		String KEYSPACE = "system";
		//
		KsDef kd = client.describe_keyspace(KEYSPACE);// NotFoundException
		ttransport.close();
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		//
		System.out.println(kd);
	}

	@Test
	// 50 times: 527 mills.
	public void listKeyspaces() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			TTransport ttransport = ctDataSource.getTTransport();
			TProtocol tprotocol = new TBinaryProtocol(ttransport);
			Cassandra.Client client = new Cassandra.Client(tprotocol);
			//
			List<KsDef> kds = client.describe_keyspaces();
			for (KsDef kd : kds) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(kd.getName());
				buffer.append("] ");
				kd.getCf_defs();
				List<CfDef> cds = kd.getCf_defs();
				for (int j = 0; j < cds.size(); j++) {
					buffer.append(cds.get(j).getName());
					if (j < cds.size() - 1) {
						buffer.append(", ");
					}
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
