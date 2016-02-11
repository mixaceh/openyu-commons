package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class BlankSingletonTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void BlankSingleton() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankSingleton");
		//
		Object result = null;
		// Caused by: java.lang.SecurityException: BlankGetInstance can not
		// construct
		result = constructor.newInstance();
		assertNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.07, time.warmup: 0.00,
	// time.bench: 0.07
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankSingleton$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of BlankGetInstanceTest.
	 *
	 * @return single instance of BlankGetInstanceTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		BlankSingleton result = null;
		//
		result = BlankSingleton.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownInstance() {
		BlankSingleton instance = BlankSingleton.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankSingleton.shutdownInstance();
		System.out.println(instance);
		assertNull(instance);
		// 多次,不會丟出ex
		instance = BlankSingleton.shutdownInstance();
		System.out.println(instance);
		assertNull(instance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartInstance() {
		BlankSingleton instance = BlankSingleton.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankSingleton.restartInstance();
		System.out.println(instance);
		assertNotNull(instance);
		// 多次,不會丟出ex
		instance = BlankSingleton.restartInstance();
		System.out.println(instance);
		assertNotNull(instance);
	}
}
