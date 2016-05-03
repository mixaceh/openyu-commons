package org.openyu.commons.lang;

import java.io.Serializable;

import org.openyu.commons.enumz.BooleanEnum;
import org.openyu.commons.enumz.ByteEnum;
import org.openyu.commons.enumz.CharEnum;
import org.openyu.commons.enumz.DoubleEnum;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.enumz.FloatEnum;
import org.openyu.commons.enumz.IntEnum;
import org.openyu.commons.enumz.LongEnum;
import org.openyu.commons.enumz.ShortEnum;
import org.openyu.commons.enumz.StringEnum;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.SerializeHelper;
import org.openyu.commons.util.SerializeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Byte輔助類
 */
public final class ByteHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ByteHelper.class);

	private ByteHelper() {
		throw new HelperException(
				new StringBuilder().append(ByteHelper.class.getName()).append(" can not construct").toString());
	}

	// --------------------------------------------------
	// toByteArray:
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

	public static byte[] toByteArray(BooleanEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Boolean value) {
		return toByteArray(BooleanHelper.safeGet(value));
	}

	/**
	 * boolean -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(boolean value) {
		byte[] result = new byte[] { (byte) (value ? 0x01 : 0x00) };
		return result; // bool -> byte=0/1
	}

	public static byte[] toByteArray(CharEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Character value) {
		return toByteArray(CharHelper.safeGet(value));
	}

	public static byte[] toByteArray(char value) {
		byte[] result = new byte[2];
		result[1] = (byte) (value & 0x000000ff);
		result[0] = (byte) ((value & 0x0000ff00) >>> 8);
		return result;
	}

	public static byte[] toByteArray(StringEnum value) {
		return toByteArray(value, EncodingHelper.UTF_8);
	}

	public static byte[] toByteArray(StringEnum value, String charsetName) {
		return toByteArray(EnumHelper.safeGet(value), charsetName);
	}

	public static byte[] toByteArray(String value) {
		return toByteArray(value, EncodingHelper.UTF_8);
	}

	/**
	 * String -> byte[]
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toByteArray(String value, String charsetName) {
		byte[] result = new byte[0];
		try {
			if (value != null) {
				if (charsetName != null) {
					// #issue 較慢
					// Charset charset = Charset.forName(charsetName);
					// result = value.getBytes(charset);

					// #fix
					result = value.getBytes(charsetName);

				} else {
					result = value.getBytes();
				}
			}
		} catch (Exception ex) {
		}
		return result;
	}

	public static byte[] toByteArray(ByteEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Byte value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	/**
	 * byte -> byte[]
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toByteArray(byte value) {
		byte[] result = new byte[1];
		result[0] = value;
		return result;
	}

	public static byte[] toByteArray(ShortEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Short value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	/**
	 * short -> byte[]
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toByteArray(short value) {
		byte[] result = new byte[2];
		result[1] = (byte) (value & 0x000000ff);
		result[0] = (byte) ((value & 0x0000ff00) >>> 8);
		return result;
	}

	public static byte[] toShortByteArray(IntEnum value) {
		return toShortByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toShortByteArray(Integer value) {
		return toShortByteArray(NumberHelper.safeGet(value));
	}

	/**
	 * int -> byte -> 1 byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteByteArray(int value) {
		AssertHelper.isBetween(value, Byte.MIN_VALUE, Byte.MAX_VALUE,
				"The Value is " + value + " must be between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE);
		//
		return toByteArray((byte) value);
	}

	/**
	 * int -> short -> 2 byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toShortByteArray(int value) {
		AssertHelper.isBetween(value, Short.MIN_VALUE, Short.MAX_VALUE,
				"The Value is " + value + " must be between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE);

		// byte[] result = new byte[2];
		// result[1] = (byte) (value & 0x00ff);
		// result[0] = (byte) ((value & 0x00ff) >>> 8);
		// return result;
		return toByteArray((short) value);
	}

	/**
	 * long -> int -> 4 byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toIntByteArray(long value) {
		AssertHelper.isBetween(value, Integer.MIN_VALUE, Integer.MAX_VALUE,
				"The Value is " + value + " must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
		//
		return toByteArray((int) value);
	}

	public static byte[] toByteArray(IntEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Integer value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	/**
	 * int -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(int value) {
		byte[] result = new byte[4];
		result[3] = (byte) (value & 0x000000ff);
		result[2] = (byte) ((value & 0x0000ff00) >>> 8);
		result[1] = (byte) ((value & 0x00ff0000) >>> 16);
		result[0] = (byte) ((value & 0xff000000) >>> 24);
		return result;
	}

	// 同toByteArray
	// public static byte[] ___getBytes(int val)
	// {
	// byte[] ret = new byte[4];
	// int v = val;
	// ret[3] = (byte) (v & 0x00ff);
	// ret[2] = (byte) (v >>= 8 & 0x00ff);
	// ret[1] = (byte) (v >>= 8 & 0x00ff);
	// ret[0] = (byte) (v >>= 8 & 0x00ff);
	// return ret;
	// }
	public static byte[] toByteArray(LongEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Long value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	/**
	 * long -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(long value) {
		byte[] result = new byte[8];

		result[7] = (byte) (value & 0x00000000000000ffl);
		result[6] = (byte) ((value & 0x000000000000ff00l) >>> 8);
		result[5] = (byte) ((value & 0x0000000000ff0000l) >>> 16);
		result[4] = (byte) ((value & 0x00000000ff000000l) >>> 24);

		result[3] = (byte) ((value & 0x000000ff00000000l) >>> 32);
		result[2] = (byte) ((value & 0x0000ff0000000000l) >>> 40);
		result[1] = (byte) ((value & 0x00ff000000000000l) >>> 48);
		result[0] = (byte) ((value & 0xff00000000000000l) >>> 56);
		return result;
	}

	public static byte[] toByteArray(FloatEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Float value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	public static byte[] toByteArray(float value) {
		int intValue = Float.floatToRawIntBits(value);
		return toByteArray(intValue);
	}

	public static byte[] toByteArray(DoubleEnum value) {
		return toByteArray(EnumHelper.safeGet(value));
	}

	public static byte[] toByteArray(Double value) {
		return toByteArray(NumberHelper.safeGet(value));
	}

	public static byte[] toByteArray(double value) {
		long longValue = Double.doubleToRawLongBits(value);
		return toByteArray(longValue);
	}

	/**
	 * 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(Serializable value) {
		// jdk 序列化
		return SerializeHelper.jdk(value);
	}

	/**
	 * 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param serializeType
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	public static byte[] toByteArray(SerializeType serializeType, Serializable value) {
		// jdk 序列化
		return SerializeHelper.jdk(value);
	}

	// -------------------------------------------------------
	// 0xff=255,16進位
	// = 1111 1111,(2進位)

	/**
	 * byte[] -> boolean
	 * 
	 * @param value
	 * @return
	 */
	public static boolean toBoolean(byte[] value) {
		return (value == null || value.length == 0) ? false : value[0] != 0x00;
	}

	public static char toChar(byte[] value) {
		if (value == null || value.length != 2) {
			return 0x00;
		}
		return (char) ((0xff & value[0]) << 8 | (0xff & value[1]) << 0);
	}

	public static String toString(byte[] value) {
		return toString(value, EncodingHelper.UTF_8);
	}

	/**
	 * byte[] -> String
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String toString(byte[] value, String charsetName) {
		String result = null;

		try {
			if (value != null) {
				// #issue 較慢
				// Charset charset = Charset.forName(charsetName);
				// result = new String(value, charset);

				// #fix
				result = new String(value, charsetName);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * byte[] -> byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte toByte(byte[] value) {
		return (value == null || value.length == 0) ? 0x0 : value[0];
	}

	/**
	 * byte[] -> short
	 * 
	 * @param value
	 * @return
	 */
	public static short toShort(byte[] value) {
		if (value == null || value.length != 2) {
			return 0x0;
		}
		return (short) ((0xff & value[0]) << 8 | (0xff & value[1]) << 0);
	}

	public static int fromShortInt(byte[] value) {
		return fromShortInt(value, 0);
	}

	/**
	 * (short)byte[] -> 2 byte -> short -> int
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int fromShortInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 1] & 0xff);
		result = result | ((value[offset + 0] & 0xff) << 8);
		return result;
	}

	public static int fromByteInt(byte[] value) {
		return fromByteInt(value, 0);
	}

	/**
	 * (byte)byte[] -> 1 byte -> byte -> int
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int fromByteInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 0] & 0xff);
		return result;
	}

	// 同toShortInt
	// public static int ___fromBytesShort(byte[] val, int offset)
	// {
	// int ret = 0;
	// ret |= (int) (val[offset + 1] & 0xff) << 0;
	// ret |= (int) (val[offset] & 0xff) << 8;
	//
	// return ret;
	// }

	public static long fromIntLong(byte[] value) {
		return fromIntLong(value, 0);
	}

	/**
	 * (int)byte[] -> 4 byte -> int -> long
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static long fromIntLong(byte[] value, int offset) {
		long result = 0;
		result = result | (value[offset + 3] & 0xff);
		result = result | (((long) (value[offset + 2] & 0xff)) << 8);
		result = result | (((long) (value[offset + 1] & 0xff)) << 16);
		result = result | (((long) (value[offset + 0] & 0xff)) << 24);
		return result;
	}

	public static int toInt(byte[] value) {
		return toInt(value, 0);
	}

	/**
	 * byte[] -> int
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int toInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 3] & 0xff);
		result = result | ((((int) value[offset + 2] & 0xff)) << 8);
		result = result | ((((int) value[offset + 1] & 0xff)) << 16);
		result = result | ((((int) value[offset + 0] & 0xff)) << 24);
		return result;
	}

	// 同toInt
	// public static int ___fromBytes(byte[] val)
	// {
	// int ret = 0;
	// ret |= (int) (val[3] & 0xff) << 0;
	// ret |= (int) (val[2] & 0xff) << 8;
	// ret |= (int) (val[1] & 0xff) << 16;
	// ret |= (int) (val[0] & 0xff) << 24;
	//
	// return ret;
	// }
	public static long toLong(byte[] value) {
		return toLong(value, 0);
	}

	/**
	 * byte[] -> long
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static long toLong(byte[] value, int offset) {
		long result = 0L;
		result = result | (value[7] & 0xff);
		result = result | (((long) value[offset + 6] & 0xff) << 8);
		result = result | (((long) value[offset + 5] & 0xff) << 16);
		result = result | (((long) value[offset + 4] & 0xff) << 24);
		result = result | (((long) value[offset + 3] & 0xff) << 32);
		result = result | (((long) value[offset + 2] & 0xff) << 40);
		result = result | (((long) value[offset + 1] & 0xff) << 48);
		result = result | (((long) value[offset + 0] & 0xff) << 56);
		return result;
	}

	public static float toFloat(byte[] value) {
		int intValue = toInt(value);
		float result = Float.intBitsToFloat(intValue);
		return result;
	}

	public static double toDouble(byte[] value) {
		long longValue = toLong(value);
		double result = Double.longBitsToDouble(longValue);
		return result;
	}

	/**
	 * 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	public static <T> T toObject(byte[] value) {
		// jdk 反序列化
		return SerializeHelper.dejdk(value);
	}

	// -------------------------------------------------------

	/**
	 * 列印byte[]內容
	 * 
	 * @param value
	 */
	public static void println(byte[] value) {
		StringBuffer sb = new StringBuffer();
		if (value != null) {
			// sb.append("[" + value.length + "]: ");
			for (int i = 0; i < value.length; i++) {
				sb.append(value[i]);
				if (i < value.length - 1) {
					sb.append(" ");
				}
			}
		}
		if (sb.length() > 0) {
			System.out.println(sb.toString());
		}
	}

	// 結果同toByteArray(Double value)
	// public static byte[] ___toByteArray(double value)
	// {
	// byte[] bytes = new byte[8];
	// ByteBuffer.wrap(bytes).putDouble(value);
	// return bytes;
	// }

	// 結果同toDouble(byte[] value)
	// public static double ___toDouble(byte[] bytes)
	// {
	// return ByteBuffer.wrap(bytes).getDouble();
	// }

	/**
	 * 讀取byte[], 從pos開始, 讀取長度length
	 * 
	 * 與UnsafeHelper.getByteArray() 效率一樣
	 * 
	 * @param value
	 * @param pos
	 * @param length
	 * @return
	 */
	public static byte[] getByteArray(byte[] value, int pos, int length) {
		byte[] result = new byte[length];
		try {
			System.arraycopy(value, pos, result, 0, length);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * byte[] 複製
	 * 
	 * 與UnsafeHelper.byteArraycopy() 效率一樣
	 * 
	 * @param src
	 * @param srcPos
	 * @param dest
	 * @param destPos
	 * @param length
	 */
	public static void byteArraycopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	/**
	 * 是否為空值
	 * 
	 * @param values
	 * @return
	 */
	public static boolean isEmpty(byte[] values) {
		return (values == null || values.length == 0);
	}

	/**
	 * 是否不為空值
	 * 
	 * @param values
	 * @return
	 */
	public static boolean notEmpty(byte[] values) {
		return (values != null && values.length > 0);
	}

	public static byte[] randomByteArray(int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = NumberHelper.randomByte();
		}
		return result;
	}

	// 可用其他方式將 int 轉 byte[]
	// 1.Using BigInteger:
	// private byte[] ___bigIntToByteArray(final int i)
	// {
	// BigInteger bigInt = BigInteger.valueOf(i);
	// return bigInt.toByteArray();
	// }

	// 2.Using DataOutputStream:
	// private byte[] ___intToByteArray(final int i) throws IOException
	// {
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// DataOutputStream dos = new DataOutputStream(bos);
	// dos.writeInt(i);
	// dos.flush();
	// return bos.toByteArray();
	// }

	// 3.Using ByteBuffer:
	// public byte[] ___intToByteArray(final int i)
	// {
	// ByteBuffer bb = ByteBuffer.allocate(4);
	// bb.putInt(i);
	// return bb.array();
	// }
}
