package org.openyu.commons.junit.supporter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

import org.springframework.context.ConfigurableApplicationContext;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

import org.apache.xbean.spring.context.SpringApplicationContext;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.openyu.commons.bean.SeqBean;
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.junit.BaseTest;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.RuntimeHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.spring.util.StopWatch;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.ByteUnit;
import org.openyu.commons.util.CalendarHelper;
import org.openyu.commons.util.DateHelper;
import org.openyu.commons.util.MemoryHelper;

/**
 * The Class BaseTestSupporter.
 *
 * benchmark jvm args
 *
 * -Djub.consumers=CONSOLE,H2 -Djub.db.file=.benchmarks
 *
 * @BeforeClass
 * @Before
 * @After
 * @Before
 * @After
 * @AfterClass
 */
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark/org.openyu.commons.junit.supporter.BaseTestSupporter")
@BenchmarkHistoryChart(filePrefix = "benchmark/org.openyu.commons.junit.supporter.BaseTestSupporterHistory")
public class BaseTestSupporter implements BaseTest, Supporter {

	// @Rule
	// public BenchmarkRule benchmarkRule = new BenchmarkRule();

	/** The application context. */
	protected static ConfigurableApplicationContext applicationContext;

	/**
	 * 起始時間
	 */
	protected static long begTime = System.nanoTime();

	/**
	 * 結束時間
	 */
	protected static long endTime = System.nanoTime();

	/**
	 * 使用的記憶體
	 */
	protected static double usedMemory = 0d;// bytes = 0;

	/**
	 * 計數器
	 */
	protected AtomicLong counter = new AtomicLong(0);

	/**
	 * Instantiates a new base test supporter.
	 */
	public BaseTestSupporter() {
	}

	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// }
	//
	// @AfterClass
	// public static void tearDownAfterClass() throws Exception {
	// }
	//
	// @Before
	// public void setUp() throws Exception {
	// }
	//
	// @After
	// public void tearDown() throws Exception {
	// }

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------

	protected static boolean randomBoolean() {
		return BooleanHelper.randomBoolean();
	}

	protected static String randomUnique() {
		return StringHelper.randomUnique();
	}

	protected static String randomAlphabet() {
		return StringHelper.randomAlphabet();
	}

	protected static String randomAlphabet(int length) {
		return StringHelper.randomString(length);
	}

	protected static String randomString() {
		return StringHelper.randomString();
	}

	protected static String randomString(int length) {
		return StringHelper.randomString(length);
	}

	protected static String randomIp(String prefixIp) {
		return StringHelper.randomIp(prefixIp);
	}

	protected static char randomChar() {
		return CharHelper.randomChar();
	}

	protected static byte randomByte() {
		return NumberHelper.randomByte();
	}

	protected static short randomShort() {
		return NumberHelper.randomShort();
	}

	protected static int randomInt() {
		return NumberHelper.randomInt();
	}

	protected static int randomInt(int maxValue) {
		return NumberHelper.randomInt(maxValue);
	}

	protected static int randomInt(int minValue, int maxValue) {
		return NumberHelper.randomInt(minValue, maxValue);
	}

	protected static long randomLong() {
		return NumberHelper.randomLong();
	}

	protected static long randomLong(long maxValue) {
		return NumberHelper.randomLong(maxValue);
	}

	protected static long randomLong(long minValue, long maxValue) {
		return NumberHelper.randomLong(minValue, maxValue);
	}

	protected static float randomFloat() {
		return NumberHelper.randomFloat();
	}

	protected static float randomFloat(float maxValue) {
		return NumberHelper.randomFloat(maxValue);
	}

	protected static float randomFloat(float minValue, float maxValue) {
		return NumberHelper.randomFloat(minValue, maxValue);
	}

	protected static double randomDouble() {
		return NumberHelper.randomDouble();
	}

	protected static double randomDouble(double maxValue) {
		return NumberHelper.randomDouble(maxValue);
	}

	protected static double randomDouble(double minValue, double maxValue) {
		return NumberHelper.randomDouble(minValue, maxValue);
	}

	protected static Date randomDate() {
		return DateHelper.randomDate();
	}

	protected static long randomDateLong() {
		return DateHelper.randomDateLong();
	}

	protected static Calendar randomCalendar() {
		return CalendarHelper.randomCalendar();
	}

