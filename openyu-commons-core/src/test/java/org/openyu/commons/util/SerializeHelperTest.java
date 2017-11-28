package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.esotericsoftware.kryo.Kryo;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;

public class SerializeHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	public static List<Object> mockLinkedList() {
		LinkedList<Object> result = new LinkedList<Object>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new String(new byte[307200]));// 300K
		//
		result.add(ByteHelper.randomByteArray(10 * 1024 * 1024));// 10M
		result.add(new short[] { 1, 2, 3 });
		result.add(new int[] { 10, 20, 30 });
		result.add(new long[] { 100L, 200L, 300L });
		result.add(new float[] { 1000f, 2000f, 3000f });
		result.add(new double[] { 10000d, 20000d, 30000d });
		//
		result.add(new Integer[] { 1, 2, 3 });
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 6.45 [+- 2.00], round.block: 0.04 [+- 0.03], round.gc: 0.00 [+- 0.00],
	// GC.calls: 1, GC.time: 0.02, time.total: 7.78, time.warmup: 0.00, time.bench:
	// 7.78
	public void bufferSize() {
		List<Object> value = mockLinkedList();
		//
		int result = SerializeHelper.bufferSize(value);
		// 10485896 bytes, 10240.1328125 K, 10.000129699707031 MB
		// 11100328 bytes, 10840.1640625 K, 10.586097717285156 MB
		System.out.println(
				result + " bytes, " + ByteUnit.BYTE.toKiB(result) + " K, " + ByteUnit.BYTE.toMiB(result) + " MB");
		//
		result = SerializeHelper.bufferSize(new byte[0]);
		System.out.println(result + " bytes");
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 6.23 [+- 1.90], round.block: 0.04 [+- 0.05], round.gc: 0.00 [+- 0.00],
	// GC.calls: 2, GC.time: 0.04, time.total: 7.61, time.warmup: 0.00, time.bench:
	// 7.61
	public void jdk() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.jdk(value);
		//
		System.out.println(result.length);// 11100269
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 4.46 [+- 1.43], round.block: 0.04 [+- 0.02], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.06, time.total: 5.01, time.warmup: 0.02, time.bench:
	// 5.00
	public void jdkWithBufferSize() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.jdk(value, 4096);// 不會爆掉
		//
		System.out.println(result.length);// 11100269
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test(expected = IllegalArgumentException.class)
	public void jdkException() {
		List<Object> value = mockLinkedList();
		//
		SerializeHelper.jdk(value, null);
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 8.54 [+- 2.77], round.block: 0.14 [+- 0.10], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.11, time.total: 9.49, time.warmup: 0.00, time.bench:
	// 9.49
	public void dejdk() {
		List<Object> list = mockLinkedList();
		byte[] value = SerializeHelper.jdk(list);

		List<Object> result = null;
		//
		result = SerializeHelper.dejdk(value);
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstConfiguration.getObjectOutput
	// round: 0.91 [+- 0.09], round.block: 0.04 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.05, time.total: 0.95, time.warmup: 0.01,
	// time.bench: 0.94
	public void ___fst2() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.___fst2(value);
		//
		System.out.println(result.length);// 307239
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstConfiguration.getObjectInput
	// round: 1.02 [+- 0.13], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 14, GC.time: 0.35, time.total: 1.31, time.warmup: 0.00,
	// time.bench: 1.31
	public void ___defst2() {
		List<Object> value = mockLinkedList();
		byte[] value = SerializeHelper.___fst2(list);
		List<String> result = null;
		//
		result = SerializeHelper.___defst2(value);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstObjectOutputCacheFactory
	// round: 0.92 [+- 0.09], round.block: 0.03 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.05, time.total: 0.96, time.warmup: 0.00,
	// time.bench: 0.96
	public void fst() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.fst(value);
		//
		System.out.println(result.length);// 307239
		// System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// fstObjectInputCacheFactory
	// round: 1.36 [+- 0.14], round.block: 0.14 [+- 0.10], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.08, time.total: 1.43, time.warmup: 0.00,
	// time.bench: 1.42
	public void defst() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelper.fst(list);
		List<String> result = null;
		//
		result = SerializeHelper.defst(value);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.28, GC: 80
	public void jgroup() {
		List<Object> value = mockLinkedList();
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

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 8.86 [+- 0.64], round.block: 0.04 [+- 0.03], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.05, time.total: 9.25, time.warmup: 0.00, time.bench:
	// 9.25
	public void kryo() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryo(new Kryo(), value);
		//
		System.out.println(result.length);// 10793004
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	@Test
	// 2017/11/28
	// round: 4.66 [+- 0.16], round.block: 0.05 [+- 0.04], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.07, time.total: 4.76, time.warmup: 0.00, time.bench:
	// 4.76
	public void kryoWithBufferSize() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		int bufferSize = SerializeHelper.bufferSize(value);
		result = SerializeHelper.kryo(new Kryo(), value, bufferSize);
		//
		System.out.println(result.length);// 10793004
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.04, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07

	// round: 6.27 [+- 1.76], round.block: 0.03 [+- 0.02], round.gc: 0.00 [+- 0.00],
	// GC.calls: 4, GC.time: 0.07, time.total: 7.52, time.warmup: 0.00, time.bench:
	// 7.52
	public void dekryo() {
		List<Object> list = mockLinkedList();
		byte[] value = SerializeHelper.kryo(new Kryo(), list);

		List<Object> result = null;
		//
		result = SerializeHelper.dekryo(new Kryo(), value, LinkedList.class);
		//
		System.out.println(result.size());// 4
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.04, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07

	// round: 5.70 [+- 1.81], round.block: 0.02 [+- 0.02], round.gc: 0.00 [+- 0.00],
	// GC.calls: 4, GC.time: 0.07, time.total: 7.24, time.warmup: 0.00, time.bench:
	// 7.24
	public void dekryoWithBufferSize() {
		List<Object> list = mockLinkedList();
		byte[] value = SerializeHelper.kryo(new Kryo(), list);

		List<Object> result = null;
		//
		int bufferSize = SerializeHelper.bufferSize(value);
		result = SerializeHelper.dekryo(new Kryo(), value, bufferSize, LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 0.80 [+- 0.08], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.82, time.warmup: 0.00,
	// time.bench: 0.82

	// 2017/11/24
	// round: 7.20 [+- 1.48], round.block: 0.03 [+- 0.04], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.05, time.total: 8.03, time.warmup: 0.00, time.bench:
	// 8.03
	public void kryoWriteClass() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.kryoWriteClass(new Kryo(), value);
		//
		System.out.println(result.length);// 10793137
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	@Test
	// round: 5.31 [+- 1.91], round.block: 0.05 [+- 0.03], round.gc: 0.00 [+- 0.00],
	// GC.calls: 3, GC.time: 0.06, time.total: 6.66, time.warmup: 0.00, time.bench:
	// 6.66
	public void kryoWriteClassWithBufferSize() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		int bufferSize = SerializeHelper.bufferSize(value);
		result = SerializeHelper.kryoWriteClass(new Kryo(), value, bufferSize);
		//
		System.out.println(result.length);// 10793137
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 1.02 [+- 0.10], round.block: 0.11 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.05, time.total: 1.07, time.warmup: 0.00,
	// time.bench: 1.07

	// 2017/11/28
	// round: 4.49 [+- 1.44], round.block: 0.15 [+- 0.19], round.gc: 0.00 [+- 0.00],
	// GC.calls: 4, GC.time: 0.10, time.total: 5.11, time.warmup: 0.00, time.bench:
	// 5.11
	public void dekryoReadClass() {
		List<Object> list = mockLinkedList();
		byte[] value = SerializeHelper.kryoWriteClass(new Kryo(), list);

		List<Object> result = null;
		//
		result = SerializeHelper.dekryoReadClass(new Kryo(), value);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 1, concurrency = 10)
	@Test
	// round: 1.01 [+- 0.10], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 1.02, time.warmup: 0.01,
	// time.bench: 1.02

	// round: 5.49 [+- 1.89], round.block: 0.04 [+- 0.04], round.gc: 0.00 [+- 0.00],
	// GC.calls: 4, GC.time: 0.07, time.total: 7.15, time.warmup: 0.00, time.bench:
	// 7.15
	public void dekryoReadClassWithBufferSize() {
		List<Object> list = mockLinkedList();
		byte[] value = SerializeHelper.kryoWriteClass(new Kryo(), list);

		List<Object> result = null;
		//
		int bufferSize = SerializeHelper.bufferSize(value);
		result = SerializeHelper.dekryoReadClass(new Kryo(), value, bufferSize);
		//
		System.out.println(result.size());
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 10.91, GC: 205
	public void jackson() {
		List<Object> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelper.jackson(value);
		//
		System.out.println(result.length);// 1,843,233 bytes
		// System.out.println(new String(result));
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
		List<Object> value = mockLinkedList();
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
		List<Object> value = mockLinkedList();
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
		List<Object> value = mockLinkedList();
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
		List<Object> value = mockLinkedList();
		byte[] serialize = SerializeHelper.serializeWithProcessor(value);

		List<String> result = null;
		//
		result = SerializeHelper.deserializeWithProcessor(serialize, LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(value, result);
	}
}
