package org.openyu.commons.util.impl;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.util.CompressProcessor;
import org.openyu.commons.util.CompressType;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CompressProcessorImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToXml() {
		CompressProcessor value = new CompressProcessorImpl();
		//
		value.setCompress(true);
		value.setCompressType(CompressType.LZ4);
		//
		String result = CollectorHelper.writeToXml(CompressProcessorImpl.class,
				value);
		System.out.println(result);
		assertNotNull(result);
		System.out.println(value.getCompressTypes());
		//
		CompressProcessor value2 = new CompressProcessorImpl();
		System.out.println(value2.getCompressTypes());
		assertTrue(value.getCompressTypes() == value2.getCompressTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromXml() {
		CompressProcessor result = CollectorHelper
				.readFromXml(CompressProcessorImpl.class);
		System.out.println(result);
		assertNotNull(result);
		System.out.println(result.getCompressTypes());
		//
		CompressProcessor value2 = new CompressProcessorImpl();
		System.out.println(value2.getCompressTypes());
		assertTrue(result.getCompressTypes() == value2.getCompressTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void compress() {
		CompressProcessor compressable = new CompressProcessorImpl();
		compressable.setCompress(true);
		compressable.setCompressType(CompressType.LZ4);
		//
		String stringValue = new String(new byte[307200]);
		byte[] value = ByteHelper.toByteArray(stringValue);
		//
		byte[] result = null;
		int count = 1000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = compressable.compress(7, value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("length: " + result.length);// 1219
		assertEquals(1219, result.length);
		//
		result = compressable.compress("LZ4", value);
		System.out.println("length: " + result.length);// 1219
		assertEquals(1219, result.length);
		//
		result = compressable.compress(value);
		System.out.println("length: " + result.length);// 1219
		assertEquals(1219, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void uncompress() throws Exception {
		CompressProcessor compressable = new CompressProcessorImpl();
		compressable.setCompress(true);
		compressable.setCompressType(CompressType.LZ4);
		//
		String stringValue = new String(new byte[307200]);
		byte[] value = compressable.compress(ByteHelper
				.toByteArray(stringValue));
		//
		byte[] result = null;
		int count = 1000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = compressable.uncompress(7, value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
		//
		result = compressable.uncompress("LZ4", value);
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
		//
		result = compressable.uncompress(value);
		System.out.println("length: " + result.length);
		assertEquals(307200, result.length);
	}

}