	protected static long randomCalendarLong() {
		return CalendarHelper.randomCalendarLong();
	}

	protected static <T extends Enum<T>> T randomType(Class<T> enumType) {
		return EnumHelper.randomType(enumType);
	}

	/**
	 * Assert collection.
	 *
	 * @param <E>
	 *            the element type
	 * @param expected
	 *            the expecteds
	 * @param actual
	 *            the actuals
	 */
	protected static <E> void assertCollectionEquals(Collection<E> expecteds, Collection<E> actuals) {
		if (expecteds == null) {
			Assert.fail("expected collection was null");
		} else {
			if (actuals == null) {
				Assert.fail("actual collection was null");
			}
			//
			int actualsLength = actuals.size();
			int expectedsLength = expecteds.size();
			if (actualsLength != expectedsLength) {
				Assert.fail("collection lengths differed, expected.length=" + expectedsLength + " actual.length="
						+ actualsLength);
			}
			//
			Object[] expectedsArray = expecteds.toArray();
			Object[] actualsArray = actuals.toArray();
			for (int i = 0; i < expectedsArray.length; i++) {
				Object expected = expectedsArray[i];
				Object actual = actualsArray[i];
				// 陣列
				boolean isArray = ArrayHelper.isArray(expected);
				if (isArray) {
					assertArrayEquals(i, expected, actual);
				} else {
					Assert.assertEquals("differed at entry [" + i + "]", expected, actual);
				}
			}
		}
	}

