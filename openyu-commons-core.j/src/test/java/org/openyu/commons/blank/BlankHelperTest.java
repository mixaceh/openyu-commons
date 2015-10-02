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
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.blank.BlankHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of BlankHelperTest.
	 *
	 * @return single instance of BlankHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		BlankHelper result = null;
		//
		result = BlankHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownInstance() {
		BlankHelper instance = BlankHelper.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankHelper.shutdownInstance();
		assertNull(instance);
		// 多次,不會丟出ex
		instance = BlankHelper.shutdownInstance();
		assertNull(instance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartInstance() {
		BlankHelper instance = BlankHelper.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BlankHelper.restartInstance();
		assertNotNull(instance);
		// 多次,不會丟出ex
		instance = BlankHelper.restartInstance();
		assertNotNull(instance);
	}

}
