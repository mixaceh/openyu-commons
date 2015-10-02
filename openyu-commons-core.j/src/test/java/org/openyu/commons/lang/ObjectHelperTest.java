package org.openyu.commons.lang;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ObjectHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void ObjectHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.ObjectHelper");
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
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.ObjectHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	// 1000000 times: 92 mills.
	// 1000000 times: 91 mills.
	// 1000000 times: 90 mills.
	public void equals() {
		Class<?>[] x = new Class[] { String.class };
		Class<?>[] y = new Class[] { String.class };

		boolean result = false;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ObjectHelper.equals(x, y);// true
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(true, result);
		//
		y = new Class[] { Integer.class };
		result = ObjectHelper.equals(x, y);// false
		System.out.println(result);
		//
		y = new Class[] { String.class, Integer.class };
		result = ObjectHelper.equals(x, y);// false
		System.out.println(result);
		//
		x = new Class[] { String.class, Integer.class };
		result = ObjectHelper.equals(x, y);// true
		System.out.println(result);
	}

	@Test
	// 1000000 times: 92 mills.
	// 1000000 times: 91 mills.
	// 1000000 times: 90 mills.
	public void toStringz() {
		double[] values = new double[] { 0d, 1d, 2d };

		String result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ObjectHelper.toString(values);// 0.0, 1.0, 2.0
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("0.0, 1.0, 2.0", result);
		//

	}

	@Test
	// 1000000 times: 92 mills.
	// 1000000 times: 91 mills.
	// 1000000 times: 90 mills.
	public void toStringByArray() {
		double[][] values = new double[][] { { 0d, 0d }, { 0d, 1d } };
		// double[][][] values = new double[][][] { { { 0d, 0d }, { 0d, 1d } },
		// { { 1d, 0d }, { 1d, 1d } } };
		String result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ObjectHelper.toString(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("0.0, 0.0, 0.0, 1.0", result);
		//
		String[] stringValues = new String[] { "aaa", "bbb", "ccc" };
		result = ObjectHelper.toString(stringValues);
		System.out.println(result);
	}
}
