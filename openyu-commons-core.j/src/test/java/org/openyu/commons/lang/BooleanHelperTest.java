package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class BooleanHelperTest.
 */
public class BooleanHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00
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
	 * Instance holder.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.BooleanHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of BooleanHelperTest.
	 *
	 * @return single instance of BooleanHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	public void getInstance() {
		BooleanHelper result = null;
		//
		result = BooleanHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Creates the boolean.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void createBoolean() {
		Boolean result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.createBoolean(true);
		}
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
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void toBoolean() {
		boolean result = false;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.toBoolean("true");
		}
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
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void randomBooleanz() {
		boolean result = false;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.randomBoolean();
		}
		//
		System.out.println(result);
	}

	/**
	 * Safe get.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void safeGet() {
		boolean result = false;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.safeGet(Boolean.TRUE);
		}
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
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void toStringz() {
		String result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.toString(true);
		}
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
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void toInt() {
		int result = -1;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = BooleanHelper.toInt(true);
		}
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

	// @Test
	// @BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// // round: 0.00
	// public void readResolve() throws Exception {
	// BooleanHelper object = BooleanHelper.getInstance();
	// Method method = getDeclaredMethod(BooleanHelper.Class,
	// "readResolve");
	// //
	// Object result = null;
	// //
	// final int COUNT = 1000;
	// for (int i = 0; i < COUNT; i++) {
	// result = method.invoke(object);
	// }
	// //
	// System.out.println(result);
	// assertNotNull(result);
	// }

	/**
	 * True false.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	public void trueFalse() {
		System.out.println(false & false & false);// false
		System.out.println(true & false & true);// false
		System.out.println(true & true & true);// true
		//
		System.out.println(false | false | false);// false
		System.out.println(true | false | true);// true
		System.out.println(true | true | true);// true
	}
}
