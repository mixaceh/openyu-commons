package org.openyu.commons.redis.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class JedisConnectionFactoryFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static JedisConnectionFactory jedisConnectionFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/redis/testContext-redis.xml",//

		});
		jedisConnectionFactory = applicationContext.getBean("jedisConnectionFactoryFactoryBean",
				JedisConnectionFactory.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void jedisConnectionFactory() throws Exception {
		System.out.println(jedisConnectionFactory);
		assertNotNull(jedisConnectionFactory);
		//
		System.out.println(jedisConnectionFactory.getConnection());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(jedisConnectionFactory);
		assertNotNull(jedisConnectionFactory);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(jedisConnectionFactory);
		assertNotNull(jedisConnectionFactory);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
