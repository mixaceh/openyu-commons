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

public class CharHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void CharHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.CharHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		System.out.println(result);
		assertNull(result);
	}

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.CharHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of CharHelperTest.
	 *
	 * @return single instance of CharHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		CharHelper result = null;
		//
		result = CharHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void toChar() {
		String value = "abc";
		char result = 0;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.toChar(value, 0, '0');
		}
		//
		System.out.println(result);
		assertEquals(97, result);
		//
		result = CharHelper.toChar(null, 99, '0');
		assertEquals('0', result);
		//
		result = CharHelper.toChar(value, 99, '0');
		assertEquals('0', result);
		//
		result = CharHelper.toChar(value);
		assertEquals(97, result);
		//
		result = CharHelper.toChar(value, 0);
		assertEquals(97, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void equals() {
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.equals(0, 0);
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.equals(0, 1);
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void equalsIgnoreCase() {
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.equalsIgnoreCase(65, 97);
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.equalsIgnoreCase(65, 0);
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void contains() {
		char[] value = { 97, 98, 99 };// a,b,c
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.contains(value, 'a');
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.contains(value, '0');
		assertFalse(result);
		//
		result = CharHelper.contains((char[]) null, '0');
		assertFalse(result);
		//
		System.out.println('0');
		System.out.println(Integer.valueOf('a'));// 97

		// ---------------------------------
		int[] value2 = { 97, 98, 99 };
		result = CharHelper.contains(value2, 'a');
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.contains(value2, '0');
		assertFalse(result);
		//
		result = CharHelper.contains((int[]) null, '0');
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void containsIgnoreCase() {
		char[] value = { 97, 98, 99 };// a,b,c
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.containsIgnoreCase(value, 'A');
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.containsIgnoreCase(value, '0');
		assertFalse(result);
		//
		result = CharHelper.containsIgnoreCase((char[]) null, '0');
		assertFalse(result);
		//
		System.out.println('0');
		System.out.println(Integer.valueOf('A'));// 65

		// ---------------------------------
		int[] value2 = { 97, 98, 99 };
		result = CharHelper.containsIgnoreCase(value2, 'A');
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = CharHelper.containsIgnoreCase(value2, '0');
		assertFalse(result);
		//
		result = CharHelper.containsIgnoreCase((int[]) null, '0');
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void randomCharz() {
		char result = 0;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.randomChar();
		}
		//
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void safeGet() {
		char result = 0;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.safeGet(new Character((char) 97));
		}
		//
		System.out.println(result);
		//
		result = CharHelper.safeGet(null);
		assertEquals(0, result);
		//
		System.out.println('\u0000');// ' '
		System.out.println('0');// 0
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void toStringz() {
		String result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = CharHelper.toString(new Character((char) 97));
		}
		//
		System.out.println(result);
		//
		result = CharHelper.toString(null);
		assertNull(result);
	}
}
