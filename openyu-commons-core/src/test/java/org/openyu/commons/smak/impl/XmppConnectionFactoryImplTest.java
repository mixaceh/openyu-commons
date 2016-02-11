package org.openyu.commons.smak.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.smack.impl.PoolableXmppConnection;
import org.openyu.commons.smack.impl.XmppConnectionFactoryImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class XmppConnectionFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static XmppConnectionFactoryImpl xmppConnectionFactory;

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
		xmppConnectionFactory.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.00, time.warmup: 0.00,
	// time.bench: 0.00
	public void xmppConnectionFactory() {
		System.out.println(xmppConnectionFactory);
		assertNotNull(xmppConnectionFactory);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 0.73 [+- 0.11], round.block: 0.59 [+- 0.20], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 0.90, time.warmup: 0.00,
	// time.bench: 0.90
	public void getXMPPConnection() throws Exception {
		XMPPConnection result = null;
		result = xmppConnectionFactory.getXMPPConnection();
		System.out.println(result);
		assertNotNull(result);
		//
		PoolableXmppConnection conn = (PoolableXmppConnection) result;
		conn.disconnect();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() throws Exception {
		xmppConnectionFactory.getXMPPConnection();
		xmppConnectionFactory.close();
	}

	@Test(expected = IOException.class)
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeException() throws Exception {
		xmppConnectionFactory.getXMPPConnection();
		xmppConnectionFactory.close();
		xmppConnectionFactory.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockGetXMPPConnection() throws Exception {
		int count = 1;
		XMPPConnection result = null;
		for (int i = 0; i < count; i++) {
			result = xmppConnectionFactory.getXMPPConnection();
			// result.disconnect();
		}
		//
		System.out.println("[" + Thread.currentThread().getName() + "] " + result);
		Thread.sleep(3 * 60 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void mockGetXMPPConnectionWithMultiThread() throws Exception {
		for (int i = 0; i < 50; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockGetXMPPConnection();
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void xmppConnection() throws Exception {
		XMPPConnection xmppConnection = xmppConnectionFactory.getXMPPConnection();
		//
		System.out.println(xmppConnection);
		assertNotNull(xmppConnection);
		//
		PoolableXmppConnection conn = (PoolableXmppConnection) xmppConnection;
		conn.disconnect();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 1.05 [+- 0.07], round.block: 0.74 [+- 0.25], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 1.08, time.warmup: 0.00,
	// time.bench: 1.08
	public void sendMessage() throws Exception {
		XMPPConnection xmppConnection = xmppConnectionFactory.getXMPPConnection();
		System.out.println(xmppConnection);
		//
		ChatManager chatManager = ChatManager.getInstanceFor(xmppConnection);

		Chat chat = chatManager.createChat("abc@127.0.0.1");
		chat.sendMessage("test_123");
		//
		PoolableXmppConnection conn = (PoolableXmppConnection) xmppConnection;
		conn.disconnect();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void reallyClose() throws Exception {
		XMPPConnection xmppConnection = xmppConnectionFactory.getXMPPConnection();
		//
		System.out.println(xmppConnection);
		assertNotNull(xmppConnection);
		//
		PoolableXmppConnection conn = (PoolableXmppConnection) xmppConnection;
		conn.reallyClose();
	}

}
