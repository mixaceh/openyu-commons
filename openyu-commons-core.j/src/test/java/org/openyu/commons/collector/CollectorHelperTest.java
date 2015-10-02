package org.openyu.commons.collector;

import static org.junit.Assert.assertNotNull;
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

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.07, time.warmup: 0.00,
	// time.bench: 0.07
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.collector.CollectorHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of CollectorHelperTest.
	 *
	 * @return single instance of CollectorHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		CollectorHelper result = null;
		//
		result = CollectorHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownInstance() {
		CollectorHelper instance = CollectorHelper.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		CollectorHelper closeInstance = CollectorHelper.shutdownInstance();
		assertNull(closeInstance);
		// 多次,不會丟出ex
		closeInstance = CollectorHelper.shutdownInstance();
		assertNull(closeInstance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartInstance() {
		CollectorHelper instance = CollectorHelper.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		CollectorHelper refreshInstance = CollectorHelper.restartInstance();
		assertNotNull(refreshInstance);
		// 多次,不會丟出ex
		refreshInstance = CollectorHelper.restartInstance();
		assertNotNull(refreshInstance);
	}

}
