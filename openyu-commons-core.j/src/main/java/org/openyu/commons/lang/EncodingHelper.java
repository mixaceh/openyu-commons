package org.openyu.commons.lang;

//import java.net.URLDecoder;
//import java.net.URLEncoder;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class EncodingHelper.
 */
public class EncodingHelper extends BaseHelperSupporter {

	/** The Constant BIG5. */
	public static final String BIG5 = "BIG5";

	/** The Constant CP850. */
	public static final String CP850 = "CP850";

	/** The Constant GBK. */
	public static final String GBK = "GBK";

	/** The Constant UTF_8. */
	public static final String UTF_8 = "UTF-8";

	/** The Constant ISO_8859_1. */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/**
	 * Instantiates a new helper.
	 */
	private EncodingHelper() {
		if (InstanceHolder.INSTANCE != null) {
			throw new HelperException(
					new StringBuilder().append(getDisplayName()).append(" can not construct").toString());
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		// private static final EncodingHelper INSTANCE = new EncodingHelper();
		private static EncodingHelper INSTANCE = new EncodingHelper();
	}

	/**
	 * Gets the single instance of EncodingHelper.
	 *
	 * @return single instance of EncodingHelper
	 */
	public synchronized static EncodingHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new EncodingHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	// --------------------------------------------------

	/**
	 * 字串編碼
	 *
	 * @param value
	 * @param srcEncoding
	 * @param destEncoding
	 * @return
	 */
	public static String encodeString(String value, String srcEncoding, String destEncoding) {
		String result = null;
		//
		try {
			if (value != null) {
				byte[] bytes = value.getBytes(srcEncoding);
				result = new String(bytes, destEncoding);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Encode base64.
	 *
	 * @param value
	 *            the value
	 * @return the byte[]
	 */
	public static byte[] encodeBase64(String value) {
		return encodeBase64(value, false);
	}

	/**
	 * Encode base64.
	 *
	 * @param value
	 *            the value
	 * @param isChunked
	 *            the is chunked
	 * @return the string
	 */
	public static byte[] encodeBase64(String value, boolean isChunked) {
		return encodeBase64(value, UTF_8, isChunked);
	}

	/**
	 * Encode base64.
	 *
	 * @param value
	 *            the value
	 * @param charsetName
	 *            the charset name
	 * @return the byte[]
	 */
	public static byte[] encodeBase64(String value, String charsetName) {
		return encodeBase64(value, charsetName, false);
	}

	/**
	 * Encode base64.
	 *
	 * @param value
	 *            the value
	 * @param charsetName
	 *            the charset name
	 * @param isChunked
	 *            the is chunked
	 * @return the string
	 */
	public static byte[] encodeBase64(String value, String charsetName, boolean isChunked) {
		byte[] result = new byte[0];
		try {
			if (value != null) {
				result = encodeBase64(value.getBytes(charsetName), isChunked);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Encode base64.
	 *
	 * @param values
	 *            the values
	 * @param isChunked
	 *            the is chunked
	 * @return the byte[]
	 */
	public static byte[] encodeBase64(byte[] values, boolean isChunked) {
		byte[] result = new byte[0];
		try {
			// 不要再用 sun.BASE64Encoder,BASE64Decoder,
			// BASE64Encoder encoder = new BASE64Encoder();
			// result = encoder.encodeBuffer(values).trim();

			// 改用 apache commons codec
			result = Base64.encodeBase64(values, isChunked);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Encode base64 string.
	 *
	 * @param values
	 *            the values
	 * @return the string
	 */
	public static String encodeBase64String(byte[] values) {
		return encodeBase64String(values, false);
	}

	/**
	 * Encode base64 string.
	 *
	 * @param values
	 *            the values
	 * @param isChunked
	 *            the is chunked
	 * @return the string
	 */
	public static String encodeBase64String(byte[] values, boolean isChunked) {
		return encodeBase64String(values, UTF_8, isChunked);
	}

	/**
	 * Encode base64 string.
	 *
	 * @param values
	 *            the values
	 * @param charsetName
	 *            the charset name
	 * @return the string
	 */
	public static String encodeBase64String(byte[] values, String charsetName) {
		return encodeBase64String(values, charsetName, false);
	}

	/**
	 * Encode base64 string.
	 *
	 * @param values
	 *            the values
	 * @param charsetName
	 *            the charset name
	 * @param isChunked
	 *            the is chunked
	 * @return the string
	 */
	public static String encodeBase64String(byte[] values, String charsetName, boolean isChunked) {
		String result = null;
		try {
			byte[] buff = encodeBase64(values, isChunked);
			result = new String(buff, charsetName);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Decode base64.
	 *
	 * @param value
	 *            the value
	 * @return the byte[]
	 */
	public static byte[] decodeBase64(String value) {
		return decodeBase64(value, UTF_8);
	}

	/**
	 * Decode base64.
	 *
	 * @param value
	 *            the value
	 * @param charsetName
	 *            the charset name
	 * @return the byte[]
	 */
	public static byte[] decodeBase64(String value, String charsetName) {
		byte[] result = new byte[0];
		try {
			if (value != null) {
				result = decodeBase64(value.getBytes(charsetName));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Decode base64.
	 *
	 * @param values
	 *            the values
	 * @return the byte[]
	 */
	public static byte[] decodeBase64(byte[] values) {
		byte[] result = new byte[0];
		try {
			// 不要再用 sun.BASE64Encoder,BASE64Decoder
			// BASE64Decoder decoder = new BASE64Decoder();
			// result = decoder.decodeBuffer(value);

			// 改用 apache commons codec
			result = Base64.decodeBase64(values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Decode base64 string.
	 *
	 * @param values
	 *            the values
	 * @param charsetName
	 *            the charset name
	 * @return the string
	 */
	public static String decodeBase64String(byte[] values, String charsetName) {
		String result = null;
		try {
			byte[] buff = decodeBase64(values);
			result = new String(buff, charsetName);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * url編碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeUrl(String value) {
		return encodeUrl(value, UTF_8);
	}

	/**
	 * url編碼
	 *
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static String encodeUrl(String value, String encoding) {
		String result = null;
		try {
			if (value != null && encoding != null) {
				result = URLEncoder.encode(value, encoding);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * url解碼
	 *
	 * @param value
	 * @return
	 */
	public static String decodeUrl(String value) {
		return decodeUrl(value, UTF_8);
	}

	/**
	 * url解碼
	 *
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static String decodeUrl(String value, String encoding) {
		String result = null;
		try {
			if (value != null && encoding != null) {
				result = URLDecoder.decode(value, encoding);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String encodeHex(String value) {
		return encodeHex(value, UTF_8);
	}

	/**
	 *
	 * 編碼,將字串轉成16進位string,如:abc -> 616263
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String encodeHex(String value, String charsetName) {
		byte[] buffs = ByteHelper.toByteArray(value, charsetName);
		return encodeHex(buffs);
	}

	/**
	 * 編碼,將byte[]轉成16進位hexString,如:97, 98, 99 -> 616263
	 *
	 * @param value
	 * @return
	 */
	public static String encodeHex(byte[] values) {
		return encodeHex(values, true);
	}

	public static String encodeHex(byte[] values, boolean toLowerCase) {
		return new String(Hex.encodeHex(values, toLowerCase));
	}

	public static byte[] decodeHex(byte[] values) {
		return decodeHex(ByteHelper.toString(values));
	}

	/**
	 * 解碼,將16進位hexString轉成byte[],如:616263 -> 97, 98, 99
	 *
	 * @param hexValue
	 * @return
	 */
	public static byte[] decodeHex(String value) {
		try {
			return Hex.decodeHex(value.toCharArray());
		} catch (Exception ex) {
		}
		return null;
	}

	public static String decodeHexString(String value) {
		return decodeHexString(value, UTF_8);
	}

	/**
	 * 解碼,將16進位hexString轉成string,如:616263 -> abc
	 *
	 * @param hexValue
	 * @return
	 */
	public static String decodeHexString(String value, String charsetName) {
		byte[] buffs = decodeHex(value);
		return ByteHelper.toString(buffs, charsetName);
	}

	// /**
	// * char 轉 byte.
	// *
	// * @param ch
	// * the ch
	// * @return the byte
	// */
	// protected static byte toByte(char ch) {
	// switch (ch) {
	// case '0':
	// return 0x00;
	// case '1':
	// return 0x01;
	// case '2':
	// return 0x02;
	// case '3':
	// return 0x03;
	// case '4':
	// return 0x04;
	// case '5':
	// return 0x05;
	// case '6':
	// return 0x06;
	// case '7':
	// return 0x07;
	// case '8':
	// return 0x08;
	// case '9':
	// return 0x09;
	// case 'a':
	// return 0x0A;
	// case 'b':
	// return 0x0B;
	// case 'c':
	// return 0x0C;
	// case 'd':
	// return 0x0D;
	// case 'e':
	// return 0x0E;
	// case 'f':
	// return 0x0F;
	// }
	// return 0x00;
	// }

}
