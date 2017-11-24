package org.openyu.commons.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.util.memory.MemoryCounter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.esotericsoftware.kryo.Kryo;

/**
 * The Class MemoryHelperTest.
 */
public class MemoryHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void sizeOf() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
		//
		long result = MemoryHelper.sizeOf(list);
		// 2076
		System.out.println("List mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		String stringVaule = "abc";
		// 0
		result = MemoryHelper.sizeOf(stringVaule);
		System.out.println("String mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		Date date = new Date();
		result = MemoryHelper.sizeOf(date);
		// 24
		System.out.println("Date mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		Integer integerValue = new Integer(0);
		result = MemoryHelper.sizeOf(integerValue);
		// 16
		System.out.println("Integer mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		Kryo kryo = new Kryo();
		result = MemoryHelper.sizeOf(kryo);
		// 759579
		System.out.println("Kryo mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		MemoryCounter memCounter = new MemoryCounter();
		result = MemoryHelper.sizeOf(memCounter);
		// 408
		System.out.println("MemoryCounter mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
		//
		StringBuilder buff = new StringBuilder();
		result = MemoryHelper.sizeOf(buff);
		// 408
		System.out.println("StringBuilder mem size: " + result + " bytes, " + ByteUnit.BYTE.toKB(result) + " KB");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void println() {
		MemoryHelper.println("Test");
		MemoryHelper.println("Title: ", 100);
		//
		MemoryHelper.println(new Integer[] { 1, 2, 3 });
		MemoryHelper.println(new byte[] { 1, 2, 3 });
		MemoryHelper.println(new Kryo());
	}
}
