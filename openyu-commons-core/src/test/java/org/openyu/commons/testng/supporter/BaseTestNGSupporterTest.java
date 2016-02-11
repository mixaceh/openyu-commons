package org.openyu.commons.testng.supporter;

//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.openyu.commons.testng.supporter.BaseTestNGSupporter;

/**
 * The Class BaseTestNGSupporterTest.
 */
public class BaseTestNGSupporterTest {

//	/** The benchmark rule. */
//	@Rule
//	public BenchmarkRule benchmarkRule = new BenchmarkRule();
//
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
//	// round: 0.00
//	public void printBeans() throws Exception {
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			BaseTestNGSupporter.printBeans();
//		}
//	}
//
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
//	// round: 0.01
//	public void getDeclaredConstructor() throws Exception {
//		Constructor<?> constructor = BaseTestNGSupporter
//				.getDeclaredConstructor(BaseTestNGSupporter.class);
//		Object result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = constructor.newInstance();
//		}
//		//
//		System.out.println(result);
//		//
//		constructor = BaseTestNGSupporter.getDeclaredConstructor(
//				BaseTestNGSupporter.class, Integer.class);
//		assertNull(constructor);
//		//
//		constructor = BaseTestNGSupporter.getDeclaredConstructor((Class<?>) null);
//		assertNull(constructor);
//		//
//		constructor = BaseTestNGSupporter
//				.getDeclaredConstructor("org.openyu.commons.testng.supporter.BaseTestNGSupporter");
//		assertNotNull(constructor);
//		//
//		constructor = BaseTestNGSupporter.getDeclaredConstructor("xxx");
//		assertNull(constructor);
//		//
//		constructor = BaseTestNGSupporter.getDeclaredConstructor((String) null);
//		assertNull(constructor);
//	}
//
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
//	// round: 0.01
//	public void getDeclaredMethod() throws Exception {
//		BaseTestNGSupporter object = new BaseTestNGSupporter();
//		Method method = BaseTestNGSupporter.getDeclaredMethod(
//				BaseTestNGSupporter.class, "printBeans");
//		Object result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = method.invoke(object);
//		}
//		//
//		System.out.println(result);
//		//
//		method = BaseTestNGSupporter.getDeclaredMethod(null, "printBeans");
//		assertNull(method);
//		//
//		method = BaseTestNGSupporter.getDeclaredMethod(BaseTestNGSupporter.class,
//				"xxx");
//		assertNull(method);
//	}
//
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
//	// round: 0.01
//	public void getDeclaredField() throws Exception {
//		BaseTestNGSupporter object = new BaseTestNGSupporter();
//		Field field = BaseTestNGSupporter.getDeclaredField(
//				BaseTestNGSupporter.class, "applicationContext");
//		Object result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = field.get(object);
//		}
//		//
//		System.out.println(result);
//		//
//		field = BaseTestNGSupporter.getDeclaredField(null, "applicationContext");
//		assertNull(field);
//		//
//		field = BaseTestNGSupporter.getDeclaredField(BaseTestNGSupporter.class,
//				"xxx");
//		assertNull(field);
//	}
}

