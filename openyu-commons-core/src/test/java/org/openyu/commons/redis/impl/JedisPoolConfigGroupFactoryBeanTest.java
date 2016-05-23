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

public class JedisPoolConfigGroupFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static JedisPoolConfig[] jedisPoolConfigs;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/redis/testContext-redis-group.xml",//

		});
		jedisPoolConfigs = applicationContext.getBean("jedisPoolConfigGroupFactoryBean", JedisPoolConfig[].class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void jedisPoolConfigs() throws Exception {
		System.out.println(jedisPoolConfigs);
		assertNotNull(jedisPoolConfigs);
		//
		for (JedisPoolConfig jedisPoolConfig : jedisPoolConfigs) {
			System.out.println(jedisPoolConfig);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(jedisPoolConfigs);
		assertNotNull(jedisPoolConfigs);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(jedisPoolConfigs);
		assertNotNull(jedisPoolConfigs);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}
}
