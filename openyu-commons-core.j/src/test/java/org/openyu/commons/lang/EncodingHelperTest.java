package org.openyu.commons.lang;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

/**
 * The Class EncodingHelperTest.
 */
public class EncodingHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void EncodingHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.EncodingHelper");
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
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.EncodingHelper$InstanceHolder");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * Gets the single instance of EncodingHelperTest.
	 *
	 * @return single instance of EncodingHelperTest
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		EncodingHelper result = null;
		//
		result = EncodingHelper.getInstance();
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	// round: 0.15
	public void encodeHexString() {
		String value = "中文測試abcdef";// 616263646566
		String result = null;
		//
		int count = 10000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = EncodingHelper.encodeHex(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.length() + ", " + result);// 36,
															// e4b8ade69687e6b8ace8a9a6616263646566
		assertEquals("e4b8ade69687e6b8ace8a9a6616263646566", result);
		//
		result = EncodingHelper.encodeHex("abc");
		System.out.println(result.length() + ", " + result);// 6, 616263
		//
		result = EncodingHelper.encodeHex("123");
		System.out.println(result.length() + ", " + result);// 6, 313233
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	public void decodeHexString() {
		String value = "e4b8ade69687e6b8ace8a9a6616263646566"; // 中文測試abcdef
		String result = null;
		//
		int count = 10000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = EncodingHelper.decodeHexString(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals("中文測試abcdef", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	public void encodeBase64String() {
		String value = "中文測試abcdef";
		String result = null;
		//
		int count = 10000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = EncodingHelper.encodeBase64String(value.getBytes());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.length() + ", " + result);// 24,
															// 5Lit5paH5ris6KmmYWJjZGVm
		assertEquals("5Lit5paH5ris6KmmYWJjZGVm", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 2, concurrency = 1)
	public void decodeBase64() {
		String value = "5Lit5paH5ris6KmmYWJjZGVm"; // 中文測試abcdef
		byte[] result = null;
		//
		int count = 10000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = EncodingHelper.decodeBase64(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);// -28, -72, -83, -26, -106, -121, -26,
										// -72, -84, -24, -87, -90, 97, 98, 99,
										// 100, 101, 102
		assertEquals(18, result.length);
		//
		String stringValue = new String(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals("中文測試abcdef", stringValue);
	}
}
