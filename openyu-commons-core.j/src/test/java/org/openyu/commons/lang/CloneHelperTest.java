package org.openyu.commons.lang;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CloneHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	// no cache
	// 1000000 times: 515 mills.
	// 1000000 times: 537 mills.
	// 1000000 times: 509 mills.
	//
	// cache
	// 1000000 times: 533 mills.
	// 1000000 times: 531 mills.
	// 1000000 times: 530 mills.
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.42 [+- 0.04], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.43, time.warmup: 0.00,
	// time.bench: 0.43
	public void genericClone() {
		Date value = new Date();
		System.out.println("value: " + value);
		//
		Date cloneValue = null;

		cloneValue = CloneHelper.genericClone(value);
		//
		cloneValue.setYear(50);// 1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	// 1000000 times: 1771 mills.
	// 1000000 times: 1778 mills.
	// 1000000 times: 1818 mills.
	//
	// #fix
	// 1000000 times: 1311 mills.
	// 1000000 times: 1316 mills.
	// 1000000 times: 1327 mills.
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void deepCloneWithArray() {
		Object[] values = new Object[] { new Date(), "aaa", 123 };
		SystemHelper.println(values);
		//
		Object[] cloneValues = null;
		// cloneValue = CloneHelper.genericClone(value);
		cloneValues = CloneHelper.deepClone(values);
		//
		Date date = (Date) cloneValues[0];
		date.setYear(50);// 1950
		SystemHelper.println(cloneValues);// 1950
		SystemHelper.println(values);// 今天日期
	}

	@Test
	// 1000000 times: 1288 mills.
	// 1000000 times: 1292 mills.
	// 1000000 times: 1297 mills.
	//
	// #fix
	// 1000000 times: 1012 mills.
	// 1000000 times: 1016 mills.
	// 1000000 times: 1015 mills.
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void deepCloneWithList() {
		List value = new LinkedList();
		value.add(new Date());
		value.add(Locale.TRADITIONAL_CHINESE);
		System.out.println("value: " + value);
		//
		List cloneValue = null;
		// cloneValue = CloneHelper.genericClone(value);
		cloneValue = CloneHelper.deepClone(value);
		//
		Date date = (Date) cloneValue.get(0);
		date.setYear(50);// 1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	// 1000000 times: 1771 mills.
	// 1000000 times: 1778 mills.
	// 1000000 times: 1818 mills.
	//
	// #fix
	// 1000000 times: 1311 mills.
	// 1000000 times: 1316 mills.
	// 1000000 times: 1327 mills.
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void deepCloneWithMap() {
		Map value = new LinkedHashMap();
		value.put(1, new Date());
		value.put(2, Locale.TRADITIONAL_CHINESE);
		System.out.println("value: " + value);
		//
		Map cloneValue = null;
		// cloneValue = CloneHelper.genericClone(value);
		cloneValue = CloneHelper.deepClone(value);
		//
		Date date = (Date) cloneValue.get(1);
		date.setYear(50);// 1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.38 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.39, time.warmup: 0.00,
	// time.bench: 0.39

	// cloner
	// round: 0.09 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.10, time.warmup: 0.00,
	// time.bench: 0.10
	public void cloneWithList() {
		List<String> value = new LinkedList<String>();
		value.add("aaa");
		value.add("bbb");
		value.add("ccc");
		System.out.println("value: " + value);
		//
		List<String> result = null;
		result = CloneHelper.clone(value);
		//
		result.remove(0);
		//
		System.out.println("old: " + value);
		System.out.println("new: " + result);
		assertTrue(value.size() == 3);
	}
}
