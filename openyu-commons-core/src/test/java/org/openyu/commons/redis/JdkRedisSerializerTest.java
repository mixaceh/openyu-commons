package org.openyu.commons.redis;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class JdkRedisSerializerTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static JdkRedisSerializer jdkRedisSerializer = new JdkRedisSerializer();

	public static LinkedList<String> mockLinkedList() {
		LinkedList<String> result = new LinkedList<String>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new String(new byte[307200]));// 300k
		return result;
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
		result = jdkRedisSerializer.serialize(value);
		//
		System.out.println(result.length + " ," + result);// 5
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.96 [+- 0.10], round.block: 0.06 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.04, time.total: 1.00, time.warmup: 0.00,
	// time.bench: 1.00
	public void serializeWithList() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = jdkRedisSerializer.serialize(value);
		//
		System.out.println(result.length);// 307257
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.94 [+- 0.09], round.block: 0.05 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.96, time.warmup: 0.01,
	// time.bench: 0.95
	public void deserialize() {
		Date date = new Date();
		byte[] value = jdkRedisSerializer.serialize(date);
		Date result = null;
		//
		result = (Date) jdkRedisSerializer.deserialize(value);
		//
		System.out.println(result);
		assertEquals(date, result);
	}

	@SuppressWarnings("unchecked")
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.03 [+- 0.10], round.block: 0.11 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.04, time.total: 1.08, time.warmup: 0.00,
	// time.bench: 1.08
	public void deserializeWithList() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = jdkRedisSerializer.serialize(list);
		List<String> result = null;
		//
		result = (List<String>) jdkRedisSerializer.deserialize(value);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

}
