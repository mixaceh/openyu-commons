package org.openyu.commons.cassandra.thrift.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.KsDef;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.cassandra.thrift.CtSession;
import org.openyu.commons.cassandra.thrift.ex.CtException;

public class CtSessionFactoryImplTest {

	protected static CtDataSourceImpl ctDataSource;

	protected static CtSessionFactoryImpl ctSessionFactory;

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

		//
		ctSessionFactory = new CtSessionFactoryImpl();
		ctSessionFactory.setCtDataSource(ctDataSource);
	}

	@Test
	public void ctSessionFactory() {
		System.out.println(ctSessionFactory);
		assertNotNull(ctSessionFactory);
	}

	@Test
	// 10 times: 49 mills.
	public void openSession() throws Exception {
		int count = 10;
		CtSession result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ctSessionFactory.openSession();
			System.out.println(result);
			assertNotNull(result);
			// result.close();
			ctSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void close() throws Exception {
		ctSessionFactory.openSession();
		ctSessionFactory.closeSession();
		ctSessionFactory.close();
	}

	@Test(expected = CtException.class)
	public void closeException() throws Exception {
		ctSessionFactory.openSession();
		ctSessionFactory.closeSession();
		ctSessionFactory.close();
		ctSessionFactory.close();
	}

	@Test
	public void mockOpenSession() throws Exception {
		int count = 1;
		CtSession result = null;
		for (int i = 0; i < count; i++) {
			result = ctSessionFactory.openSession();
			// result.close();
			ctSessionFactory.closeSession();
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
	public void getKeyspace() throws Exception {
		CtSession ctSession = ctSessionFactory.openSession();
		//
		long beg = System.currentTimeMillis();
		String KEYSPACE = "system";
		//
		KsDef kd = ctSession.describe_keyspace(KEYSPACE);// NotFoundException
		ctSessionFactory.closeSession();
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		//
		System.out.println(kd);
	}

	@Test
	// 50 times: 597 mills.
	public void listKeyspaces() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			CtSession ctSession = ctSessionFactory.openSession();
			System.out.println(ctSession);
			//
			List<KsDef> kds = ctSession.describe_keyspaces();
			for (KsDef kd : kds) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(kd.getName());
				buffer.append("] ");
				List<CfDef> cds = kd.getCf_defs();
				int size = cds.size();
				for (int j = 0; j < size; j++) {
					buffer.append(cds.get(j).getName());
					if (j < size - 1) {
						buffer.append(", ");
					}
				}
				//
				System.out.println(buffer);
			}
			ctSessionFactory.closeSession();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}
}
