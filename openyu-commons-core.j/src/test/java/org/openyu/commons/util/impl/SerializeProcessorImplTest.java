package org.openyu.commons.util.impl;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.util.SerializeProcessor;
import org.openyu.commons.util.SerializeType;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SerializeProcessorImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	public static LinkedList<String> mockLinkedList() {
		LinkedList<String> result = new LinkedList<String>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new String(new byte[307200]));// 300k
		return result;
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.74 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.74, time.warmup: 0.00,
	// time.bench: 0.74
	public void writeToXml() {
		SerializeProcessor processor = new SerializeProcessorImpl();
		//
		processor.setSerialize(true);
		processor.setSerializeType(SerializeType.JDK);
		//
		String result = CollectorHelper.writeToXml(
				SerializeProcessorImpl.class, processor);
		System.out.println(result);
		assertNotNull(result);
		//
		SerializeProcessor compare = new SerializeProcessorImpl();
		assertTrue(processor.getSerializeTypes() == compare.getSerializeTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.69 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.69, time.warmup: 0.00,
	// time.bench: 0.69
	public void readFromXml() {
		SerializeProcessor processor = CollectorHelper
				.readFromXml(SerializeProcessorImpl.class);
		System.out.println(processor);
		assertNotNull(processor);
		//
		SerializeProcessor compare = new SerializeProcessorImpl();
		assertTrue(processor.getSerializeTypes() == compare.getSerializeTypes());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void serialize() {
		SerializeProcessor processor = new SerializeProcessorImpl();
		processor.setSerialize(true);
		processor.setSerializeType(SerializeType.JDK);
		//
		LinkedList<String> value = mockLinkedList();
		byte[] result = null;
		//
		result = processor.serialize(2, value);
		//
		System.out.println("length: " + result.length);
		assertEquals(614486, result.length);
		//
		result = processor.serialize("JDK", value);
		System.out.println("length: " + result.length);
		assertEquals(614486, result.length);
		//
		processor.setSerializeType(SerializeType.KRYO);
		result = processor.serialize(value);
		System.out.println("length: " + result.length);
		assertEquals(307235, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void deserialize() {
		SerializeProcessor processor = new SerializeProcessorImpl();
		processor.setSerialize(true);
		processor.setSerializeType(SerializeType.JDK);
		//
		LinkedList<String> list = mockLinkedList();
		byte[] value = processor.serialize(list);

		List<String> result = null;
		//
		result = processor.deserialize(2, value);
		//
		System.out.println("size: " + result.size());
		assertCollectionEquals(list, result);
		//
		result = processor.deserialize("JDK", value);
		System.out.println("size: " + result.size());
		assertCollectionEquals(list, result);
		//
		result = processor.deserialize(value);
		System.out.println("size: " + result.size());
		assertCollectionEquals(list, result);
	}

}
