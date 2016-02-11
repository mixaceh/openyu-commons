package org.openyu.commons.smak.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.smack.XmppSession;
import org.openyu.commons.smack.ex.XmppException;
import org.openyu.commons.smack.impl.XmppConnectionFactoryImpl;
import org.openyu.commons.smack.impl.XmppSessionFactoryImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class XmppSessionFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static XmppConnectionFactoryImpl xmppConnectionFactory;

	protected static XmppSessionFactoryImpl xmppSessionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		xmppConnectionFactory = new XmppConnectionFactoryImpl();
		xmppConnectionFactory.setHost("127.0.0.1");
		xmppConnectionFactory.setPort(5222);
		xmppConnectionFactory.setServiceName("127.0.0.1");
		//
		xmppConnectionFactory.setUsername("root");
		xmppConnectionFactory.setPassword("123456");
		xmppConnectionFactory.setResourceId("resourceId");
		xmppConnectionFactory.setConnectTimeout(15 * 1000);
		xmppConnectionFactory.setSendPresence(true);
		xmppConnectionFactory.setCompressionEnabled(false);
		//
		xmppConnectionFactory.setSslEnabled(false);
		xmppConnectionFactory.setUseSSLContext(false);
		xmppConnectionFactory.setPacketReplyTimeout(25 * 1000);
		//
		xmppConnectionFactory.setMaxActive(100);
		xmppConnectionFactory.setMaxIdle(100);
		//
		xmppSessionFactory = new XmppSessionFactoryImpl();
		xmppSessionFactory.setXmppConnectionFactory(xmppConnectionFactory);
		xmppSessionFactory.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void xmppSessionFactory() {
		System.out.println(xmppSessionFactory);
		assertNotNull(xmppSessionFactory);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 0.88 [+- 0.06], round.block: 0.59 [+- 0.20], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 0.90, time.warmup: 0.00,
	// time.bench: 0.90
	public void openSession() throws Exception {
		XmppSession result = null;
		result = xmppSessionFactory.openSession();
		System.out.println(result);
		assertNotNull(result);
		// result.close();
		xmppSessionFactory.closeSession();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() throws Exception {
		xmppSessionFactory.openSession();
		xmppSessionFactory.closeSession();
		xmppSessionFactory.close();
	}

	@Test(expected = XmppException.class)
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeException() throws Exception {
		xmppSessionFactory.openSession();
		xmppSessionFactory.closeSession();
		xmppSessionFactory.close();
		xmppSessionFactory.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenSession() throws Exception {
		int count = 1;
		XmppSession result = null;
		for (int i = 0; i < count; i++) {
			result = xmppSessionFactory.openSession();
			// result.close();
			xmppSessionFactory.closeSession();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockOpenSessionWithMultiThread() throws Exception {
		for (int i = 0; i < 100; i++) {
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
			Thread.sleep(50);
		}
		//
		Thread.sleep(1 * 60 * 60 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
	// round: 0.09 [+- 0.26], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.87, time.warmup: 0.00,
	// time.bench: 0.87
	public void sendMessage() throws Exception {
		XmppSession xmppSession = xmppSessionFactory.openSession();
		System.out.println(xmppSession);
		//
		xmppSession.sendMessage("abc@127.0.0.1", "test_123");
		xmppSessionFactory.closeSession();
	}

}
