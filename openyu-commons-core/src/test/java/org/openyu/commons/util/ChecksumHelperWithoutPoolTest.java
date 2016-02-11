package org.openyu.commons.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ChecksumHelperWithoutPoolTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 6.44
	public void ___crc32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.___crc32(value);
		}
		//
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 6.50
	public void crc32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.crc32(value);
		}
		//
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.73
	public void ___crc32InputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			in = new ByteArrayInputStream(value);
			result = ChecksumHelperWithoutPool.___crc32(in);
			in.close();
		}
		//
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.76
	public void crc32InputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			in = new ByteArrayInputStream(value);
			result = ChecksumHelperWithoutPool.crc32(in);
			in.close();
		}
		//
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 4.04
	public void crc32AsHex() {
		byte[] value = new byte[307200];// 300k
		String result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.crc32AsHex(value);
		}
		//
		System.out.println(result);
		assertEquals("0000000082ecc096", result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelperWithoutPool.crc32AsHex(in);
		System.out.println(result);
		assertEquals("0000000082ecc096", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.98
	public void ___adler32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.___adler32(value);
		}
		//
		System.out.println(result);
		assertEquals(2956722177L, result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelperWithoutPool.adler32(in);
		System.out.println(result);
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.99
	public void adler32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.adler32(value);
		}
		//
		System.out.println(result);
		assertEquals(2956722177L, result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelperWithoutPool.adler32(in);
		System.out.println(result);
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.19
	public void ___adler32InputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			in = new ByteArrayInputStream(value);
			result = ChecksumHelperWithoutPool.___adler32(in);
			in.close();
		}
		//
		System.out.println(result);// 2956722177
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.20
	public void adler32InputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			in = new ByteArrayInputStream(value);
			result = ChecksumHelperWithoutPool.adler32(in);
			in.close();
		}
		//
		System.out.println(result);// 2956722177
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 1.31
	public void adler32AsHex() {
		byte[] value = new byte[307200];// 300k
		String result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = ChecksumHelperWithoutPool.adler32AsHex(value);
		}
		//
		System.out.println(result);
		assertEquals("00000000b03c0001", result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelperWithoutPool.adler32AsHex(in);
		System.out.println(result);
		assertEquals("00000000b03c0001", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.42 [+- 0.04], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.44, time.warmup: 0.00,
	// time.bench: 0.44
	public void checksumWithProcessor() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		result = ChecksumHelperWithoutPool.checksumWithProcessor(value);
		//
		System.out.println(result);
		assertEquals(2956722177L, result);
	}

}
