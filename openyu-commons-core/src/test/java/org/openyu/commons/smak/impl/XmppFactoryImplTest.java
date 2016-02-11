package org.openyu.commons.smak.impl;

import static org.junit.Assert.assertNotNull;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.smack.XmppFactory;
import org.openyu.commons.smack.impl.XmppFactoryImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class XmppFactoryImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static XmppFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = new XmppFactoryImpl("127.0.0.1", 5222, "127.0.0.1", "root", "123456",
				"resourceId", 15 * 1000, true, false, false, false, 25 * 1000);
		factory.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 0.40 [+- 0.00], round.block: 0.05 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 0.41, time.warmup: 0.00,
	// time.bench: 0.41
	public void createXMPPConnection() throws Exception {
		XMPPConnection result = null;
		result = factory.createXMPPConnection();
		System.out.println(result);
		assertNotNull(result);
		//
		XMPPTCPConnection conn = (XMPPTCPConnection) result;
		conn.disconnect();
	}
}
