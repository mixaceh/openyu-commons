package org.openyu.commons.testng.supporter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.context.ApplicationContext;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeTest;

import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import org.openyu.commons.testng.BaseTestNG;
import org.openyu.commons.mark.Supporter;

//import static org.testng.Assert.assertNull;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertEquals;

/**
 * The Class BaseTestNGSupporter.
 *
 * benchmark jvm args
 *
 * -Djub.consumers=CONSOLE,H2 -Djub.db.file=.benchmarks
 *
 */
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark/org.openyu.commons.testng.supporter.BaseTestNGSupporter")
@BenchmarkHistoryChart(filePrefix = "benchmark/org.openyu.commons.testng.supporter.BaseTestNGSupporterHistory")
public class BaseTestNGSupporter implements BaseTestNG, Supporter {

	/** The application context. */
	protected static ApplicationContext applicationContext;

	/**
	 * Instantiates a new base test supporter.
	 */
	public BaseTestNGSupporter() {
	}

	// @BeforeMethod
	// public void beforeMethod() {
	// }

	// @AfterMethod
	// public void afterMethod() {
	// }
	//
	// @BeforeClass
	// public void beforeClass() {
	// }
	//
	// @AfterClass
	// public void afterClass() {
	// }
	//
	// @BeforeTest
	// public void beforeTest() {
	// }
	//
	// @AfterTest
	// public void afterTest() {
	// }

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------

	// protected static boolean randomBoolean() {
	// return BooleanHelper.randomBoolean();
	// }
	//
	// protected static String randomUnique() {
	// return StringHelper.randomUnique();
	// }
	//
	// protected static String randomAlphabet() {
	// return StringHelper.randomAlphabet();
	// }
	//
	// protected static String randomAlphabet(int length) {
	// return StringHelper.randomString(length);
	// }
	//
	// protected static String randomString() {
	// return StringHelper.randomString();
	// }
	//
	// protected static String randomString(int length) {
	// return StringHelper.randomString(length);
	// }
	//
	// protected static String randomIp(String prefixIp) {
	// return StringHelper.randomIp(prefixIp);
	// }
	//
	// protected static char randomChar() {
	// return CharHelper.randomChar();
	// }
	//
	// protected static byte randomByte() {
	// return NumberHelper.randomByte();
	// }
	//
	// protected static short randomShort() {
	// return NumberHelper.randomShort();
	// }
	//
	// protected static int randomInt() {
	// return NumberHelper.randomInt();
	// }
	//
	// protected static int randomInt(int maxValue) {
	// return NumberHelper.randomInt(maxValue);
	// }
	//
	// protected static int randomInt(int minValue, int maxValue) {
	// return NumberHelper.randomInt(minValue, maxValue);
	// }
	//
	// protected static long randomLong() {
	// return NumberHelper.randomLong();
	// }
	//
	// protected static long randomLong(long maxValue) {
	// return NumberHelper.randomLong(maxValue);
	// }
	//
	// protected static long randomLong(long minValue, long maxValue) {
	// return NumberHelper.randomLong(minValue, maxValue);
	// }
	//
	// protected static float randomFloat() {
	// return NumberHelper.randomFloat();
	// }
	//
	// protected static float randomFloat(float maxValue) {
	// return NumberHelper.randomFloat(maxValue);
	// }
	//
	// protected static float randomFloat(float minValue, float maxValue) {
	// return NumberHelper.randomFloat(minValue, maxValue);
	// }
	//
	// protected static double randomDouble() {
	// return NumberHelper.randomDouble();
	// }
	//
	// protected static double randomDouble(double maxValue) {
	// return NumberHelper.randomDouble(maxValue);
	// }
	//
	// protected static double randomDouble(double minValue, double maxValue) {
	// return NumberHelper.randomDouble(minValue, maxValue);
	// }
	//
	// protected static Date randomDate() {
	// return DateHelper.randomDate();
	// }
	//
	// protected static long randomDateLong() {
	// return DateHelper.randomDateLong();
	// }
	//
	// protected static Calendar randomCalendar() {
	// return CalendarHelper.randomCalendar();
	// }
	//
	// protected static long randomCalendarLong() {
	// return CalendarHelper.randomCalendarLong();
	// }
	//
	// protected static <T extends Enum<T>> T randomType(Class<T> enumType) {
	// return EnumHelper.randomType(enumType);
	// }

