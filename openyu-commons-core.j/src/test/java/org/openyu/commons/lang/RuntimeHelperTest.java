package org.openyu.commons.lang;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class RuntimeHelperTest.
 */
public class RuntimeHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void RuntimeHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.RuntimeHelper");
		//
		Object result = null;
		result = constructor.newInstance();
		assertNull(result);
	}

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.RuntimeHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of RuntimeHelperTest.
	 *
	 * @return single instance of RuntimeHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		RuntimeHelper result = null;
		//
		result = RuntimeHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void usedMemory() {
		long result = RuntimeHelper.usedMemory();
		System.out.println(result);
		assertTrue(result > 0);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void gc() {
		RuntimeHelper.gc();
		System.out.println(RuntimeHelper.usedMemory());
	}

}
