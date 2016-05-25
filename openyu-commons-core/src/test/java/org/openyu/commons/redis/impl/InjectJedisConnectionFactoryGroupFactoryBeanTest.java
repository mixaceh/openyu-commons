package org.openyu.commons.redis.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import redis.clients.jedis.JedisPoolConfig;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class InjectJedisConnectionFactoryGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static JedisConnectionFactory[] jedisConnectionFactorys;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/redis/testContext-inject-redis-group.xml",//

		});
		jedisConnectionFactorys = applicationContext.getBean("jedisConnectionFactoryGroupFactoryBean",
				JedisConnectionFactory[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void jedisConnectionFactorys() throws Exception {
		System.out.println(jedisConnectionFactorys);
		assertNotNull(jedisConnectionFactorys);
		//
		for (JedisConnectionFactory jedisConnectionFactory : jedisConnectionFactorys) {
			System.out.println(jedisConnectionFactory.getConnection());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(jedisConnectionFactorys);
		assertNotNull(jedisConnectionFactorys);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(jedisConnectionFactorys);
		assertNotNull(jedisConnectionFactorys);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
