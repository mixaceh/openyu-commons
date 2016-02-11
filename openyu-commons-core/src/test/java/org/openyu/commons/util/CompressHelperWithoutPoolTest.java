package org.openyu.commons.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;

public class CompressHelperWithoutPoolTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.13 [+- 0.02], round.block: 0.00 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.16, time.warmup: 0.00,
	// time.bench: 0.16
	public void deflater() throws Exception {
		byte[] value = new byte[307200];
		//
		byte[] result = null;
		result = CompressHelperWithoutPool.deflater(value);
		//
		System.out.println("length: " + result.length);// 1363
		// SystemHelper.println(result);
		assertEquals(1363, result.length);// BEST_SPEED
		// assertEquals(321, result.length);// BEST_COMPRESSION
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.15 [+- 0.03], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.19, time.warmup: 0.00,
	// time.bench: 0.19
	public void inflater() throws Exception {
		byte[] value = new byte[307200];
		byte[] compress = CompressHelperWithoutPool.deflater(value);
		//
		byte[] result = null;
		//
		result = CompressHelperWithoutPool.inflater(compress);
		//
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.97
	public void lz4() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.lz4(value);
		}
		System.out.println("length: " + result.length);// 1219
		assertEquals(1219, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 1.17
	public void unlz4() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.lz4(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.unlz4(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 1.30
	public void snappy() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.snappy(value);
		}
		System.out.println("length: " + result.length);// 14423
		assertEquals(14423, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 1.92
	public void unsnappy() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.snappy(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.unsnappy(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 185.09
	public void lzma() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.lzma(value);
		}
		System.out.println("length: " + result.length);// 124
		assertEquals(124, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 13.92
	public void unlzma() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.lzma(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.unlzma(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.56
	public void lzf() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.lzf(value);
		}
		System.out.println("length: " + result.length);// 3561
		assertEquals(3561, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 4.95
	public void unlzf() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.lzf(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.unlzf(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 23.79
	public void gzip() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.gzip(value);
		}
		System.out.println("length: " + result.length);// 333
		// SystemHelper.println(result);
		assertEquals(333, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 16.57
	public void ungzip() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.gzip(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.ungzip(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 18.09
	public void deflate() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.deflate(value);
		}
		System.out.println("length: " + result.length);// 321
		// SystemHelper.println(result);
		assertEquals(321, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round:
	public void inflate() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.deflate(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 10000;
		for (int i = 0; i < count; i++) {
			result = CompressHelperWithoutPool.inflate(value);
		}
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void deflater9NotEqualsDeflate() throws Exception {
		String stringValue = new String(new byte[307200]);
		byte[] value = CompressHelperWithoutPool.deflater(ByteHelper
				.toByteArray(stringValue));
		System.out.println(value.length);
		//
		byte[] value2 = CompressHelperWithoutPool.deflate(ByteHelper
				.toByteArray(stringValue));
		System.out.println(value2.length);
		assertFalse(Arrays.equals(value, value2));
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.52 [+- 0.05], round.block: 0.11 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.54, time.warmup: 0.00,
	// time.bench: 0.54
	public void compressWithProcessor() {
		byte[] value = new byte[307200];
		byte[] result = null;
		//
		result = CompressHelperWithoutPool.compressWithProcessor(value);
		//
		System.out.println("length: " + result.length);// 1219
		assertEquals(1219, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.52 [+- 0.05], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.55, time.warmup: 0.00,
	// time.bench: 0.54
	public void decompressWithProcessor() {
		byte[] value = new byte[307200];
		byte[] compress = CompressHelperWithoutPool
				.compressWithProcessor(value);
		//
		byte[] result = null;
		//
		result = CompressHelperWithoutPool.decompressWithProcessor(compress);
		//
		System.out.println("length: " + result.length);// 307200
		assertTrue(Arrays.equals(value, result));
	}

	// --------------------------------------------------
	// SampleTest
	// --------------------------------------------------
	public static class SampleTest extends BaseTestSupporter {

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.38
		public void deflaterBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.deflater(encode);
			}
			System.out.println("length: " + result.length);// 336
			assertEquals(336, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void inflaterBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			byte[] compress = CompressHelperWithoutPool.deflater(encode);
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.inflater(compress);
			}
			System.out.println("length: " + result.length);
			assertTrue(Arrays.equals(encode, result));
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.38
		public void deflaterHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value.getBytes());// 1578
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.deflater(encode.getBytes());
			}
			System.out.println("length: " + result.length);// 266
			assertEquals(266, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void inflaterHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value);// 1578
			byte[] compress = CompressHelperWithoutPool.deflater(encode
					.getBytes());
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.inflater(compress);
			}
			System.out.println("length: " + result.length);
			assertTrue(Arrays.equals(encode.getBytes(), result));
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.38
		public void gzipBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.gzip(encode);
			}
			System.out.println("length: " + result.length);// 340
			assertEquals(340, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void ungzipBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			byte[] compress = CompressHelperWithoutPool.gzip(encode);
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.ungzip(compress);
			}
			System.out.println("length: " + result.length);
			assertTrue(Arrays.equals(encode, result));
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.38
		public void gzipHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value.getBytes());// 1578
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.gzip(encode.getBytes());
			}
			System.out.println("length: " + result.length);// 249
			assertEquals(249, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void ungzipHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value);// 1578
			byte[] compress = CompressHelperWithoutPool.gzip(encode.getBytes());
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.ungzip(compress);
			}
			System.out.println("length: " + result.length);
			assertEquals(1578, result.length);
			assertTrue(Arrays.equals(encode.getBytes(), result));
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.38
		public void deflateBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.deflate(encode);
			}
			System.out.println("length: " + result.length);// 328
			assertEquals(328, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void inflateBase64() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			byte[] encode = EncodingHelper.encodeBase64(value);// 1052
			byte[] compress = CompressHelperWithoutPool.deflate(encode);
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.inflate(compress);
			}
			System.out.println("length: " + result.length);
			assertTrue(Arrays.equals(encode, result));
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.37
		public void deflateHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value.getBytes());// 1578
			//
			byte[] result = null;
			int count = 10000;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.deflate(encode.getBytes());
			}
			System.out.println("length: " + result.length);// 237
			assertEquals(237, result.length);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
		// round: 0.13
		public void inflateHex() throws Exception {
			// 789
			String value = "Catalog||Games, movies & music||Music||Childrens,Catalog||Games, movies & music||Music||Jazz music,Catalog||Games, movies & music||Music||Blues music,Catalog||Games, movies & music||Video||Television: series,Catalog||Games, movies & music||Music||Folk music,Catalog||Games, movies & music||Video||Miscellaneous,Catalog||Games, movies & music||Music||Popular music,Catalog||Games, movies & music||Music||New age music,Catalog||Games, movies & music||Music||Original cast recordings,Catalog||Games, movies & music||Video||Westerns,Catalog||Games, movies & music||Video||Horror,Catalog||Games, movies & music||Music||Music video: latin,Catalog||Games, movies & music||Music||Classical music,Catalog||Games, movies & music||Music||World music,Catalog||Games, movies & music||Music||Electronica";
			String encode = EncodingHelper.encodeHex(value);// 1578
			byte[] compress = CompressHelperWithoutPool.deflate(encode
					.getBytes());
			//
			byte[] result = null;
			int count = 1;
			for (int i = 0; i < count; i++) {
				result = CompressHelperWithoutPool.inflate(compress);
			}
			System.out.println("length: " + result.length);
			assertTrue(Arrays.equals(encode.getBytes(), result));
		}
	}

}
