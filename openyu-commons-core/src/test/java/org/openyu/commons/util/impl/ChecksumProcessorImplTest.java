package org.openyu.commons.util.impl;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.util.ChecksumProcessor;
import org.openyu.commons.util.ChecksumType;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ChecksumProcessorImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToXml() {
		ChecksumProcessor value = new ChecksumProcessorImpl();
		//
		value.setChecksum(true);
		value.setChecksumType(ChecksumType.ADLER32);
		value.setChecksumKey("LongLongAgo");
		//
		String result = CollectorHelper.writeToXml(ChecksumProcessorImpl.class,
				value);
		System.out.println(result);
		assertNotNull(result);
		System.out.println(value.getChecksumTypes());
		//
		ChecksumProcessor value2 = new ChecksumProcessorImpl();
		System.out.println(value2.getChecksumTypes());
		assertTrue(value.getChecksumTypes() == value2.getChecksumTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromXml() {
		ChecksumProcessor result = CollectorHelper
				.readFromXml(ChecksumProcessorImpl.class);
		System.out.println(result);
		assertNotNull(result);
		System.out.println(result.getChecksumTypes());
		//
		ChecksumProcessor value2 = new ChecksumProcessorImpl();
		System.out.println(value2.getChecksumTypes());
		assertTrue(result.getChecksumTypes() == value2.getChecksumTypes());
	}

	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	@Test
	// #issue use ArrayHelper.add()
	// 1000 times: 776 mills.
	// round: 0.76, GC: 17
	//
	// #fix use UnsafeHelper.putByteArray
	// 1000 times: 752 mills.
	// round: 0.75, GC: 17
	public void checksum() {
		ChecksumProcessor checksumable = new ChecksumProcessorImpl();
		checksumable.setChecksum(true);
		checksumable.setChecksumKey("LongLongAgo");
		//
		byte[] value = new byte[307200];// 300k
		long result = 0;
		//
		int count = 1000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = checksumable.checksum(1, value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals(564433161L, result);
		//
		result = checksumable.checksum("ADLER32", value);
		System.out.println(result);
		assertEquals(3378971704L, result);
		//
		checksumable.setChecksumType(ChecksumType.ADLER32);
		result = checksumable.checksum(value);
		System.out.println(result);
		assertEquals(3378971704L, result);
	}

	// @Test
	// @BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// // round: 0.1
	// public void executeAsBytes() {
	// Checksumable checksumable = new ChecksumableImpl();
	// //
	// byte[] value = new byte[307200];// 300k
	// byte[] result = null;
	// //
	// int count = 100;
	// for (int i = 0; i < count; i++) {
	// result = checksumable.executeAsBytes(1, value, "LongLongAgo");
	// }
	// //
	// System.out.println(result);
	// assertEquals(8, result.length);
	// //
	// result = checksumable.executeAsBytes("ADLER32", value, "LongLongAgo");
	// System.out.println(result);
	// assertEquals(8, result.length);
	// //
	// result = checksumable.executeAsBytes(ChecksumType.ADLER32, value,
	// "LongLongAgo");
	// System.out.println(result);
	// assertEquals(8, result.length);
	// }
	//
	// @Test
	// @BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// // round: 0.1
	// public void executeAsHex() {
	// Checksumable checksumable = new ChecksumableImpl();
	// //
	// byte[] value = new byte[307200];// 300k
	// String result = null;
	// //
	// int count = 100;
	// for (int i = 0; i < count; i++) {
	// result = checksumable.executeAsHex(1, value, "LongLongAgo");
	// }
	// //
	// System.out.println(result);
	// assertEquals("0000000021a49109", result);
	// //
	// result = checksumable.executeAsHex("ADLER32", value, "LongLongAgo");
	// System.out.println(result);
	// assertEquals("00000000c9670438", result);
	// //
	// result = checksumable.executeAsHex(ChecksumType.ADLER32, value,
	// "LongLongAgo");
	// System.out.println(result);
	// assertEquals("00000000c9670438", result);
	// }
}
