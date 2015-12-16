package org.openyu.commons.collector;

import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class CollectorHelperTest.
 */
public class CollectorHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void CollectorHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.collector.CollectorHelper");
		//
		Object result = null;
		result = constructor.newInstance();
		assertNull(result);
	}
}
