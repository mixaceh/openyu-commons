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
import com.esotericsoftware.kryo.Kryo;

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

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void getJavaVersion() {
		float result = 0f;
		//
		result = SystemHelper.getJavaVersion();
		System.out.println(result);
		assertTrue(result > 0f);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void getProperty() {
		String result = null;
		//
		result = SystemHelper.getProperty("file.encoding", "UTF-8");
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
		result = SystemHelper.setProperty("file.encoding", "UTF-8");
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
		result = SystemHelper.isJavaVersionAtLeast(1.2f);
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

}
