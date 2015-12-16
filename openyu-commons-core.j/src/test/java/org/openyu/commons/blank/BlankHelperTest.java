package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class BlankHelperTest.
 */
public class BlankHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void BlankHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankHelper");
		//
		Object result = null;
		// Caused by: org.openyu.commons.helper.ex.HelperException: BlankHelper can not construct
		result = constructor.newInstance();
		assertNull(result);
	}
}
