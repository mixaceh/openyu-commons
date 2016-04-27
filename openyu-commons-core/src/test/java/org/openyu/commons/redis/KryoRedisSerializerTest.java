package org.openyu.commons.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class KryoRedisSerializerTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static KryoRedisSerializer kryoRedisSerializer = new KryoRedisSerializer();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 1.23 [+- 0.01], round.block: 0.07 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.04, time.total: 1.25, time.warmup: 0.00,
	// time.bench: 1.24
	public void serialize() {
		String value = "abc";
		byte[] result = null;
		//
		result = kryoRedisSerializer.serialize(value);
		//
		System.out.println(result.length + " ," + result);// 5
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	public void deserialize() {
		Date value = new Date();
		byte[] result = kryoRedisSerializer.serialize(value);
		Date deValue = null;
		//
		deValue = (Date) kryoRedisSerializer.deserialize(result);
		//
		System.out.println(deValue);
		assertEquals(value, deValue);
	}
}
