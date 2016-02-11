package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

import org.openyu.commons.util.SearchHelper;

/**
 * The Class SearchHelperTest.
 */
public class SearchHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void SearchHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.util.SearchHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		System.out.println(result);
		assertNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1000000, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.13, time.total: 1.90, time.warmup: 0.09,
	// time.bench: 1.81
	public void sequential() {
		// 不需排序
		int[] value = new int[] { 10, 4, 9, 1, 2, 5, 3, 8, 7, 6 };
		int result = SearchHelper.sequential(value, 7);
		// System.out.println(result);// 8
		assertEquals(8, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1000000, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.14, time.total: 1.62, time.warmup: 0.10,
	// time.bench: 1.52
	public void iterative() {
		// 需先排序
		int[] value = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int result = SearchHelper.iterative(value, 7);
		// System.out.println(result);// 6
		assertEquals(6, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1000000, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.13, time.total: 1.92, time.warmup: 0.09,
	// time.bench: 1.83
	public void divideAndConquer() {
		// 需先排序
		int[] value = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int result = SearchHelper.divideAndConquer(value, 7);
		// System.out.println(result);// 6
		assertEquals(6, result);
	}
}