	/**
	 * Assert collection.
	 *
	 * @param <E>
	 *            the element type
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 */
//	protected static <E> void assertCollection(Collection<E> expected,
//			Collection<E> actual) {
//		if (expected == null) {
//			assertNull(actual);
//		} else {
//			assertNotNull(actual);
//			assertEquals(expected.size(), actual.size());
//			//
//			Object[] expecteds = expected.toArray();
//			Object[] actuals = actual.toArray();
//			for (int i = 0; i < expecteds.length; i++) {
//				assertEquals(expecteds[i], actuals[i]);
//			}
//		}
//	}
//
//	/**
//	 * Assert map.
//	 *
//	 * @param <K>
//	 *            the key type
//	 * @param <V>
//	 *            the value type
//	 * @param expected
//	 *            the expected
//	 * @param actual
//	 *            the actual
//	 */
//	protected static <K, V> void assertMap(Map<K, V> expected, Map<K, V> actual) {
//		if (expected == null) {
//			assertNull(actual);
//		} else {
//			assertNotNull(actual);
//			assertEquals(expected.size(), actual.size());
//			//
//			Object[] expectedKeys = expected.keySet().toArray();
//			Object[] actualKeys = actual.keySet().toArray();
//			for (int i = 0; i < expectedKeys.length; i++) {
//				// key
//				@SuppressWarnings("unchecked")
//				K expectedKey = (K) expectedKeys[i];
//				@SuppressWarnings("unchecked")
//				K actualKey = (K) actualKeys[i];
//				assertEquals(expectedKey, actualKey);
//
//				// value
//				V expectedValue = expected.get(expectedKey);
//				V actualValue = actual.get(actualKey);
//				assertEquals(expectedValue, actualValue);
//			}
//		}
//	}
//
//	// /**
//	// * 檢核date
//	// *
//	// * @param expected
//	// * @param actual
//	// */
//	// protected static void assertDate(Date expected, Date actual) {
//	// if (expected == null) {
//	// assertNull(actual);
//	// } else {
//	// assertNotNull(actual);
//	// assertEquals(DateHelper.toString(expected),
//	// DateHelper.toString(actual));
//	// }
//	// }
//
//	/**
//	 * Prints the insert.
//	 *
//	 * @param idx
//	 *            the idx
//	 * @param value
//	 *            the value
//	 */
//	protected static void printInsert(int idx, int value) {
//		System.out.println("[" + idx + "] insert: "
//				+ (value > 0 ? "ok" : "fail"));
//	}
//
//	/**
//	 * Prints the find.
//	 *
//	 * @param idx
//	 *            the idx
//	 * @param value
//	 *            the value
//	 */
//	protected static void printFind(int idx, Object value) {
//		System.out.println("[" + idx + "] find  : "
//				+ (value != null ? "ok" : "fail"));
//	}
//
//	/**
//	 * Prints the update.
//	 *
//	 * @param idx
//	 *            the idx
//	 * @param value
//	 *            the value
//	 */
//	protected static void printUpdate(int idx, int value) {
//		System.out.println("[" + idx + "] update: "
//				+ (value > 0 ? "ok" : "fail"));
//	}
//
//	/**
//	 * Prints the delete.
//	 *
//	 * @param idx
//	 *            the idx
//	 * @param value
//	 *            the value
//	 */
//	protected static void printDelete(int idx, int value) {
//		System.out.println("[" + idx + "] delete: "
//				+ (value > 0 ? "ok" : "fail"));
//	}
//
//	/**
//	 * Prints the delete.
//	 *
//	 * @param idx
//	 *            the idx
//	 * @param value
//	 *            the value
//	 */
//	protected static void printDelete(int idx, Object value) {
//		System.out.println("[" + idx + "] delete: "
//				+ (value != null ? "ok" : "fail"));
//	}
//
//	/**
//	 * Prints the beans.
//	 */
//	protected static void printBeans() {
//		if (applicationContext == null) {
//			return;
//		}
//		//
//		String[] beanNames = applicationContext.getBeanDefinitionNames();
//		Arrays.sort(beanNames, Collator.getInstance(java.util.Locale.ENGLISH));
//		String msgPattern = "[{0}]-[{1}]: {2}";
//		long start = System.currentTimeMillis();
//		System.out.println("=========================================");
//		for (int i = 0; i < beanNames.length; i++) {
//			Object bean = null;
//			boolean abstractBean = false;
//			try {
//				// when abstract bean will throw exception
//				// so set bean="abstract bean"
//				bean = applicationContext.getBean(beanNames[i]).getClass()
//						.getSimpleName();
//			} catch (Exception ex) {
//				abstractBean = true;
//				bean = "[abstract=\"true\"]";
//			}
//
//			StringBuffer msg = new StringBuffer(MessageFormat.format(
//					msgPattern, i, beanNames[i], bean));
//			// 不是抽象類別
//			if (!abstractBean) {
//				System.out.println(msg);// 顯示黑色
//			} else {
//				System.err.println(msg);// 顯示紅色
//			}
//
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("=========================================");
//		msgPattern = "[total]:{0} time:{1} mills.";
//		StringBuffer msg = new StringBuffer(MessageFormat.format(msgPattern,
//				beanNames.length, (end - start)));
//		System.out.println(msg);
//	}
//
//	/**
//	 * Gets the declared constructor.
//	 *
//	 * @param className
//	 *            the class name
//	 * @param parameterTypes
//	 *            the parameter types
//	 * @return the declared constructor
//	 */
//	protected static Constructor<?> getDeclaredConstructor(String className,
//			Class<?>... parameterTypes) {
//		Constructor<?> result = null;
//		try {
//			if (className != null) {
//				ClassLoader classLoader = Thread.currentThread()
//						.getContextClassLoader();
//				Class<?> clazz = Class.forName(className, true, classLoader);
//				result = getDeclaredConstructor(clazz, parameterTypes);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * Gets the declared constructor.
//	 *
//	 * @param clazz
//	 *            the clazz
//	 * @param parameterTypes
//	 *            the parameter types
//	 * @return the declared constructor
//	 */
//	protected static Constructor<?> getDeclaredConstructor(Class<?> clazz,
//			Class<?>... parameterTypes) {
//		Constructor<?> result = null;
//		try {
//			if (clazz != null) {
//				result = clazz.getDeclaredConstructor(parameterTypes);
//				result.setAccessible(true);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * Gets the declared method.
//	 *
//	 * @param clazz
//	 *            the clazz
//	 * @param methodName
//	 *            the method name
//	 * @param parameterTypes
//	 *            the parameter types
//	 * @return the declared method
//	 */
//	protected static Method getDeclaredMethod(Class<?> clazz,
//			String methodName, Class<?>... parameterTypes) {
//		Method result = null;
//		try {
//
//			if (clazz != null) {
//				result = clazz.getDeclaredMethod(methodName, parameterTypes);
//				result.setAccessible(true);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * Gets the declared field.
//	 *
//	 * @param clazz
//	 *            the clazz
//	 * @param fieldName
//	 *            the field name
//	 * @return the declared field
//	 */
//	protected static Field getDeclaredField(Class<?> clazz, String fieldName) {
//		Field result = null;
//		try {
//
//			if (clazz != null) {
//				result = clazz.getDeclaredField(fieldName);
//				result.setAccessible(true);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
}
