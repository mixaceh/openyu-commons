package org.openyu.commons.thrift.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.thrift.transport.TTransport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.thrift.TTransportFactory;

public class TFramedTTransportFactoryImplTest {

	protected static TTransportFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// DEV
		factory = new TFramedTTransportFactoryImpl("172.22.30.12", 9090, 0, 3,
				1000L);
	}

	@Test
	// 10 times: 56 mills.
	public void createTTransport() throws Exception {
		int count = 10;
		TTransport result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = factory.createTTransport();
			System.out.println(result);
			assertNotNull(result);
			result.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
