package org.openyu.commons.smak.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.smack.impl.XmppConnectionFactoryImpl;
import org.openyu.commons.smack.impl.XmppSessionFactoryImpl;
import org.openyu.commons.smack.impl.XmppTemplateImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class XmppTemplateImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static XmppConnectionFactoryImpl xmppConnectionFactory;

	protected static XmppSessionFactoryImpl xmppSessionFactory;

	protected static XmppTemplateImpl xmppTemplate;

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
		//
		xmppTemplate = new XmppTemplateImpl(xmppSessionFactory);
		xmppTemplate.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.00, time.warmup: 0.00,
	// time.bench: 0.00
	public void xmppTemplate() {
		System.out.println(xmppTemplate);
		assertNotNull(xmppTemplate);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
	// round: 0.09 [+- 0.26], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.87, time.warmup: 0.00,
	// time.bench: 0.87
	public void sendMessage() throws Exception {
		xmppTemplate.sendMessage("abc@127.0.0.1", "test_123");
	}

}
