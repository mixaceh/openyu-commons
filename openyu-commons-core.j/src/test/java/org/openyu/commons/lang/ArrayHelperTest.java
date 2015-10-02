package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class ArrayHelperTest.
 */
public class ArrayHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void ArrayHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.ArrayHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		assertNull(result);
	}

	/**
	 * Instance holder.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.ArrayHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of ArrayHelperTest.
	 *
	 * @return single instance of ArrayHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		ArrayHelper result = null;
		//
		result = ArrayHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Adds the unique.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void addUnique() {
		Class<?>[] x = new Class[] { String.class };
		Class<?>[] y = new Class[] { Integer.class };
		//
		Class<?>[] result = null;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.addUnique(x, y, Class[].class);
		}
		//
		SystemHelper.println(result);
		assertEquals(2, result.length);
		//
		y = new Class[] { String.class };
		result = ArrayHelper.addUnique(x, y, Class[].class);
		assertEquals(1, result.length);
		//
		result = ArrayHelper.addUnique(null, y, Class[].class);
		assertEquals(1, result.length);
		//
		result = ArrayHelper.addUnique(x, null, Class[].class);
		assertEquals(1, result.length);
		//
		String[] stringResult = ArrayHelper.addUnique(new String[] { "a", "b",
				null }, new String[] { "c", null }, String[].class);
		SystemHelper.println(stringResult);
		assertEquals(4, stringResult.length);
	}

	/**
	 * Adds the object array.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void addObjectArray() {
		Class<?>[] x = new Class[] { String.class };
		Class<?>[] y = new Class[] { String.class };
		//
		Class<?>[] result = null;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.add(x, y, Class[].class);
		}
		//
		SystemHelper.println(result);
		assertEquals(2, result.length);
		//
		y = new Class[] { String.class };
		result = ArrayHelper.add(x, y, Class[].class);
		assertEquals(2, result.length);
		//
		result = ArrayHelper.add(null, y, Class[].class);
		assertEquals(1, result.length);
		//
		result = ArrayHelper.add(x, null, Class[].class);
		assertEquals(1, result.length);
	}

	/**
	 * Adds the byte array.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// 1000000 times: 12 mills.
	// round: 0.01, GC: 1
	public void addByteArray() {
		byte[] x = new byte[] { 1, 2, 3 };
		byte[] y = new byte[] { 4, 5, 6 };
		//
		byte[] result = null;
		final int COUNT = 10000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.add(x, y);
		}
		long end = System.currentTimeMillis();
		System.out.println(COUNT + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result); // 1, 2, 3, 4, 5, 6
		assertEquals(6, result.length);
		//
		y = new byte[] { 7, 8 };
		result = ArrayHelper.add(result, y);
		SystemHelper.println(result); // 1, 2, 3, 4, 5, 6
		assertEquals(8, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void addTwoDimension() {
		byte[] aaa = new byte[] { 0, 1, 2 };
		byte[] bbb = new byte[] { 10, 20, 30 };
		//
		byte[] FLASH_EOF_BYTES = new byte[] { 0x00 };

		final int COUNT = 10000;
		long beg = System.currentTimeMillis();
		boolean result = false;
		for (int i = 0; i < COUNT; i++) {

		}
		byte[][] newBuffs = ArrayHelper.add(FLASH_EOF_BYTES, aaa, bbb);
		SystemHelper.println(newBuffs);
	}

	/**
	 * Checks if is array.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void isArray() {
		String xxx = "xxx";
		System.out.println(xxx);
		String[] value = new String[] { "134", "adb", "d555" };
		// System.out.println("String[]: "+value);
		System.out.println(value.getClass().getName());
		//
		System.out.println("byte[]: " + new byte[0].getClass().getName());
		System.out.println("short[]: " + new short[0].getClass().getName());

		//
		final int COUNT = 10000;
		long beg = System.currentTimeMillis();
		boolean result = false;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.isArray(new int[0][0]);
			System.out.println(result);
			result = ArrayHelper.isArray(new List[0][0][0]);
			System.out.println(result);
			result = ArrayHelper.isArray(null);
			System.out.println(result);
			result = ArrayHelper.isArray(new Date());
			System.out.println(result);
		}
		long end = System.currentTimeMillis();
		System.out.println(COUNT + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(ArrayHelper.getDimension(new Date()));
		System.out.println(ArrayHelper.getDimension(new int[0]));
	}

	/**
	 * Gets the first entry.
	 *
	 * @return the first entry
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void getFirstEntry() {
		Object[] values = new Object[] { "aaa", "bbb" };

		Object[] objects = null;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			objects = ArrayHelper.getFirstEntry(values);
		}
		//
		SystemHelper.println(objects);
		assertEquals(String.class, objects[0]);
		assertEquals("aaa", objects[1]);
		//
		objects = ArrayHelper.getFirstEntry(null);
		assertNull(objects[0]);
		assertNull(objects[1]);
		//
		values = new Object[] { new String[] { "aaa" }, "bbb" };
		objects = ArrayHelper.getFirstEntry(values);
		assertEquals(String.class, objects[0]);
		assertEquals("aaa", objects[1]);
		//
		values = new Object[] { null, "bbb" };
		objects = ArrayHelper.getFirstEntry(values);
		assertEquals(String.class, objects[0]);
		assertEquals("bbb", objects[1]);
		//
		values = new Object[] {};
		objects = ArrayHelper.getFirstEntry(values);
		assertNull(objects[0]);
		assertNull(objects[1]);
	}

	/**
	 * Contains.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void contains() {
		String[] values = new String[] { "aaa", "bbb" };
		//
		boolean result = false;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.contains(values, "aaa");
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = ArrayHelper.contains(null, "aaa");
		assertFalse(result);
	}

	/**
	 * Gets the.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void get() {
		String[] values = new String[] { "aaa", "bbb" };

		Object result = null;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.get(values, 0);
		}
		//
		System.out.println(result);
		assertNotNull(result);
		//
		result = ArrayHelper.get(null, 0);
		assertNull(result);
		//
		result = ArrayHelper.get(values, 99);
		assertNull(result);
	}

	/**
	 * Sets the.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void set() {
		String[] values = new String[2];

		boolean result = false;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.set(values, 1, "111");
		}
		//
		System.out.println(result);
		SystemHelper.println(values);
		assertTrue(result);
		//
		result = ArrayHelper.set(null, 1, "111");
		assertFalse(result);
		//
		result = ArrayHelper.set(values, 99, "111");
		assertFalse(result);
	}

	/**
	 * Checks if is empty.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void isEmpty() {
		String[] values = new String[2];

		boolean result = false;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.isEmpty(values);
		}
		//
		System.out.println(result);
		assertFalse(result);
		//
		result = ArrayHelper.isEmpty(null);
		assertTrue(result);
		result = ArrayHelper.isEmpty(new String[] {});
		assertTrue(result);
	}

	/**
	 * Checks if is not empty.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void notEmpty() {
		String[] values = new String[2];

		boolean result = false;
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = ArrayHelper.notEmpty(values);
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = ArrayHelper.notEmpty(null);
		assertFalse(result);
		result = ArrayHelper.notEmpty(new String[] {});
		assertFalse(result);
	}

}
