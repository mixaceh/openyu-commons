package org.openyu.commons.junit.supporter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * The Class BaseTestSupporterTest.
 */
public class BaseTestSupporterTest {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.00
	public void printBeans() throws Exception {
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			BaseTestSupporter.printBean();
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getDeclaredConstructor() throws Exception {
		Constructor<?> constructor = BaseTestSupporter
				.getDeclaredConstructor(BaseTestSupporter.class);
		Object result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = constructor.newInstance();
		}
		//
		System.out.println(result);
		//
		constructor = BaseTestSupporter.getDeclaredConstructor(
				BaseTestSupporter.class, Integer.class);
		assertNull(constructor);
		//
		constructor = BaseTestSupporter.getDeclaredConstructor((Class<?>) null);
		assertNull(constructor);
		//
		constructor = BaseTestSupporter
				.getDeclaredConstructor("org.openyu.commons.junit.supporter.BaseTestSupporter");
		assertNotNull(constructor);
		//
		constructor = BaseTestSupporter.getDeclaredConstructor("xxx");
		assertNull(constructor);
		//
		constructor = BaseTestSupporter.getDeclaredConstructor((String) null);
		assertNull(constructor);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getDeclaredMethod() throws Exception {
		BaseTestSupporter object = new BaseTestSupporter();
		Method method = BaseTestSupporter.getDeclaredMethod(
				BaseTestSupporter.class, "printBeans");
		Object result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = method.invoke(object);
		}
		//
		System.out.println(result);
		//
		method = BaseTestSupporter.getDeclaredMethod(null, "printBeans");
		assertNull(method);
		//
		method = BaseTestSupporter.getDeclaredMethod(BaseTestSupporter.class,
				"xxx");
		assertNull(method);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.01
	public void getDeclaredField() throws Exception {
		BaseTestSupporter object = new BaseTestSupporter();
		Field field = BaseTestSupporter.getDeclaredField(
				BaseTestSupporter.class, "applicationContext");
		Object result = null;
		//
		final int COUNT = 10000;
		for (int i = 0; i < COUNT; i++) {
			result = field.get(object);
		}
		//
		System.out.println(result);
		//
		field = BaseTestSupporter.getDeclaredField(null, "applicationContext");
		assertNull(field);
		//
		field = BaseTestSupporter.getDeclaredField(BaseTestSupporter.class,
				"xxx");
		assertNull(field);
	}
}
