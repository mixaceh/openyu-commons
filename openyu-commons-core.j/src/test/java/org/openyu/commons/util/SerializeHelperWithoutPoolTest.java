package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SerializeHelperWithoutPoolTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	public static LinkedList<String> mockLinkedList() {
		LinkedList<String> result = new LinkedList<String>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new String(new byte[307200]));// 300k
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void getInstance() {
		// SerializeHelperWithoutPool.getInstance();
		List<String> value = mockLinkedList();
		SerializeHelperWithoutPool.serialize(value);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 1.29, GC: 80
	public void serialize() {
		List<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.serialize(value);
		}
		//
		System.out.println(result.length + " ," + result);// 614,486 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test(expected = IllegalArgumentException.class)
	public void serializeException() {
		List<String> value = mockLinkedList();
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			SerializeHelperWithoutPool.serialize(value, null);
		}
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 1.14, GC: 77
	public void deserialize() {
		List<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.serialize(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.deserialize(value);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// FSTObjectOutput
	// round: 0.30, GC: 17
	public void ___fst() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___fst(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,239 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// FSTObjectInput
	// round: 0.49, GC: 70
	public void ___defst() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___fst(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___defst(value);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// fstConfiguration.getObjectOutput
	// round: 0.29, GC: 17
	public void ___fst2() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___fst2(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,239 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// fstConfiguration.getObjectInput
	// round: 0.39, GC: 17
	public void ___defst2() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___fst2(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___defst2(value);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// fstObjectOutputCacheFactory
	// round: 0.25, GC: 17
	public void fst() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.fst(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,239 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// fstObjectInputCacheFactory
	// round: 0.53, GC: 17
	public void defst() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.fst(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.defst(value);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 1.28, GC: 80
	public void jgroup() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.jgroup(value);
		}
		//
		System.out.println(result.length + " ," + result);// 614,487 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 1.11, GC: 77
	public void dejgroup() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.jgroup(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.dejgroup(value);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.70, GC: 38
	public void ___kryo() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___kryo(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,235 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.55, GC: 35
	public void ___dekryo() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___kryo(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___dekryo(value,
					LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// kryoCacheFactory
	// round: 0.70, GC: 38
	public void kryo() {
		List<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.kryo(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,235 bytes
		System.out.println(new String(result));
	}

	@SuppressWarnings("unchecked")
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// kryoCacheFactory
	// round: 0.55, GC: 35
	public void dekryo() {
		List<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.kryo(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.dekryo(value, LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 2.10, GC: 204
	public void ___jackson() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___jackson(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,235 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 3.45, GC: 54
	public void ___dejackson() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___jackson(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___dejackson(value,
					LinkedList.class);
		}
		//
		System.out.println(result);
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
			result = SerializeHelperWithoutPool.jackson(value);
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
		byte[] value = SerializeHelperWithoutPool.jackson(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.dejackson(value,
					LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 12.60, GC: 410
	public void jacksonToString() {
		LinkedList<String> value = mockLinkedList();
		String result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.jacksonToString(value);
		}
		//
		System.out.println(result.length() + " ," + result);// 1,843,225 bytes
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 5.24, GC: 340
	public void dejacksonFromString() {
		LinkedList<String> list = mockLinkedList();
		String value = SerializeHelperWithoutPool.jacksonToString(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.dejacksonFromString(value,
					LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.44, GC: 35
	public void ___smile() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___smile(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,235 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.73, GC: 54
	public void ___desmile() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___smile(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___desmile(value,
					LinkedList.class);
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
			result = SerializeHelperWithoutPool.smile(value);
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
		byte[] value = SerializeHelperWithoutPool.smile(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool
					.desmile(value, LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.45, GC: 39
	public void ___smileJaxrs() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___smileJaxrs(value);
		}
		//
		System.out.println(result.length + " ," + result);// 307,233 bytes
		System.out.println(new String(result));
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// round: 0.74, GC: 61
	public void ___desmileJaxrs() {
		LinkedList<String> list = mockLinkedList();
		byte[] value = SerializeHelperWithoutPool.___smileJaxrs(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.___desmileJaxrs(value,
					LinkedList.class);
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
			result = SerializeHelperWithoutPool.smileJaxrs(value);
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
		byte[] value = SerializeHelperWithoutPool.smileJaxrs(list);

		List<String> result = null;
		//
		int count = 500;
		for (int i = 0; i < count; i++) {
			result = SerializeHelperWithoutPool.desmileJaxrs(value,
					LinkedList.class);
		}
		//
		System.out.println(result);
		assertCollectionEquals(list, result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.63 [+- 0.07], round.block: 0.09 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.04, time.total: 0.69, time.warmup: 0.00,
	// time.bench: 0.69
	public void serializeWithProcessor() {
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = SerializeHelperWithoutPool.serializeWithProcessor(value);
		//
		System.out.println(result.length + " ," + result);// 307235
		assertEquals(307235, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.67 [+- 0.07], round.block: 0.13 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.04, time.total: 0.74, time.warmup: 0.00,
	// time.bench: 0.74
	public void deserializeWithProcessor() {
		LinkedList<String> value = mockLinkedList();
		byte[] serialize = SerializeHelperWithoutPool
				.serializeWithProcessor(value);

		List<String> result = null;
		//
		result = SerializeHelperWithoutPool.deserializeWithProcessor(serialize,
				LinkedList.class);
		//
		System.out.println(result.size());
		assertCollectionEquals(value, result);
	}
}
