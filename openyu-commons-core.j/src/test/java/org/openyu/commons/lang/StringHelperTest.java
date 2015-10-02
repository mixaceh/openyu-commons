package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class StringHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	// 利用 string.getBytes("UTF8"); 轉成 UTF8,在new String("","UTF8")
	public void encode() throws Exception {
		String value = "中文編碼測試";
		System.out.println("value: " + value);
		//
		byte[] bytes = value.getBytes("UTF8");
		String utf8String = new String(bytes, "UTF8");
		// utf8String = utf8String.concat(utf8String);
		System.out.println("UTF8:" + utf8String);
		//
		byte[] bytes2 = value.getBytes("BIG5");
		String utf8String2 = new String(bytes2, "BIG5");
		System.out.println("BIG5: " + utf8String2);
	}

	@Test
	// 1000000 times: 249 mills.
	// 1000000 times: 250 mills.
	// 1000000 times: 249 mills.
	// verified
	public void excludeFirst() {
		String value = "org.openyu.cir.department.model";
		String result = null;

		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringHelper.excludeFirst(value, ".", 2);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		//

		result = StringHelper.excludeFirst(value, ".", 2);
		System.out.println(result);
		//
		result = StringHelper.excludeLast(result, ".");
		System.out.println(result);
		result = StringHelper.replace(result, ".", "/");
		System.out.println(result);
		result = "/pub/" + result;
		// /pub/cir/department
		System.out.println(result);
	}

	@Test
	// 1000000 times: 537 mills.
	// 1000000 times: 518 mills.
	// 1000000 times: 532 mills.
	// verified
	public void randomUniquez() {
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringHelper.randomUnique();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// 1361579JmbDESVea
		// 1385516QYYcz8oTR
		// 1403474eUFu2c065

		// 0123456789012345
		System.out.println(result.length() + ", " + result);
	}

	@Test
	// 1000000 times: 117 mills.
	// 1000000 times: 117 mills.
	// 1000000 times: 118 mills.
	// verified
	public void randomAlphabetz() {
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringHelper.randomAlphabet();
			// System.out.println(result);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(1, result.length());
	}

	@Test
	// 1000000 times: 117 mills.
	// 1000000 times: 117 mills.
	// 1000000 times: 118 mills.
	// verified
	public void randomStringz() {
		String result = null;
		//
		int count = 100;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringHelper.randomString();
			System.out
					.println("[" + i + "] " + result + ", " + result.length());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		//
		result = StringHelper.randomString(20);
		System.out.println(result);
		assertEquals(10, result.length());
	}

	@Test
	public void charCode() {
		System.out.println(StringHelper.SPADE_CHAR);
		System.out.println(StringHelper.HEART_CHAR);
		System.out.println(StringHelper.DIAMOND_CHAR);
	}

	// @Test
	// 1000000 times: 342 mills.
	// 1000000 times: 347 mills.
	// 1000000 times: 347 mills.
	//
	// 1000000 times: 435 mills.
	// 1000000 times: 431 mills.
	// 1000000 times: 435 mills.
	// public void split()
	// {
	// String value = "a;b;c;;;;";//長度7
	// String[] result = null;
	// //
	// int count = 1000000;
	//
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++)
	// {
	// result = StringHelper.split(value, ";");
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	//
	// System.out.println(result.length);//長度3
	// SystemHelper.println(result);
	// }

	@Test
	// 1000000 times: 170 mills.
	// 1000000 times: 168 mills.
	// 1000000 times: 168 mills.
	public void StringUtils_splitPreserveAllTokens() {
		String value = "a;b;c;;;;";// 長度7
		String[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringUtils.splitPreserveAllTokens(value, ";");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.length);// 長度7
		SystemHelper.println(result);
	}

	@Test
	public void exampleSplit() {
		String input = "one|two|three||five|||eight||";

		useStringTokenizer(input); // [one, two, three, , five, , , eight, ]
		useSplit(input);// [one, two, three, , five, , , eight, , ]
		//

	}

	private static final String DELIM = "|";

	private static void useStringTokenizer(String input) {
		StringTokenizer st = new StringTokenizer(input, DELIM, true);
		List fields = new ArrayList();
		while (st.hasMoreTokens())
			fields.add(extractField(st));
		System.out.println(fields);
	}

	private static String extractField(StringTokenizer st) {
		String value = st.nextToken();
		if (DELIM.equals(value))
			value = "";
		else if (st.hasMoreTokens())
			st.nextToken(); // skip next delimiter
		return value;
	}

	private static void useSplit(String input) {
		String[] fields = input.split("\\|", 10);
		System.out.println(Arrays.asList(fields));
	}

	@Test
	public void startsWith() {
		String roleCode = "TEST_ROLE_marry001";
		boolean flag = roleCode.startsWith("TEST_ROLE", 0);
		System.out.println(flag);
		//
		roleCode = "_marry001";
		flag = roleCode.startsWith("TEST_ROLE", 0);
		System.out.println(flag);
	}

	@Test
	// 1000000 times: 180 mills.
	// 1000000 times: 181 mills.
	// 1000000 times: 179 mills.
	public void randomIp() {
		String value = "192.168.1";
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = StringHelper.randomIp(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void substring() {
		StringBuilder buff = new StringBuilder();
		buff.append("01234567");
		int pos = 6;
		System.out.println(buff.substring(0, pos));// 012345
		System.out.println(buff.substring(pos));// 67
	}

	@Test
	public void relation() {
		String relation = "slave1";
		String[] buff = relation.split(":");
		System.out.println(buff.length + " ");
		SystemHelper.println(buff);
		//
		relation = "slave1:192.168.9.28:3110";
		buff = relation.split(":");
		System.out.println(buff.length + " ");
		SystemHelper.println(buff);
	}

	@Test
	public void isEmpty() {
		boolean result = StringHelper.isEmpty(null);
		System.out.println(result);
		assertTrue(result);
		//
		result = StringHelper.isEmpty("aaa");
		System.out.println(result);
		assertFalse(result);
		//
		System.out.println("\0"); // 看不到
		String aaa = "\0";
		System.out.println(aaa); // 看不到
		System.out.println("\0".getBytes()); // 看不到
		System.out.println("\0".length());// 1
		//
		byte FLASH_EOF = 0x00;
		byte[] FLASH_EOF_BYTES = new byte[] { FLASH_EOF };
		System.out.println("FLASH_EOF: " + FLASH_EOF); // 0
		System.out.println("FLASH_EOF_BYTES: " + FLASH_EOF_BYTES);// [B@22ec3e6
		System.out.println("FLASH_EOF_BYTES: " + FLASH_EOF_BYTES.length);// 1
	}

	@Test
	public void print() {
		System.out.println(Character.SPACE_SEPARATOR);// 12
		System.out.println(Character.LINE_SEPARATOR);// 13
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.08 [+- 0.01], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.10, time.warmup: 0.00,
	// time.bench: 0.10
	public void replace() {
		String value = "aaa中文測試 bbb中文測試 ccc中文測試";
		String result = StringHelper.replace(value, "中文測試", "****");
		System.out.println(result);
		assertEquals(23, result.length());
		//
		value = "StringHelperTest$InnerTest";
		result = StringHelper.replace(value, "$", ".");
		System.out.println(result);
		assertEquals("StringHelperTest.InnerTest", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.08 [+- 0.01], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.10, time.warmup: 0.00,
	// time.bench: 0.10
	public void replaceWithNotFound() {
		String value = "StringHelperTest";
		String result = StringHelper.replace(value, "$", ".");
		System.out.println(result);
		assertEquals(value, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void extractList() {
		String value = "style=\"line-height:18pt; margin:0pt 0pt 10pt; orphans:0; text-indent:85.05pt; widows:0\"><span style=\"color:#385068; font-family:Arial; font-size:14pt; font-style:italic; font-weight:bold\">";
		List<String> result = StringHelper
				.extractList(value, "style=\"", "\">");
		System.out.println(result);
		//
		assertTrue(result.size() > 0);
	}

}