	protected static void assertArrayEquals(int index, Object expected, Object actual) {
		if (expected instanceof boolean[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((boolean[]) expected),
					((boolean[]) actual));
		} else if (expected instanceof char[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((char[]) expected),
					((char[]) actual));
		} else if (expected instanceof byte[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((byte[]) expected),
					((byte[]) actual));
		} else if (expected instanceof short[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((short[]) expected),
					((short[]) actual));
		} else if (expected instanceof int[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((int[]) expected),
					((int[]) actual));
		} else if (expected instanceof long[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((long[]) expected),
					((long[]) actual));
		} else if (expected instanceof float[]) {
			// While delta is the maximum difference (delta) between expected and actual for
			// which both numbers are still considered equal.
			// https://stackoverflow.com/questions/7554281/junit-assertions-make-the-assertion-between-floats

			float delta = 0.0f;
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((float[]) expected),
					((float[]) actual), delta);

		} else if (expected instanceof double[]) {
			double delta = 0.0d;
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((double[]) expected),
					((double[]) actual), delta);

		} else if (expected instanceof Object[]) {
			Assert.assertArrayEquals("differed at entry [" + index + "]", ((Object[]) expected),
					((Object[]) actual));
		}
	}

	/**
	 * Assert map.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param expecteds
	 *            the expecteds
	 * @param actuals
	 *            the actuals
	 */
	protected static <K, V> void assertMapEquals(Map<K, V> expecteds, Map<K, V> actuals) {
		if (expecteds == null) {
			Assert.fail("expected map was null");
		} else {
			if (actuals == null) {
				Assert.fail("actual map was null");
			}
			//
			int actualsLength = actuals.size();
			int expectedsLength = expecteds.size();
			if (actualsLength != expectedsLength) {
				Assert.fail(
						"map lengths differed, expected.length=" + expectedsLength + " actual.length=" + actualsLength);
			}
			//
			Object[] expectedsKeyArray = expecteds.keySet().toArray();
			Object[] actualsKeyArray = actuals.keySet().toArray();
			for (int i = 0; i < expectedsKeyArray.length; i++) {
				// key
				@SuppressWarnings("unchecked")
				K expectedKey = (K) expectedsKeyArray[i];
				@SuppressWarnings("unchecked")
				K actualKey = (K) actualsKeyArray[i];
				// 陣列
				boolean isArrayExpectedKey = ArrayHelper.isArray(expectedKey);
				if (isArrayExpectedKey) {
					assertArrayEquals(i, expectedKey, actualKey);
				} else {
					Assert.assertEquals("differed at key", expectedKey, actualKey);
				}

				// value
				V expectedValue = expecteds.get(expectedKey);
				V actualValue = actuals.get(actualKey);
				// 陣列
				boolean isArrayExpectedValue = ArrayHelper.isArray(expectedValue);
				if (isArrayExpectedValue) {
					assertArrayEquals(i, expectedValue, actualValue);
				} else {
					Assert.assertEquals("differed at value [" + expectedKey + "]", expectedValue, actualValue);
				}
			}
		}
	}

	// /**
	// * 檢核date
	// *
	// * @param expected
	// * @param actual
	// */
	// protected static void assertDate(Date expected, Date actual) {
	// if (expected == null) {
	// assertNull(actual);
	// } else {
	// assertNotNull(actual);
	// assertEquals(DateHelper.toString(expected),
	// DateHelper.toString(actual));
	// }
	// }

	protected static void printInsert(Serializable value) {
		printInsert(-1, value);
	}

	/**
	 * Prints the insert.
	 *
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	protected static void printInsert(int idx, Serializable value) {
		StringBuffer buff = new StringBuffer();
		if (idx > -1) {
			buff.append("#." + idx + " ");
		}
		buff.append("Insert PK[" + value + "]");
		System.out.println(buff);
	}

	protected static void printFind(Object value) {
		printFind(-1, value);
	}

	/**
	 * Prints the find.
	 *
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	protected static void printFind(int idx, Object value) {
		long seq = 0;
		//
		if (value instanceof SeqEntity) {
			SeqEntity entity = (SeqEntity) value;
			seq = entity.getSeq();
		} else if (value instanceof SeqBean) {
			SeqBean entity = (SeqBean) value;
			seq = entity.getSeq();
		}
		//
		StringBuffer buff = new StringBuffer();
		if (idx > -1) {
			buff.append("#." + idx + " ");
		}
		//
		buff.append("Find ");
		if (seq > 0) {
			buff.append("PK[" + seq + "]");
		} else {
			buff.append(value);
		}
		System.out.println(buff);
	}

	protected static void printUpdate(int value) {
		printUpdate(-1, value);
	}

	/**
	 * Prints the update.
	 *
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	protected static void printUpdate(int idx, int value) {
		StringBuffer buff = new StringBuffer();
		if (idx > -1) {
			buff.append("#." + idx + " ");
		}
		buff.append("Update ");
		if (value > 0) {
			buff.append("success");
		} else {
			buff.append("failed");
		}
		System.out.println(buff);
	}

	protected static void printUpdate(int idx, boolean value) {
		printUpdate(idx, (value ? 1 : 0));
	}

	protected static void printDelete(int value) {
		printDelete(-1, value);
	}

	/**
	 * Prints the delete.
	 *
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	protected static void printDelete(int idx, int value) {
		StringBuffer buff = new StringBuffer();
		if (idx > -1) {
			buff.append("#." + idx + " ");
		}
		buff.append("Delete ");
		if (value > 0) {
			buff.append("success");
		} else {
			buff.append("failed");
		}
		System.out.println(buff);
	}

	protected static void printDelete(Object value) {
		printDelete(-1, value);
	}

	/**
	 * Prints the delete.
	 *
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	protected static void printDelete(int idx, Object value) {
		long seq = 0;
		//
		if (value instanceof SeqEntity) {
			SeqEntity entity = (SeqEntity) value;
			seq = entity.getSeq();
		} else if (value instanceof SeqBean) {
			SeqBean entity = (SeqBean) value;
			seq = entity.getSeq();
		}
		//
		StringBuffer buff = new StringBuffer();
		if (idx > -1) {
			buff.append("#." + idx + " ");
		}
		//
		buff.append("Delete ");
		if (seq > 0) {
			buff.append("PK[" + seq + "]");
		} else {
			buff.append(value);
		}
		System.out.println(buff);
	}

	/**
	 * Prints the beans.
	 */
	protected static void printBean() {
		AssertHelper.notNull(applicationContext, "The ApplicationContext must not be null");
		//
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames, Collator.getInstance(java.util.Locale.ENGLISH));
		final String MSG_PATTERN = "#.{0} {1} \"{2}\"";
		// 計算所耗費的記憶體(bytes)
		RuntimeHelper.gc();
		// 原本的記憶體
		long memory = RuntimeHelper.usedMemory();
		//
		StopWatch sw = new StopWatch("Spring beans");
		// System.out.println("=========================================");
		// System.out.println("Spring beans");
		// System.out.println("=========================================");
		for (int i = 0; i < beanNames.length; i++) {
			String className = null;
			String name = beanNames[i];
			sw.start("#." + (i + 1) + " " + name);
			Object bean = null;
			try {
				// when abstract bean will throw exception
				// so set bean="abstract bean"
				bean = applicationContext.getBean(beanNames[i]);
				if (bean == null) {
					className = "NULL CLASS";
				} else {
					className = bean.getClass().getSimpleName();
				}
			} catch (Exception ex) {
				className = "ABSTRACT CLASS";
			}
			sw.stop();
			sw.printResult();

			// StringBuilder msg = new
			// StringBuilder(MessageFormat.format(MSG_PATTERN, (i + 1),
			// className, beanNames[i]));

			// 不是抽象類別
			// if (!abstractBean) {
			// System.out.println(msg);// 顯示黑色
			// } else {
			// System.err.println(msg);// 顯示紅色
			// }

			// System.out.println(msg);
			//
			usedMemory = Math.max(usedMemory, (RuntimeHelper.usedMemory() - memory));
		}
		long durTime = endTime - begTime;
		//
		RuntimeHelper.gc();
		usedMemory = Math.max(usedMemory, (RuntimeHelper.usedMemory() - memory));
		double kb = NumberHelper.round(ByteUnit.BYTE.toKiB(usedMemory), 1);
		double mb = NumberHelper.round(ByteUnit.BYTE.toMiB(usedMemory), 1);
		double sizeOf = NumberHelper.round(ByteUnit.BYTE.toMiB(MemoryHelper.sizeOf(applicationContext)), 1);
		//
		System.out.println("=========================================");
		final String MEMORY_PATTERN = "count: {0}, time: {1} mills., {2} bytes, {3} KB, {4} MB, sizeOf: {5} MB memory used";
		StringBuilder msg = new StringBuilder(
				MessageFormat.format(MEMORY_PATTERN, beanNames.length, durTime, usedMemory, kb, mb, sizeOf));
		System.out.println(msg);
	}

	/**
	 * Gets the declared constructor.
	 *
	 * @param className
	 *            the class name
	 * @param parameterTypes
	 *            the parameter types
	 * @return the declared constructor
	 */
	protected static Constructor<?> getDeclaredConstructor(String className, Class<?>... parameterTypes) {
		Constructor<?> result = null;
		try {
			if (className != null) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				Class<?> clazz = Class.forName(className, true, classLoader);
				result = getDeclaredConstructor(clazz, parameterTypes);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets the declared constructor.
	 *
	 * @param clazz
	 *            the clazz
	 * @param parameterTypes
	 *            the parameter types
	 * @return the declared constructor
	 */
	protected static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		return ClassHelper.getDeclaredConstructor(clazz, parameterTypes);
	}

	/**
	 * Gets the declared method.
	 *
	 * @param clazz
	 *            the clazz
	 * @param methodName
	 *            the method name
	 * @param parameterTypes
	 *            the parameter types
	 * @return the declared method
	 */
	protected static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		return ClassHelper.getDeclaredMethod(clazz, methodName, parameterTypes);
	}

	/**
	 * Gets the declared field.
	 *
	 * @param clazz
	 *            the clazz
	 * @param fieldName
	 *            the field name
	 * @return the declared field
	 */
	protected static Field getDeclaredField(Class<?> clazz, String fieldName) {
		return ClassHelper.getDeclaredField(clazz, fieldName);
	}

	/**
	 * 模擬oom
	 * 
	 * 會丟出 OutOfMemoryError
	 */
	protected static void mockOutOfMemory() {
		Map<Integer, String> value = new HashMap<Integer, String>();
		for (int i = 0; i < 10000000; i++) {
			value.put(new Integer(i), new String("Object_" + i));
		}
	}

	/**
	 * 印出結果
	 * 
	 * @param beg
	 * @param byteCounter
	 * @param timesCounter
	 */
	protected static void printResult(long beg, AtomicLong byteCounter, AtomicLong timesCounter) {
		long end = System.currentTimeMillis();
		long dur = (end - beg);
		double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
		double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
		double mbresult = NumberHelper
				.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
		//
		System.out.println(timesCounter.get() + " rows, " + byteCounter.get() + " bytes / " + dur + " ms. = " + result
				+ " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
	}

}
