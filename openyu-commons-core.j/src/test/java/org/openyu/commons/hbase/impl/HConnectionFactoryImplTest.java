package org.openyu.commons.hbase.impl;

import static org.junit.Assert.assertNotNull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.hbase.HConnectionFactory;
import org.openyu.commons.hbase.impl.HConnectionFactoryImpl;

public class HConnectionFactoryImplTest {

	protected static HConnectionFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// DEV
		Configuration configuration = new Configuration();
		configuration
				.set("hbase.zookeeper.quorum",
						"172.22.30.11,172.22.30.12,172.22.30.13,172.22.30.14,172.22.30.15");
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.setInt("hbase.client.retries.number", 3);
		configuration.setLong("hbase.client.pause", 1000L);
		//configuration.setInt("hbase.client.rpc.maxattempts", 1);
		factory = new HConnectionFactoryImpl(configuration);
	}

	@Test
	// 10 times: 2198 mills.
	public void createHConnection() throws Exception {
		int count = 10;
		HConnection result = null;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = factory.createHConnection();
			System.out.println(result);
			assertNotNull(result);
			result.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

}
