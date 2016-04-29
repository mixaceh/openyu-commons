package org.openyu.commons.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ChecksumHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 8.72 [+- 0.95], round.block: 0.05 [+- 0.10], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.02, time.total: 9.01, time.warmup: 0.00,
	// time.bench: 9.01
	public void crc32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		result = ChecksumHelper.crc32(value);
		//
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 5.95 [+- 0.60], round.block: 0.28 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.02, time.total: 6.03, time.warmup: 0.00,
	// time.bench: 6.03
	public void crc32WithInputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		in = new ByteArrayInputStream(value);
		result = ChecksumHelper.crc32(in);
		in.close();
		System.out.println(result);// 2196553878
		assertEquals(2196553878L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 6.17 [+- 0.62], round.block: 0.54 [+- 0.05], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.02, time.total: 6.26, time.warmup: 0.01,
	// time.bench: 6.25
	public void crc32AsHex() {
		byte[] value = new byte[307200];// 300k
		String result = null;
		//
		result = ChecksumHelper.crc32AsHex(value);
		//
		System.out.println(result);
		assertEquals("0000000082ecc096", result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelper.crc32AsHex(in);
		System.out.println(result);
		assertEquals("0000000082ecc096", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 4.57 [+- 0.46], round.block: 0.26 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.02, time.total: 4.64, time.warmup: 0.01,
	// time.bench: 4.62
	public void adler32() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		result = ChecksumHelper.adler32(value);
		//
		System.out.println(result);
		assertEquals(2956722177L, result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelper.adler32(in);
		System.out.println(result);
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 5.57 [+- 0.56], round.block: 0.65 [+- 0.09], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.03, time.total: 5.65, time.warmup: 0.00,
	// time.bench: 5.65
	public void adler32WithInputStream() throws Exception {
		byte[] value = new byte[307200];// 300k
		InputStream in = null;
		long result = 0;
		//
		in = new ByteArrayInputStream(value);
		result = ChecksumHelper.adler32(in);
		in.close();
		//
		System.out.println(result);// 2956722177
		assertEquals(2956722177L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 5.66 [+- 0.57], round.block: 0.34 [+- 0.04], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.02, time.total: 5.74, time.warmup: 0.00,
	// time.bench: 5.74
	public void adler32AsHex() {
		byte[] value = new byte[307200];// 300k
		String result = null;
		//
		result = ChecksumHelper.adler32AsHex(value);
		//
		System.out.println(result);
		assertEquals("00000000b03c0001", result);
		//
		InputStream in = new ByteArrayInputStream(value);
		result = ChecksumHelper.adler32AsHex(in);
		System.out.println(result);
		assertEquals("00000000b03c0001", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 2.43 [+- 0.24], round.block: 0.74 [+- 0.09], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.02, time.total: 2.46, time.warmup: 0.00,
	// time.bench: 2.46
	public void checksumWithProcessor() {
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		result = ChecksumHelper.checksumWithProcessor(value);
		//
		System.out.println(result);
		assertEquals(2956722177L, result);
	}

}
