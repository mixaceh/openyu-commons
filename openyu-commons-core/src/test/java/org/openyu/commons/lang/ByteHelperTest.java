package org.openyu.commons.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ByteHelperTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithBoolean() {
		boolean value = true;
		byte[] result = null;
		//
		int count = 1000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(1, result.length);
		//
		boolean newValue = false;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toBoolean(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithChar() {
		char value = 'A';
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(2, result.length);
		//
		char newValue = ' ';
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toChar(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// #issue 較慢
	// 1000000 times: 361 mills.
	// 1000000 times: 371 mills.
	// 1000000 times: 350 mills.
	//
	// #fix
	// 1000000 times: 286 mills.
	// 1000000 times: 322 mills.
	// 1000000 times: 287 mills.
	// verified
	public void toByteArrayWithString() {
		String value = "abc";
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(3, result.length);
		//
		String newValue = null;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toString(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithByte() {
		byte value = (byte) 127;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(1, result.length);
		//
		byte newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toByte(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithShort() {
		short value = (short) 500;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(2, result.length);
		//
		short newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toShort(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void toShortByteArray() {
		int value = Short.MAX_VALUE;
		byte[] result = null;
		//
		result = ByteHelper.toShortByteArray(value);

		ByteHelper.println(result);
		assertEquals(2, result.length);
		//
		int newValue = 0;
		newValue = ByteHelper.fromShortInt(result);

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void toByteByteArray() {
		int value = Byte.MAX_VALUE;
		byte[] result = null;
		//
		result = ByteHelper.toByteByteArray(value);

		ByteHelper.println(result);
		assertEquals(1, result.length);
		//
		int newValue = 0;
		newValue = ByteHelper.fromByteInt(result);

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	public void toIntByteArray() {
		long value = Integer.MAX_VALUE;
		// 不要用Integer.MAX_VALUE+1,會變成 -2147483648
		// long value = Long.MAX_VALUE;
		byte[] result = null;
		//
		result = ByteHelper.toIntByteArray(value);

		ByteHelper.println(result);
		assertEquals(4, result.length);
		//
		long newValue = 0;
		newValue = ByteHelper.fromIntLong(result);

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithInt() {
		int value = 100;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(4, result.length);
		//
		int newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toInt(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithLong() {
		long value = 500L;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(8, result.length);
		//
		long newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toLong(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithFloat() {
		float value = 500f;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(4, result.length);
		//
		float newValue = 0f;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toFloat(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue, Float.MAX_VALUE);
	}

	@Test
	// 1000000 times: 81 mills.
	// 1000000 times: 98 mills.
	// 1000000 times: 87 mills.
	// verified
	public void toByteArrayWithDouble() {
		double value = 500d;
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.toByteArray(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteHelper.println(result);
		assertEquals(8, result.length);
		//
		double newValue = 0d;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteHelper.toDouble(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue, Double.MAX_VALUE);
	}

	@Test
	// 1000000 times: 1172 mills.
	// 1000000 times: 1171 mills.
	// 1000000 times: 1187 mills.
	// verified
	public void println() {
		String value = "abcdef1234567890";
		//
		int count = 1;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ByteHelper.println(ByteHelper.toByteArray(value));
			// 97 98 99 100 101 102 49 50 51 52 53 54 55 56 57 48
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 87 mills.
	// round: 0.09, GC: 13
	public void getByteArray() {
		String value = "abcdef1234567890";// length=16
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteHelper.getByteArray(value.getBytes(), 2, 4);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(ByteHelper.toString(result));

		assertEquals("cdef", new String(result));
		assertEquals(4, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.01, GC: 0
	public void byteArraycopy() throws Exception {
		String value = "abcdef1234567890";
		byte[] buff = value.getBytes();
		byte[] result = new byte[buff.length];
		//
		int count = 1000000;
		for (int i = 0; i < count; i++) {
			ByteHelper.byteArraycopy(buff, 0, result, 0, buff.length);
		}

		System.out.println(ByteHelper.toString(result));
		assertArrayEquals(buff, result);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.36, GC: 6
	public void randomByteArray() {
		byte[] result = null;
		int count = 1000000;

		for (int i = 0; i < count; i++) {
			result = ByteHelper.randomByteArray(32);
		}

		SystemHelper.println(result);
		assertEquals(32, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void toByteArrayWithObject() {
		Date value = new Date();
		byte[] result = null;
		//
		int count = 1;

		for (int i = 0; i < count; i++) {
			// 序列化
			result = ByteHelper.toByteArray(value);
		}

		ByteHelper.println(result);
		assertEquals(46, result.length);
		//
		Date date = null;
		for (int i = 0; i < count; i++) {
			// 反序列化
			date = ByteHelper.toObject(result);
		}

		System.out.println(date);
		assertEquals(value, date);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void modifyByteArray() {
		byte[] value = new byte[] { 0, 1, 2 };
		SystemHelper.println(value);// 0, 1, 2
		//
		modifyByteArray(value);
		assertEquals(value[0], 10);
		assertEquals(value[1], 20);
		SystemHelper.println(value);// 10, 20, 2
		//
		newByteArray(value);
		SystemHelper.println(value);// 10, 20, 2
		assertEquals(value[0], 10);
		assertEquals(value[1], 20);
	}

	public void modifyByteArray(byte[] value) {
		value[0] = (byte) 10;
		value[1] = (byte) 20;
	}

	public void newByteArray(byte[] value) {
		value = new byte[] { 99 };
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void toObject() {
		Object result = null;
		// 非jdk序列化,所以用反序列化會失敗, 丟出EOFException
		// byte[] value = ByteUtil.toBytes(new Boolean(false));

		// 使用jdk序列化, 可以正常反序列化
		byte[] value = ByteHelper.toByteArray((Serializable) new Boolean(false));

		// java.lang.Date也可以測
		// byte[] value = ByteUtil.toBytes(new Date());
		result = ByteHelper.toObject(value);
		System.out.println(result);
		assertNotNull(result);
		assertFalse(((Boolean) result).booleanValue());
	}
}
