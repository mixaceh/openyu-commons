package org.openyu.commons.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.EncodingHelper;

/**
 * 1.也可利用ByteBuffer 轉byte[],如:byteBuffer.array() -> byte[]
 * 
 * 2.或利用ByteHelper 轉byte[]
 * 
 */
public class ByteBufferHelper extends BaseHelperSupporter {

	private static transient final Logger log = LogManager
			.getLogger(ByteBufferHelper.class);

	private static ByteBufferHelper instance;

	public ByteBufferHelper() {
	}

	public static synchronized ByteBufferHelper getInstance() {
		if (instance == null) {
			instance = new ByteBufferHelper();
		}
		return instance;
	}

	// --------------------------------------------------
	// toBytes:
	// --------------------------------------------------
	// boolean
	// char
	// String

	// byte
	// short
	// int
	// long
	// float
	// double

	public static ByteBuffer toByteBuffer(boolean value) {
		ByteBuffer result = ByteBuffer.allocate(1);
		byte byteValue = value ? (byte) 1 : (byte) 0;
		result.put(byteValue);
		result.flip();// 將pos歸位到0
		return result;
	}

	public static ByteBuffer toByteBuffer(char value) {
		ByteBuffer result = ByteBuffer.allocate(2);
		result.putChar(value);
		result.flip();
		return result;
	}

	public static ByteBuffer toByteBuffer(String value) {
		return toByteBuffer(value, "UTF-8");
	}

	public static ByteBuffer toByteBuffer(String value, String charsetName) {
		ByteBuffer result = null;
		try {
			Charset charset = Charset.forName(charsetName);
			CharsetEncoder encoder = charset.newEncoder();
			result = encoder.encode(CharBuffer.wrap(value));
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static ByteBuffer toByteBuffer(byte value) {
		ByteBuffer ret = ByteBuffer.allocate(1);
		ret.put(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(short value) {
		ByteBuffer ret = ByteBuffer.allocate(2);
		ret.putShort(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(int value) {
		ByteBuffer ret = ByteBuffer.allocate(4);
		ret.putInt(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(long value) {
		ByteBuffer ret = ByteBuffer.allocate(8);
		ret.putLong(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(float value) {
		ByteBuffer ret = ByteBuffer.allocate(4);
		ret.putFloat(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(double value) {
		ByteBuffer ret = ByteBuffer.allocate(8);
		ret.putDouble(value);
		ret.flip();
		return ret;
	}

	public static ByteBuffer toByteBuffer(byte[] values) {
		return ByteBuffer.wrap(values);
	}

	// --------------------------------------------------
	public static boolean toBoolean(ByteBuffer value) {
		boolean result = false;
		try {
			if (value != null) {
				int originPos = value.position();
				result = (value.get() == 1 ? true : false);
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static char toChar(ByteBuffer value) {
		char result = 0x00;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getChar();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toString(ByteBuffer value) {
		return toString(value, EncodingHelper.UTF_8);
	}

	public static String toString(ByteBuffer value, String charsetName) {
		String result = null;
		try {
			Charset charset = Charset.forName(charsetName);
			CharsetDecoder decoder = charset.newDecoder();
			//
			int originPos = value.position();
			result = decoder.decode(value).toString();
			// reset buffer's position to its original so it is not altered:
			value.position(originPos);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static byte toByte(ByteBuffer value) {
		byte result = 0;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.get();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static short toShort(ByteBuffer value) {
		short result = 0;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getShort();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static int toInt(ByteBuffer value) {
		int result = 0;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getInt();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static long toLong(ByteBuffer value) {
		long result = 0;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getLong();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static float toFloat(ByteBuffer value) {
		float result = 0f;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getFloat();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static double toDouble(ByteBuffer value) {
		double result = 0d;
		try {
			if (value != null) {
				int originPos = value.position();
				result = value.getDouble();
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;

	}

	public static byte[] toBytes(ByteBuffer value) {
		byte[] result = new byte[0];
		try {
			if (value != null) {
				int originPos = value.position();
				result = new byte[value.remaining()];
				value.get(result, 0, result.length);
				value.position(originPos);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static void debug(ByteBuffer value) {
		StringBuffer sb = new StringBuffer();
		if (value != null) {
			sb.append(value);
		}
		if (sb.length() > 0) {
			System.out.println(sb.toString());
		}
	}
}
