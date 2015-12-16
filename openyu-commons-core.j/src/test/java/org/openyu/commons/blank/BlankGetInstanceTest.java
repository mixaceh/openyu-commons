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
 * The Class BlankGetInstanceTest.
 */
public class BlankGetInstanceTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void BlankGetInstance() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankGetInstance");
		//
		Object result = null;
		//Caused by: java.lang.SecurityException: BlankGetInstance can not construct
		result = constructor.newInstance();
		assertNull(result);
	}

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.07, time.warmup: 0.00,
	// time.bench: 0.07
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankGetInstance$InstanceHolder");
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
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		BlankGetInstance result = null;
		//
		result = BlankGetInstance.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownInstance() {
		BlankGetInstance instance = BlankGetInstance.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankGetInstance.shutdownInstance();
		System.out.println(instance);
		assertNull(instance);
		// 多次,不會丟出ex
		instance = BlankGetInstance.shutdownInstance();
		System.out.println(instance);
		assertNull(instance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartInstance() {
		BlankGetInstance instance = BlankGetInstance.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankGetInstance.restartInstance();
		System.out.println(instance);
		assertNotNull(instance);
		// 多次,不會丟出ex
		instance = BlankGetInstance.restartInstance();
		System.out.println(instance);
		assertNotNull(instance);
	}
}
