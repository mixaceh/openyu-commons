package org.openyu.commons.lang;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.util.LocaleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//parseXxx(String) ->Number -> toXxx
//parseXxxs(array) -> array -> toXxxs
//format(Number) -> String ->toString
public final class NumberHelper extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(NumberHelper.class);

	public static final byte DEFAULT_BYTE = 0;

	public static final short DEFAULT_SHORT = 0;

	public static final int DEFAULT_INT = 0;

	public static final long DEFAULT_LONG = 0L;

	public static final float DEFAULT_FLOAT = 0f;

	public static final double DEFAULT_DOUBLE = 0d;

	//
	public static final String DEFAULT_PATTERN = "#,##0.##";

	// 精確度,精確到小數點以下4位
	public static final int DEFAULT_SCALE = 4;

	// 精確度,精確到小數點以下10位
	public static final int TEN_SCALE = 10;

	// 圓週率
	public static final double PI = Math.PI;

	// 自然指數
	public static final double E = Math.E;

	public static final Byte BYTE_ZERO = new Byte((byte) 0);

	public static final Byte BYTE_ONE = new Byte((byte) 1);

	public static final Byte BYTE_MINUS_ONE = new Byte((byte) -1);

	//
	public static final Short SHORT_ZERO = new Short((short) 0);

	public static final Short SHORT_ONE = new Short((short) 1);

	public static final Short SHORT_MINUS_ONE = new Short((short) -1);

	//
	public static final Integer INTEGER_ZERO = new Integer(0);

	public static final Integer INTEGER_ONE = new Integer(1);

	public static final Integer INTEGER_MINUS_ONE = new Integer(-1);

	//
	public static final Long LONG_ZERO = new Long(0L);

	public static final Long LONG_ONE = new Long(1L);

	public static final Long LONG_MINUS_ONE = new Long(-1L);

	//
	public static final Float FLOAT_ZERO = new Float(0f);

	public static final Float FLOAT_ONE = new Float(1f);

	public static final Float FLOAT_MINUS_ONE = new Float(-1f);

	//
	public static final Double DOUBLE_ZERO = new Double(0d);

	public static final Double DOUBLE_ONE = new Double(1d);

	public static final Double DOUBLE_MINUS_ONE = new Double(-1d);

	// 每次執行,若seed都是一樣,則都會得到相同的結果
	public static final Random random = new Random(System.nanoTime());

	private static SecureRandom secureRandom = null;

	public static final String SHA1PRNG = "SHA1PRNG";

	// 百位數
	public static final int HUND_DIGIT = 100;

	// 千位數
	public static final int THOUSAND_DIGIT = 1000;

	// 萬位數
	public static final int TEN_THOUSAND_DIGIT = 10000;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				secureRandom = SecureRandom.getInstance(SHA1PRNG);
				secureRandom.setSeed(System.nanoTime());
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}
		}
	}

	private NumberHelper() {
		throw new HelperException(
				new StringBuilder().append(NumberHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal createBigDecimal(double value) {
		return createBigDecimal(value, DEFAULT_DOUBLE, null, null);
	}

	public static BigDecimal createBigDecimal(Object value) {
		return createBigDecimal(value, DEFAULT_DOUBLE, null, null);
	}

	public static BigDecimal createBigDecimal(Object value, double defaultValue) {
		return createBigDecimal(value, defaultValue, null, null);
	}

	public static BigDecimal createBigDecimal(Object value, double defaultValue, String pattern) {
		return createBigDecimal(value, defaultValue, pattern, null);
	}

	/**
	 *
	 * double -> BigDecimal,要用Double.toString(value)轉成string, 再new BigDecimal Double
	 * -> BigDecimal,要用value.toString()轉成string, 再new BigDecimal
	 *
	 * 否則會有誤差
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal createBigDecimal(Object value, double defaultValue, String pattern, Locale locale) {
		BigDecimal result = BigDecimal.ZERO;
		try {

			if (value instanceof Double && !Double.isNaN((Double) value) && !Double.isInfinite((Double) value)) {
				result = new BigDecimal(value.toString());
			} else if (value instanceof Float && !Float.isNaN((Float) value) && !Float.isInfinite((Float) value)) {
				result = new BigDecimal(value.toString());
			} else if (value instanceof String) {
				double doubleValue = toDouble(value, defaultValue, pattern, locale);
				result = new BigDecimal(Double.toString(doubleValue));
			} else if (value instanceof Byte || value instanceof Short || value instanceof Integer
					|| value instanceof Long) {
				result = new BigDecimal(value.toString());
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		return result;
	}

	// --------------------------------------------------------
	public static byte toByte(Object value) {
		return toByte(value, DEFAULT_BYTE);
	}

	public static byte toByte(Object value, int defaultValue) {
		return toByte(value, defaultValue, null);
	}

	public static byte toByte(Object value, int defaultValue, String pattern) {
		return toByte(value, defaultValue, pattern, null);
	}

	/**
	 * 可判斷 Number,String
	 *
	 * @param value
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static byte toByte(Object value, int defaultValue, String pattern, Locale locale) {
		byte result = (byte) defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				result = ((Number) value).byteValue();
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).byteValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static byte[] toBytes(Byte[] values) {
		return toBytes(values, DEFAULT_BYTE);
	}

	public static byte[] toBytes(Byte[] values, int defaultValue) {
		byte[] results = new byte[0];
		if (values != null) {
			results = new byte[values.length];
			for (int i = 0; i < results.length; i++) {
				Byte element = values[i];
				try {
					results[i] = (element != null ? element.byteValue() : (byte) defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Byte[] toBytes(byte[] values) {
		return toBytes(values, DEFAULT_BYTE);
	}

	public static Byte[] toBytes(byte[] values, int defaultValue) {
		Byte[] results = new Byte[0];
		if (values != null) {
			results = new Byte[values.length];
			for (int i = 0; i < results.length; i++) {
				byte element = values[i];
				try {
					results[i] = new Byte(element);
				} catch (Exception ex) {
					results[i] = (byte) defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------
	public static short toShort(Object value) {
		return toShort(value, DEFAULT_SHORT);
	}

	public static short toShort(Object value, int defaultValue) {
		return toShort(value, defaultValue, null);
	}

	public static short toShort(Object value, int defaultValue, String pattern) {
		return toShort(value, defaultValue, pattern, null);
	}

	public static short toShort(Object value, int defaultValue, String pattern, Locale locale) {
		short result = (short) defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				result = ((Number) value).shortValue();
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).shortValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static short[] toShorts(Short[] values) {
		return toShorts(values, DEFAULT_SHORT);
	}

	public static short[] toShorts(Short[] values, int defaultValue) {
		short[] results = new short[0];
		if (values != null) {
			results = new short[values.length];
			for (int i = 0; i < results.length; i++) {
				Short element = values[i];
				try {
					results[i] = (element != null ? element.shortValue() : (short) defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Short[] toShorts(short[] values) {
		return toShorts(values, DEFAULT_SHORT);
	}

	public static Short[] toShorts(short[] values, int defaultValue) {
		Short[] results = new Short[0];
		if (values != null) {
			results = new Short[values.length];
			for (int i = 0; i < results.length; i++) {
				short element = values[i];
				try {
					results[i] = new Short(element);
				} catch (Exception ex) {
					results[i] = (short) defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------
	public static int toInt(long value) {
		int result = 0;
		if (value >= Integer.MAX_VALUE) {
			result = Integer.MAX_VALUE;
		} else if (value <= Integer.MIN_VALUE) {
			result = Integer.MIN_VALUE;
		} else {
			result = (int) value;
		}
		return result;
	}

	public static int toInt(Object value) {
		return toInt(value, DEFAULT_INT);
	}

	public static int toInt(Object value, int defaultValue) {
		return toInt(value, defaultValue, null);
	}

	public static int toInt(Object value, int defaultValue, String pattern) {
		return toInt(value, defaultValue, pattern, null);
	}

	/**
	 * 可判斷 Number,String
	 *
	 * @param value
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static int toInt(Object value, int defaultValue, String pattern, Locale locale) {
		int result = defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				if (value instanceof Long) {
					Long longValue = (Long) value;
					result = toInt(longValue.longValue());
				} else {
					result = ((Number) value).intValue();
				}
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).intValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------
	public static int[] toInts(Integer[] values) {
		return toInts(values, DEFAULT_INT);
	}

	public static int[] toInts(Integer[] values, int defaultValue) {
		int[] results = new int[0];
		if (values != null) {
			results = new int[values.length];
			for (int i = 0; i < results.length; i++) {
				Integer element = values[i];
				try {
					results[i] = (element != null ? element.intValue() : defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Integer[] toInts(int[] values) {
		return toInts(values, DEFAULT_INT);
	}

	public static Integer[] toInts(int[] values, int defaultValue) {
		Integer[] results = new Integer[0];
		if (values != null) {
			results = new Integer[values.length];
			for (int i = 0; i < results.length; i++) {
				int element = values[i];
				try {
					results[i] = new Integer(element);
				} catch (Exception ex) {
					results[i] = defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------

	public static long toLong(Object value) {
		return toLong(value, DEFAULT_LONG);
	}

	public static long toLong(Object value, long defaultValue) {
		return toLong(value, defaultValue, null);
	}

	public static long toLong(Object value, long defaultValue, String pattern) {
		return toLong(value, defaultValue, pattern, null);
	}

	/**
	 * 可判斷 Number,String
	 *
	 * @param value
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static long toLong(Object value, long defaultValue, String pattern, Locale locale) {
		long result = defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				result = ((Number) value).longValue();
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).longValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static long[] toLongs(Long[] values) {
		return toLongs(values, DEFAULT_LONG);
	}

	public static long[] toLongs(Long[] values, long defaultValue) {
		long[] results = new long[0];
		if (values != null) {
			results = new long[values.length];
			for (int i = 0; i < results.length; i++) {
				Long element = values[i];
				try {
					results[i] = (element != null ? element.longValue() : defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Long[] toLongs(long[] values) {
		return toLongs(values, DEFAULT_LONG);
	}

	public static Long[] toLongs(long[] values, long defaultValue) {
		Long[] results = new Long[0];
		if (values != null) {
			results = new Long[values.length];
			for (int i = 0; i < results.length; i++) {
				long element = values[i];
				try {
					results[i] = new Long(element);
				} catch (Exception ex) {
					results[i] = defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------
	public static float toFloat(Object value) {
		return toFloat(value, 0f);
	}

	public static float toFloat(Object value, float defaultValue) {
		return toFloat(value, defaultValue, null);
	}

	public static float toFloat(Object value, float defaultValue, String pattern) {
		return toFloat(value, defaultValue, pattern, null);
	}

	/**
	 * 可判斷 Number,String
	 *
	 * @param value
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static float toFloat(Object value, float defaultValue, String pattern, Locale locale) {
		float result = defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				result = ((Number) value).floatValue();
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).floatValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static float[] toFloats(Float[] values) {
		return toFloats(values, DEFAULT_FLOAT);
	}

	public static float[] toFloats(Float[] values, float defaultValue) {
		float[] results = new float[0];
		if (values != null) {
			results = new float[values.length];
			for (int i = 0; i < results.length; i++) {
				Float element = values[i];
				try {
					results[i] = (element != null ? element.floatValue() : defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Float[] toFloats(float[] values) {
		return toFloats(values, DEFAULT_FLOAT);
	}

	public static Float[] toFloats(float[] values, float defaultValue) {
		Float[] results = new Float[0];
		if (values != null) {
			results = new Float[values.length];
			for (int i = 0; i < results.length; i++) {
				float element = values[i];
				try {
					results[i] = new Float(element);
				} catch (Exception ex) {
					results[i] = defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------

	public static double toDouble(Object value) {
		return toDouble(value, DEFAULT_DOUBLE);
	}

	public static double toDouble(Object value, double defaultValue) {
		return toDouble(value, defaultValue, null);
	}

	public static double toDouble(Object value, double defaultValue, String pattern) {
		return toDouble(value, defaultValue, pattern, null);
	}

	/**
	 * 可判斷 Number,String
	 *
	 * @param value
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static double toDouble(Object value, double defaultValue, String pattern, Locale locale) {
		double result = defaultValue;
		try {
			StringBuilder sb = new StringBuilder();
			if (value instanceof Number) {
				result = ((Number) value).doubleValue();
			} else if (value instanceof String) {
				sb.append(value);
			}
			//
			if (!StringHelper.isEmpty(sb.toString())) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.parse(sb.toString()).doubleValue();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------
	public static double[] toDoubles(Double[] values) {
		return toDoubles(values, DEFAULT_DOUBLE);
	}

	public static double[] toDoubles(Double[] values, double defaultValue) {
		double[] results = new double[0];
		if (values != null) {
			results = new double[values.length];
			for (int i = 0; i < results.length; i++) {
				Double element = values[i];
				try {
					results[i] = (element != null ? element.doubleValue() : defaultValue);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	public static Double[] toDoubles(double[] values) {
		return toDoubles(values, DEFAULT_DOUBLE);
	}

	public static Double[] toDoubles(double[] values, double defaultValue) {
		Double[] results = new Double[0];
		if (values != null) {
			results = new Double[values.length];
			for (int i = 0; i < results.length; i++) {
				double element = values[i];
				try {
					results[i] = new Double(element);
				} catch (Exception ex) {
					results[i] = defaultValue;
					// ex.printStackTrace();
				}
			}
		}
		return results;
	}

	// --------------------------------------------------------

	public static <T> String toString(T value) {
		return toString(value, null);
	}

	public static <T> String toString(T value, String pattern) {
		return toString(value, pattern, null);
	}

	public static <T> String toString(T value, String pattern, Locale locale) {
		String result = "";
		//
		try {
			if (value != null) {
				DecimalFormat df = createDecimalFormat(pattern, locale);
				result = df.format(value);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static DecimalFormat createDecimalFormat() {
		return createDecimalFormat(null);
	}

	public static DecimalFormat createDecimalFormat(String pattern) {
		return createDecimalFormat(pattern, null);
	}

	public static DecimalFormat createDecimalFormat(String pattern, Locale locale) {
		DecimalFormat result = null;
		//
		try {
			String newPattern = (pattern != null ? pattern : DEFAULT_PATTERN);
			Locale newLocale = (locale != null ? locale : LocaleHelper.getLocale());
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(newLocale);
			result = new DecimalFormat(newPattern, symbols);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------
	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double add(double value1, double value2) {
		return add(value1, value2, DEFAULT_DOUBLE, DEFAULT_DOUBLE);
	}

	public static double add(Object value1, Object value2) {
		return add(value1, value2, DEFAULT_DOUBLE, DEFAULT_DOUBLE);
	}

	public static double add(Object value1, Object value2, double defaultValue1, double defaultValue2) {
		return add(value1, value2, defaultValue1, defaultValue2, null, null);
	}

	public static double add(Object value1, Object value2, double defaultValue1, double defaultValue2, String pattern,
			Locale locale) {
		BigDecimal big1 = createBigDecimal(value1, defaultValue1, pattern, locale);
		BigDecimal big2 = createBigDecimal(value2, defaultValue2, pattern, locale);
		return big1.add(big2).doubleValue();
	}

	// --------------------------------------------------------

	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * value1 - value2
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double subtract(double value1, double value2) {
		return subtract(value1, value2, DEFAULT_DOUBLE, DEFAULT_DOUBLE);
	}

	public static double subtract(Object value1, Object value2) {
		return subtract(value1, value2, DEFAULT_DOUBLE, DEFAULT_DOUBLE);
	}

	public static double subtract(Object value1, Object value2, double defaultValue1, double defaultValue2) {
		return subtract(value1, value2, defaultValue1, defaultValue2, null, null);
	}

	public static double subtract(Object value1, Object value2, double defaultValue1, double defaultValue2,
			String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value1, defaultValue1, pattern, locale);
		BigDecimal big2 = createBigDecimal(value2, defaultValue2, pattern, locale);
		return big1.subtract(big2).doubleValue();
	}

	// --------------------------------------------------------
	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double multiply(double value1, double value2) {
		return multiply(value1, value2, TEN_SCALE);
	}

	public static double multiply(Object value1, Object value2) {
		return multiply(value1, value2, TEN_SCALE);
	}

	public static double multiply(Object value1, Object value2, int scale) {
		return multiply(value1, value2, scale, 0d, 0d);
	}

	public static double multiply(Object value1, Object value2, int scale, double defaultValue1, double defaultValue2) {
		return multiply(value1, value2, scale, defaultValue1, defaultValue2, null, null);

	}

	/**
	 * 乘法,預設精確到小數點以下10位
	 *
	 * @param value1
	 * @param value2
	 * @param scale
	 * @param defaultValue1
	 * @param defaultValue2
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static double multiply(Object value1, Object value2, int scale, double defaultValue1, double defaultValue2,
			String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value1, defaultValue1, pattern, locale);
		BigDecimal big2 = createBigDecimal(value2, defaultValue2, pattern, locale);
		return round(big1.multiply(big2).doubleValue(), scale);
	}

	// --------------------------------------------------------
	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double divide(double value1, double value2) {
		return divide(value1, value2, TEN_SCALE);
	}

	public static double divide(Object value1, Object value2) {
		return divide(value1, value2, TEN_SCALE);
	}

	public static double divide(Object value1, Object value2, int scale) {
		return divide(value1, value2, scale, 0d, 0d);
	}

	public static double divide(Object value1, Object value2, int scale, double defaultValue1, double defaultValue2) {
		return divide(value1, value2, scale, defaultValue1, defaultValue2, null, null);
	}

	/**
	 * 除法,預設精確到小數點以下10位
	 *
	 * @param value
	 *            Object
	 * @return double
	 */
	public static double divide(Object value1, Object value2, int scale, double defaultValue1, double defaultValue2,
			String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value1, defaultValue1, pattern, locale);
		BigDecimal big2 = createBigDecimal(value2, defaultValue2, pattern, locale);
		// System.out.println(big1+" "+big2);
		return (big2.doubleValue() == 0) ? 0 : big1.divide(big2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// --------------------------------------------------------
	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value
	 * @return
	 */
	public static double round(double value) {
		return round(value, DEFAULT_SCALE);
	}

	public static double round(Object value) {
		return round(value, DEFAULT_SCALE);
	}

	public static double round(Object value, int scale) {
		return round(value, scale, DEFAULT_DOUBLE);
	}

	public static double round(Object value, int scale, double defaultValue) {
		return round(value, scale, defaultValue, null);
	}

	public static double round(Object value, int scale, double defaultValue, String pattern) {
		return round(value, scale, defaultValue, pattern, null);
	}

	/**
	 *
	 * 四捨五入,預設精確到小數點以下4位
	 *
	 * @param value
	 * @param scale
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static double round(Object value, int scale, double defaultValue, String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value, defaultValue, pattern, locale);
		BigDecimal big2 = createBigDecimal("1");
		return big1.divide(big2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// --------------------------------------------------------
	public static double up(double value) {
		return up(value, DEFAULT_SCALE);
	}

	public static double up(Object value) {
		return up(value, DEFAULT_SCALE);
	}

	public static double up(Object value, int scale) {
		return up(value, scale, DEFAULT_DOUBLE);
	}

	public static double up(Object value, int scale, double defaultValue) {
		return up(value, scale, defaultValue, null);
	}

	public static double up(Object value, int scale, double defaultValue, String pattern) {
		return up(value, scale, defaultValue, pattern, null);
	}

	/**
	 *
	 * 無條件進位,預設精確到小數點以下4位
	 *
	 * @param value
	 * @param scale
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static double up(Object value, int scale, double defaultValue, String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value, defaultValue, pattern, locale);
		BigDecimal big2 = createBigDecimal("1");
		return big1.divide(big2, scale, BigDecimal.ROUND_UP).doubleValue();
	}

	// --------------------------------------------------------
	/**
	 *
	 * 處理基本型別轉換: byte,short,int,long,double,float
	 *
	 * @param value
	 * @return
	 */
	public static double down(double value) {
		return down(value, DEFAULT_SCALE);
	}

	public static double down(Object value) {
		return down(value, DEFAULT_SCALE);
	}

	public static double down(Object value, int scale) {
		return down(value, scale, DEFAULT_DOUBLE);
	}

	public static double down(Object value, int scale, double defaultValue) {
		return down(value, scale, defaultValue, null);
	}

	public static double down(Object value, int scale, double defaultValue, String pattern) {
		return down(value, scale, defaultValue, pattern, null);
	}

	/**
	 *
	 * 無條件捨去,預設精確到小數點以下4位
	 *
	 * @param value
	 * @param scale
	 * @param defaultValue
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static double down(Object value, int scale, double defaultValue, String pattern, Locale locale) {
		BigDecimal big1 = createBigDecimal(value, defaultValue, pattern, locale);
		BigDecimal big2 = createBigDecimal("1");
		return big1.divide(big2, scale, BigDecimal.ROUND_DOWN).doubleValue();
	}

	/**
	 * <p>
	 * 從大到小的順序:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum double
	 * <li>Normal positve numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum double (<code>-Double.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * </p>
	 *
	 * <ul>
	 * <li>value1 > value2 =>1
	 * <li>value1 < value2 =>-1
	 * <li>value1 == value2 =>0
	 * <li>比較兩個 Nan==Nan => false, => 改為 true
	 * <li>比較兩個 -0.0==+0.0 => true,=> 改為 false;
	 * <ul>
	 *
	 * @param value1
	 *            double
	 * @param value2
	 *            double
	 * @return int
	 */
	public static int compare(double value1, double value2) {
		if (value1 < value2) {
			return -1;
		}
		if (value1 > value2) {
			return 1;
		}
		//
		long value1Bits = Double.doubleToLongBits(value1);
		long value2Bits = Double.doubleToLongBits(value2);
		if (value1Bits == value2Bits) {
			return 0;
		}
		//
		if (value1Bits < value2Bits) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * <p>
	 * 從大到小的順序:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum double
	 * <li>Normal positve numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum double (<code>-Double.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * </p>
	 *
	 * <ul>
	 * <li>value1 > value2 =>1
	 * <li>value1 < value2 =>-1
	 * <li>value1 == value2 =>0
	 * <li>比較兩個 Nan==Nan => false, => 改為 true
	 * <li>比較兩個 -0.0==+0.0 => true,=> 改為 false;
	 * <ul>
	 *
	 * @param value1
	 *            float
	 * @param value2
	 *            float
	 * @return int
	 */
	public static int compare(float value1, float value2) {
		if (value1 < value2) {
			return -1;
		}
		if (value1 > value2) {
			return 1;
		}
		//
		int value1Bits = Float.floatToIntBits(value1);
		int value2Bits = Float.floatToIntBits(value2);
		if (value1Bits == value2Bits) {
			return 0;
		}
		//
		if (value1Bits < value2Bits) {
			return -1;
		} else {
			return 1;
		}
	}

	// +-300 => true
	public static boolean isNumeric(String value) {
		boolean result = false;
		try {
			Double.parseDouble(value);
			result = true;
		} catch (Exception ex) {
		}
		return result;

	}

	// +-300 => false
	public static boolean ___isNumeric2(String value) {
		for (int i = value.length(); --i >= 0;) {
			if (!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// +-300 => false
	public static boolean ___isNumeric3(String value) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(value).matches();
	}

	// +-300 => false
	public static boolean ___isNumeric4(String value) {
		for (int i = value.length(); --i >= 0;) {
			int chr = value.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	public static String extractNumeric(String value) {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (Character.isDigit(ch)) {
				ret.append(ch);
			}
		}
		return ret.toString();
	}

	/**
	 * 如:70%機率 -> randomWin(70) ->7000/10000
	 *
	 * @param rateValue
	 * @return
	 */
	public static boolean randomWin(int rateValue) {
		return randomWin(rateValue, TEN_THOUSAND_DIGIT);
	}

	/**
	 * 如:70%機率 -> randomWin(7000,10000) ->70/10000
	 *
	 * @param rateValue
	 *            成功機率 0 <= rateValue < maxValue
	 * @return
	 */
	public static boolean randomWin(int rateValue, int maxValue) {
		boolean result = false;
		if (rateValue < maxValue) {
			result = (randomInt(0, maxValue) < rateValue);// 0 <= rate < 100
		}
		return result;
	}

	/**
	 * 如:70%機率 -> randomWin(0.7)
	 *
	 * @param rateValue
	 *            成功機率 0 <= rate < 1
	 * @return
	 */
	public static boolean randomWin(double rateValue) {
		return (randomDouble(0, 1) < rateValue); // 0 <= rate < 1
	}

	/**
	 * 隨機布林 true/false
	 *
	 * @return
	 */
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}

	/**
	 * 隨機byte整數 Byte.MIN_VALUE <= i < Byte.MAX_VALUE,無檢查是否重覆
	 *
	 * @return
	 */
	public static byte randomByte() {
		return (byte) randomInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
	}

	/**
	 * 隨機byte整數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 * @return
	 */
	public static byte randomByte(byte maxValue) {
		return (byte) randomInt(0, maxValue);
	}

	/**
	 * 隨機短整數 Short.MIN_VALUE <= i < Short.MAX_VALUE,無檢查是否重覆
	 *
	 * @return
	 */
	public static short randomShort() {
		return (short) randomInt(Short.MIN_VALUE, Short.MAX_VALUE);
	}

	/**
	 * 隨機短整數 0 <= i <= maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 * @return
	 */
	public static short randomShort(short maxValue) {
		return (short) randomInt(0, maxValue);
	}

	/**
	 * 隨機整數 Integer.MIN_VALUE <= i < Integer.MAX_VALUE,無檢查是否重覆
	 *
	 * @return
	 */
	public static int randomInt() {
		return random.nextInt();
	}

	/**
	 * 隨機整數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 * @return
	 */
	public static int randomInt(int maxValue) {
		return random.nextInt(maxValue);
	}

	/**
	 * 隨機整數 minValue <= i <= maxValue,無檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static int randomInt(int minValue, int maxValue) {
		return random.nextInt(maxValue - minValue + 1) + minValue;
	}

	/**
	 * 隨機整數 minValue <= i <= maxValue,有檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 * @param amount
	 */
	public static int[] randomUniqueInt(int minValue, int maxValue, int amount) {
		if (minValue >= maxValue) {
			return new int[0];
		}
		int index = maxValue - minValue;
		int size = index + 1;
		if (size < amount) {
			return new int[0];
		}
		int[] ret = new int[size];
		for (int i = 0; i < size; i++) {
			ret[i] = i + minValue;
		}
		for (int i = 0; i < size - 1; i++) {
			int randomIndex = randomInt(i, index);
			int temp = ret[i];
			ret[i] = ret[randomIndex];
			ret[randomIndex] = temp;
		}
		return Arrays.copyOfRange(ret, 0, amount);
	}

	/**
	 * 隨機長整數 Long.MIN_VALUE <= i < Long.MAX_VALUE,無檢查是否重覆
	 *
	 * @return
	 */
	public static long randomLong() {
		return random.nextLong();
	}

	// fix random.nextLong() -> nextLong(n)
	/**
	 * 隨機長整數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 * @return
	 */
	public static long randomLong(long maxValue) {
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do {
			bits = (random.nextLong() << 1) >>> 1;
			val = bits % maxValue;
		} while (bits - val + (maxValue - 1) < 0L);
		return val;
	}

	/**
	 * 隨機長整數 minValue <= i <= maxValue,無檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 */
	public static long randomLong(long minValue, long maxValue) {
		return randomLong(maxValue - minValue + 1) + minValue;
	}

	/**
	 * 隨機單精倍數 0 <= i < 1,無檢查是否重覆
	 */
	public static float randomFloat() {
		return random.nextFloat();
	}

	/**
	 * 隨機單精倍數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 */
	public static float randomFloat(float maxValue) {
		return randomFloat(0, maxValue);
	}

	/**
	 * 隨機單精倍數 minValue <= i <= maxValue,無檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 */
	public static float randomFloat(float minValue, float maxValue) {
		return random.nextFloat() * (maxValue - minValue + 1) + minValue;
	}

	/**
	 * 隨機雙精倍數 0 <= i < 1,無檢查是否重覆
	 *
	 * @return
	 */
	public static double randomDouble() {
		return random.nextDouble();
	}

	/**
	 * 隨機雙精倍數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * @param maxValue
	 * @return
	 */
	public static double randomDouble(double maxValue) {
		return randomDouble(0, maxValue);
	}

	/**
	 * 隨機雙精倍數 minValue <= i <= maxValue,無檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static double randomDouble(double minValue, double maxValue) {
		return random.nextDouble() * (maxValue - minValue + 1) + minValue;
	}

	/**
	 * 隨機整數 Integer.MIN_VALUE <= i < Integer.MAX_VALUE,無檢查是否重覆
	 *
	 * @return
	 */
	public static int secureRandomInt() {
		return secureRandom.nextInt();
	}

	/**
	 * 隨機整數 0 <= i < maxValue,無檢查是否重覆
	 *
	 * This would return a value from 0 (inclusive) to maxvalue (exclusive).
	 *
	 * @param maxValue
	 * @return
	 */
	public static int secureRandomInt(int maxValue) {
		return secureRandom.nextInt(maxValue);
	}

	/**
	 * 隨機整數 minValue <= i <= maxValue,無檢查是否重覆
	 *
	 * @param minValue
	 * @param maxValue
	 */
	public static int secureRandomInt(int minValue, int maxValue) {
		return secureRandom.nextInt(maxValue - minValue + 1) + minValue;
	}

	public static byte safeGet(Byte value) {
		return (value != null ? value : DEFAULT_BYTE);
	}

	public static short safeGet(Short value) {
		return (value != null ? value : DEFAULT_SHORT);
	}

	public static int safeGet(Integer value) {
		return (value != null ? value : DEFAULT_INT);
	}

	public static long safeGet(Long value) {
		return (value != null ? value : DEFAULT_LONG);
	}

	public static float safeGet(Float value) {
		return (value != null ? value : DEFAULT_FLOAT);
	}

	public static double safeGet(Double value) {
		return (value != null ? value : DEFAULT_DOUBLE);
	}

	/**
	 * 兩數相加是否溢位
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isAddOverflow(Integer value1, Integer value2) {
		boolean result = false;
		Integer tempValue = Integer.MAX_VALUE - value1;
		if (tempValue < value2) {
			result = true;
		}
		return result;
	}

	/**
	 * 兩數相乘是否溢位
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isMultiplyOverflow(Integer value1, Integer value2) {
		boolean result = false;
		if (value1 != 0) {
			Integer tempValue = Integer.MAX_VALUE / value1;
			if (tempValue < value2) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 兩數相加是否溢位
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isAddOverflow(Long value1, Long value2) {
		boolean result = false;
		Long tempValue = Long.MAX_VALUE - value1;
		if (tempValue < value2) {
			result = true;
		}
		return result;
	}

	/**
	 * 兩數相乘是否溢位
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isMultiplyOverflow(Long value1, Long value2) {
		boolean result = false;
		if (value1 != 0) {
			Long tempValue = Long.MAX_VALUE / value1;
			if (tempValue < value2) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 比例, rate/10000
	 *
	 * @param rate
	 *            萬分數
	 * @return
	 */
	public static double ratio(int rate) {
		return divide(rate, TEN_THOUSAND_DIGIT, 4);
	}

}
