package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BooleanHelperTest.
 */
public class BooleanHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void BooleanHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.BooleanHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		System.out.println(result);
		assertNull(result);
	}

	/**
	 * Creates the boolean.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.81, time.warmup: 0.80,
	// time.bench: 0.01
	public void createBoolean() {
		Boolean result = null;
		//
		result = BooleanHelper.createBoolean(true);
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = BooleanHelper.createBoolean(false);
		assertFalse(result);
		//
		result = BooleanHelper.createBoolean(null);
		assertFalse(result);
		//
		result = BooleanHelper.createBoolean("1");
		assertTrue(result);
		//
		result = BooleanHelper.createBoolean("0");
		assertFalse(result);
	}

	/**
	 * To boolean.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.81, time.warmup: 0.80,
	// time.bench: 0.01
	public void toBoolean() {
		boolean result = false;
		//
		result = BooleanHelper.toBoolean("true");
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = BooleanHelper.toBoolean("false");
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean(null);
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean("");
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean("on");
		assertTrue(result);
		//
		result = BooleanHelper.toBoolean("off");
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean("yes");
		assertTrue(result);
		//
		result = BooleanHelper.toBoolean("no");
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean("1");
		assertTrue(result);
		//
		result = BooleanHelper.toBoolean("0");
		assertFalse(result);
		//
		result = BooleanHelper.toBoolean("?");
		assertFalse(result);
	}

	/**
	 * Random booleanz.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.72, time.warmup: 0.71,
	// time.bench: 0.01
	public void randomBooleanz() {
		boolean result = false;
		//
		result = BooleanHelper.randomBoolean();
		//
		System.out.println(result);
	}

	/**
	 * Safe get.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.73,
	// time.bench: 0.01
	public void safeGet() {
		boolean result = false;
		//
		result = BooleanHelper.safeGet(Boolean.TRUE);
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = BooleanHelper.safeGet(null);
		assertFalse(result);
	}

	/**
	 * To stringz.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.73,
	// time.bench: 0.01
	public void toStringz() {
		String result = null;
		//
		result = BooleanHelper.toString(true);
		//
		System.out.println(result);
		assertEquals("1", result);
		//
		result = BooleanHelper.toString(false);
		assertEquals("0", result);
		//
		result = BooleanHelper.toString(null);
		assertEquals("0", result);
		//
		result = BooleanHelper.toString(Boolean.TRUE);
		assertEquals("1", result);
	}

	/**
	 * To int.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.73,
	// time.bench: 0.01
	public void toInt() {
		int result = -1;
		//
		result = BooleanHelper.toInt(true);
		//
		System.out.println(result);
		assertEquals(1, result);
		//
		result = BooleanHelper.toInt(false);
		assertEquals(0, result);
		//
		result = BooleanHelper.toInt(null);
		assertEquals(0, result);
		//
		result = BooleanHelper.toInt(Boolean.TRUE);
		assertEquals(1, result);
	}

	/**
	 * True false.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.73,
	// time.bench: 0.01
	public void trueFalse() {
		System.out.println(false & false & false);// false
		System.out.println(true & false & true);// false
		System.out.println(true & true & true);// true
		//
		System.out.println(false | false | false);// false
		System.out.println(true | false | true);// true
		System.out.println(true | true | true);// true
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.73,
	// time.bench: 0.01
	public void info() {
		Logger LOGGER = LoggerFactory.getLogger(BooleanHelper.class);
		LOGGER.info("abc");
	}

}
