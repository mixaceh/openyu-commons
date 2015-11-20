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

import org.openyu.commons.util.SortHelper;

/**
 * The Class SortHelperTest.
 */
public class SortHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void SortHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.util.SortHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		System.out.println(result);
		assertNull(result);
	}

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.util.SortHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of SortHelperTest.
	 *
	 * @return single instance of SortHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		SortHelper result = null;
		//
		result = SortHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Selection.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void selection() {
		int[] value = new int[] { 10, 4, 9, 1, 2, 5, 3, 8, 7, 6 };
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			SortHelper.selection(value);
		}
		//
		for (int entry : value) {
			System.out.println(entry);
		}
		//
		assertEquals(1, value[0]);
		assertEquals(2, value[1]);
		assertEquals(6, value[5]);
	}

	/**
	 * Insertion.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.14
	public void insertion() {
		int[] value = new int[] { 10, 6, 9, 1, 2, 5, 3, 8, 7, 4 };

		int count = 10000;
		for (int i = 0; i < count; i++) {
			SortHelper.insertion(value);
		}
		//
		for (int entry : value) {
			System.out.println(entry);
		}
		//
		assertEquals(1, value[0]);
		assertEquals(2, value[1]);
		assertEquals(6, value[5]);
	}

	/**
	 * Bubble.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.07
	public void bubble() {
		int[] value = new int[] { 10, 6, 9, 1, 2, 5, 3, 8, 7, 4 };

		int count = 10000;
		for (int i = 0; i < count; i++) {
			SortHelper.bubble(value);
		}
		//
		for (int entry : value) {
			System.out.println(entry);
		}
		//
		assertEquals(1, value[0]);
		assertEquals(2, value[1]);
		assertEquals(6, value[5]);
	}
}
