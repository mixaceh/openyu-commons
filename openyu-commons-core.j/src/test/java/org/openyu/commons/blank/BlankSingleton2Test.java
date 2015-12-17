package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class BlankSingleton2Test extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	/**
	 * Gets the single instance of BlankGetInstanceTest.
	 *
	 * @return single instance of BlankGetInstanceTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		BlankSingleton2 result = null;
		//
		result = BlankSingleton2.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}
}
