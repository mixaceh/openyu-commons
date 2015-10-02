package org.openyu.commons.nio;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.SystemHelper;

public class ByteBufferHelperTest extends BaseTestSupporter {

	@Test
	public void toByteBuffer() {
		String value = "abcde";
		ByteBuffer byteBuffer = ByteBufferHelper.toByteBuffer(value);
		System.out.println("string -> byteBuffer: " + byteBuffer);
		//
		value = ByteBufferHelper.toString(byteBuffer);
		System.out.println("byteBuffer -> string: " + value);
		//
		String value2 = "abcdeeeee";
		ByteBuffer byteBuffer2 = ByteBufferHelper.toByteBuffer(value2);
		System.out.println("string -> byteBuffer: " + byteBuffer);
		value2 = ByteBufferHelper.toString(byteBuffer2);
		System.out.println("byteBuffer -> string: " + value2);
		//
		System.out.println(byteBuffer == byteBuffer2);
		//
		byte byteValue = 123;
		byteBuffer = ByteBufferHelper.toByteBuffer(byteValue);
		System.out.println("byte -> byteBuffer: " + byteBuffer);
		byteValue = ByteBufferHelper.toByte(byteBuffer);
		System.out.println("byteBuffer -> byte: " + byteValue);
		//
		short shortValue = 1234;
		byteBuffer = ByteBufferHelper.toByteBuffer(shortValue);
		System.out.println("short -> byteBuffer: " + byteBuffer);
		shortValue = ByteBufferHelper.toShort(byteBuffer);
		System.out.println("byteBuffer -> short: " + shortValue);
		//
		int intValue = 123;
		byteBuffer = ByteBufferHelper.toByteBuffer(intValue);
		System.out.println("int -> byteBuffer: " + byteBuffer);
		intValue = ByteBufferHelper.toInt(byteBuffer);
		System.out.println("byteBuffer -> int : " + intValue);

		// 會有BufferUnderflowException
		// 因之前有parseInt未歸位(position未回到原本的position)
		System.out.println(byteBuffer.get());// 取1個,此時position會變為1
		System.out.println(byteBuffer);
		//
		long longValue = 123;
		byteBuffer = ByteBufferHelper.toByteBuffer(longValue);
		System.out.println("long -> byteBuffer: " + byteBuffer);
		//
		float floatValue = 123.36f;
		byteBuffer = ByteBufferHelper.toByteBuffer(floatValue);
		System.out.println("float -> byteBuffer: " + byteBuffer);
		//
		double doubleValue = 123.36d;
		byteBuffer = ByteBufferHelper.toByteBuffer(doubleValue);
		System.out.println("double -> byteBuffer: " + byteBuffer);
		//
		boolean booleanValue = false;
		byteBuffer = ByteBufferHelper.toByteBuffer(booleanValue);
		System.out.println("boolean -> byteBuffer: " + byteBuffer);
		booleanValue = ByteBufferHelper.toBoolean(byteBuffer);
		System.out.println("byteBuffer -> boolean : " + booleanValue);
		//
		char charValue = 'A';
		byteBuffer = ByteBufferHelper.toByteBuffer(charValue);
		System.out.println("char -> byteBuffer: " + byteBuffer);
	}

	@Test
	// 1000000 times: 100 mills.
	// 1000000 times: 109 mills.
	// 1000000 times: 94 mills.
	// verified
	public void toByteBufferByBoolean() {
		boolean value = true;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(1, result.array().length);
		//
		boolean newValue = false;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toBoolean(result);
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
	public void toByteBufferByChar() {
		char value = 'A';
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(2, result.array().length);
		//
		char newValue = ' ';
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toChar(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue);
	}

	@Test
	// 1000000 times: 218 mills.
	// 1000000 times: 371 mills.
	// 1000000 times: 350 mills.
	// verified
	public void toByteBufferByString() {
		String value = "abc";
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(3, result.array().length);
		//
		String newValue = null;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toString(result);
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
	public void toByteBufferByByte() {
		byte value = (byte) 127;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(1, result.array().length);
		//
		byte newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toByte(result);
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
	public void toByteBufferByShort() {
		short value = (short) 500;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(2, result.array().length);
		//
		short newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toShort(result);
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
	public void toByteBufferByInt() {
		int value = 500;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(4, result.array().length);
		//
		int newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toInt(result);
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
	public void toByteBufferByLong() {
		long value = 500L;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(8, result.array().length);
		//
		long newValue = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toLong(result);
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
	public void toByteBufferByFloat() {
		float value = 500f;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(4, result.array().length);
		//
		float newValue = 0f;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toFloat(result);
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
	public void toByteBufferByDouble() {
		double value = 500d;
		ByteBuffer result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ByteBufferHelper.toByteBuffer(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		ByteBufferHelper.debug(result);
		assertEquals(8, result.array().length);
		//
		double newValue = 0d;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			newValue = ByteBufferHelper.toDouble(result);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(newValue);
		assertEquals(value, newValue, Double.MAX_VALUE);
	}

	@Test(expected = BufferOverflowException.class)
	public void byteBufferBufferOverflowException() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(1);
		//
		buffer.putLong(2L);// BufferOverflowException
	}

	@Test
	public void byteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(1);
		buffer.putLong(2L);
		buffer.flip();
		//
		int originPos = buffer.position();
		System.out.println("position: " + originPos);// 0
		System.out.println("remaining: " + buffer.remaining());// 12

		byte[] result = new byte[buffer.remaining()];
		buffer.get(result, 0, result.length);
		System.out.println("position: " + buffer.position());// 12
		System.out.println("remaining: " + buffer.remaining());// 0
		SystemHelper.println(result);
		//
		buffer.position(originPos);
		originPos = buffer.position();
		System.out.println("position: " + originPos);// 0
		System.out.println("remaining: " + buffer.remaining());// 12
		buffer.get(result, 0, result.length);
		SystemHelper.println(result);
		//
		buffer.clear();
		originPos = buffer.position();
		System.out.println("position: " + originPos);// 0
		System.out.println("remaining: " + buffer.remaining());// 1024
		buffer.get(result, 0, result.length);
		SystemHelper.println(result);
	}

	@Test
	// 1000000 times: 321 mills.
	public void toBytes() {
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ByteBuffer value = ByteBuffer.allocate(1024);
			value.putInt(1);
			value.putLong(2L);
			value.flip();
			// SystemHelper.println(value.array());// length=1024
			result = ByteBufferHelper.toBytes(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2
		SystemHelper.println(result);
		assertEquals(12, result.length);
	}

	@Test
	// 1000000 times: 320 mills.
	public void toBytesByteArrayOutputStream() throws Exception {
		byte[] result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(1);
			out.write(ByteHelper.toByteArray(2L));
			result = out.toByteArray();
			out.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(12, result.length);
	}

}
