package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class SystemHelperTest.
 */
public class SystemHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void SystemHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.SystemHelper");
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
	// round: 0.00
	public void InstanceHolder() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.SystemHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of SystemHelperTest.
	 *
	 * @return single instance of SystemHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		SystemHelper result = null;
		//
		result = SystemHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void getJavaVersion() {
		float result = 0f;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = SystemHelper.getJavaVersion();
		}
		//
		System.out.println(result);
		assertTrue(result > 0f);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getJavaVersionAsFloat() throws Exception {
		SystemHelper object = SystemHelper.getInstance();
		Method method = getDeclaredMethod(object.getClass(),
				"getJavaVersionAsFloat");
		float result = 0f;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = (Float) method.invoke(object);
		}
		//
		System.out.println(result);
		assertTrue(result > 0f);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getJavaVersionAsInt() throws Exception {
		SystemHelper object = SystemHelper.getInstance();
		Method method = getDeclaredMethod(SystemHelper.class,
				"getJavaVersionAsInt");
		int result = 0;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = (Integer) method.invoke(object);
		}
		//
		System.out.println(result);
		assertTrue(result > 0);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getJavaVersionMatches() throws Exception {
		SystemHelper object = SystemHelper.getInstance();
		Method method = getDeclaredMethod(SystemHelper.class,
				"getJavaVersionMatches", String.class);
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = (Boolean) method.invoke(object, "1.6");
		}
		//
		System.out.println(result);
		// assertTrue(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getOSMatches() throws Exception {
		SystemHelper object = SystemHelper.getInstance();
		Method method = getDeclaredMethod(SystemHelper.class, "getOSMatches",
				String.class);
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = (Boolean) method.invoke(object, "Windows 7");
		}
		//
		System.out.println(result);
		// assertTrue(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void getProperty() {
		String result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = SystemHelper.getProperty("file.encoding", "UTF-8");
		}
		//
		System.out.println(result);
		assertEquals("UTF-8", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void setProperty() {
		String result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = SystemHelper.setProperty("file.encoding", "UTF-8");
		}
		//
		System.out.println(result);
		assertEquals("UTF-8", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void isJavaVersionAtLeast() {
		boolean result = false;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = SystemHelper.isJavaVersionAtLeast(1.2f);
		}
		//
		System.out.println(result);
		assertTrue(result);
		//
		//
		result = SystemHelper.isJavaVersionAtLeast(9.9f);
		assertFalse(result);
		//
		result = SystemHelper.isJavaVersionAtLeast(1);
		assertTrue(result);
		//
		result = SystemHelper.isJavaVersionAtLeast(999);
		assertFalse(result);
	}

	/**
	 * Println.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void println() {
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			SystemHelper.println("Test");
		}
		//
		SystemHelper.println(new Integer[] { 1, 2, 3 });
	}

}
