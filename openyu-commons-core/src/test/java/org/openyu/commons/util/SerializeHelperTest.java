package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SerializeHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	public static LinkedList<String> mockLinkedList() {
		LinkedList<String> result = new LinkedList<String>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new String(new byte[307200]));// 300k
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 33.15 [+- 17.92], round.block: 30.93 [+- 17.82], round.gc: 0.00
	// [+- 0.00], GC.calls: 10, GC.time: 0.39, time.total: 68.19, time.warmup:
	// 0.02, time.bench: 68.17
	public void serialize() {
		List<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.serialize(value);
		System.out.println(result.length + " ," + result);// 614,486 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test(expected = IllegalArgumentException.class)
	public void serializeException() {
		List<String> value = mockLinkedList();
		//
		SerializeHelper.serialize(value, null);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 13.68 [+- 6.63], round.block: 11.31 [+- 6.57], round.gc: 0.00 [+-
	// 0.00], GC.calls: 12, GC.time: 0.47, time.total: 25.20, time.warmup: 0.00,
	// time.bench: 25.20
	public void deserialize() {
		List<String> list = mockLinkedList();
		byte[] value = SerializeHelper.serialize(list);

		List<String> result = null;
		//
		result = SerializeHelper.deserialize(value);
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstConfiguration.getObjectOutput
	// round: 0.29, GC: 17
	public void ___fst2() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.___fst2(value);
		//
		System.out.println(result.length + " ," + result);// 307,239 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstConfiguration.getObjectInput
	// round: 0.39, GC: 17
	public void ___defst2() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.___fst2(list);

		List<String> result = null;
		//
		result = SerializeHelper.___defst2(value);
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstObjectOutputCacheFactory
	// round: 0.25, GC: 17
	public void fst() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.fst(value);
		//
		System.out.println(result.length + " ," + result);// 307,239 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstObjectInputCacheFactory
	// round: 0.53, GC: 17
	public void defst() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.fst(list);

		List<String> result = null;
		//
		result = SerializeHelper.defst(value);
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.28, GC: 80
	public void jgroup() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.jgroup(value);
		System.out.println(result.length + " ," + result);// 614,487 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.11, GC: 77
	public void dejgroup() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.jgroup(list);

		List<String> result = null;
		//
		result = SerializeHelper.dejgroup(value);
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.95 [+- 0.10], round.block: 0.05 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.04, time.total: 0.99, time.warmup: 0.00,
	// time.bench: 0.99
	public void kryo() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryo(value);
		//
		System.out.println(result.length);// 307235
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	@Test
	// round: 1.03 [+- 0.01], round.block: 0.06 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.04, time.total: 1.05, time.warmup: 0.00,
	// time.bench: 1.05
	public void kryoWithBufferSize() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryo(value, 102400);
		//
		System.out.println(result.length);// 307235
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.04, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07
	public void dekryo() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.kryo(list);

		List<String> result = null;
		//
		result = SerializeHelper.dekryo(value, LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.04, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07
	public void dekryoWithBufferSize() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.kryo(list);

		List<String> result = null;
		//
		result = SerializeHelper.dekryo(value, 102400, LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.80 [+- 0.08], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.82, time.warmup: 0.00,
	// time.bench: 0.82
	public void kryoWriteClass() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryoWriteClass(value);
		//
		System.out.println(result.length);// 307257
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	@Test
	// round: 1.03 [+- 0.01], round.block: 0.06 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.04, time.total: 1.05, time.warmup: 0.00,
	// time.bench: 1.05
	public void kryoWriteClassWithBufferSize() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryoWriteClass(value, 102400);
		//
		System.out.println(result.length);// 307257
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.11 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.05, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07
	public void dekryoReadClass() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.kryoWriteClass(list);

		List<String> result = null;
		//
		result = SerializeHelper.dekryoReadClass(value);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.01 [+- 0.10], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 1.02, time.warmup: 0.01,
	// time.bench: 1.02
	public void dekryoReadClassWithBufferSize() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.kryoWriteClass(list);

		List<String> result = null;
		//
		result = SerializeHelper.dekryoReadClass(value, 102400);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 10.91, GC: 205
	public void jackson() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.jackson(value);
		}
		//
		System.out.println(result.length + " ," + result);// 1,843,233 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 3.19, GC: 50
	public void dejackson() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.jackson(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.dejackson(value, LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.44, GC: 33
	public void smile() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.smile(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,233 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.52, GC: 49
	public void desmile() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.smile(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.desmile(value, LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.40, GC: 37
	public void smileJaxrs() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.smileJaxrs(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,233 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.54, GC: 57
	public void desmileJaxrs() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.smileJaxrs(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelper.desmileJaxrs(value, LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.59 [+- 0.07], round.block: 0.38 [+- 0.05], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.03, time.total: 0.64, time.warmup: 0.00,
	// time.bench: 0.64
	public void serializeWithProcessor() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.serializeWithProcessor(value);
		//
		System.out.println(result.length + " ," + result);// 307235
		assertEquals(307235, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.67 [+- 0.07], round.block: 0.46 [+- 0.06], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.04, time.total: 0.74, time.warmup: 0.00,
	// time.bench: 0.74
	public void deserializeWithProcessor() {
		LinkedList<String> value = mockLinkedList();
		byte[] serialize = SerializeHelper.serializeWithProcessor(value);

		List<String> result = null;
		//
		result = SerializeHelper.deserializeWithProcessor(serialize, LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(value, result);
	}
}
