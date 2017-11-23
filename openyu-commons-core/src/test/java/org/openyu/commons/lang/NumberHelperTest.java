package org.openyu.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.openyu.commons.security.SecurityHelper;
import org.openyu.commons.util.LocaleHelper;

public class NumberHelperTest {

	@Test
	// 1000000 times: 378 mills.
	// 1000000 times: 375 mills.
	// 1000000 times: 374 mills.
	public void createDecimalFormat() {
		DecimalFormat result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.createDecimalFormat();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 314 mills.
	// 1000000 times: 334 mills.
	// 1000000 times: 303 mills.
	public void createBigDecimal() {
		BigDecimal result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.createBigDecimal("3.141592653589793");
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(0, Double.compare(3.141592653589793, result.doubleValue()));
		//
		result = NumberHelper.createBigDecimal(3.141592653589793);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(new Double(3.141592653589793));
		System.out.println(result);
		result = NumberHelper.createBigDecimal((Double) null);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal((byte) 10);
		System.out.println(result);
		result = NumberHelper.createBigDecimal((short) 10);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(10);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(10L);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(10f);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(10d);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 314 mills.
	// 1000000 times: 334 mills.
	// 1000000 times: 303 mills.
	public void createBigDecimalByFloat() {
		BigDecimal result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.createBigDecimal("3.1415927");
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(0, Float.compare(3.1415927f, result.floatValue()));
		//
		result = NumberHelper.createBigDecimal(3.1415927f);
		System.out.println(result);
		result = NumberHelper.createBigDecimal(new Float(3.1415927));
		System.out.println(result);
		result = NumberHelper.createBigDecimal((Float) null);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 314 mills.
	// 1000000 times: 334 mills.
	// 1000000 times: 303 mills.
	public void createBigDecimalByPrimitive() {
		BigDecimal result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.createBigDecimal(1);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(1, result.intValue());
		//
		result = NumberHelper.createBigDecimal((byte) 1);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal((short) 1);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(1);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(1L);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(1f);
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(1d);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 314 mills.
	// 1000000 times: 334 mills.
	// 1000000 times: 303 mills.
	public void createBigDecimalByObject() {
		BigDecimal result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.createBigDecimal(new Integer(1));
			// result = NumberHelper.createBigDecimal(new Date());
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(1, result.intValue());
		//
		result = NumberHelper.createBigDecimal(new Byte((byte) 1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Short((short) 1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Integer(1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Long(1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Float(1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Double(1));
		System.out.println(result);
		//
		result = NumberHelper.createBigDecimal(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 636 mills.
	// 1000000 times: 627 mills.
	// 1000000 times: 649 mills.
	public void toByte() {
		byte result = 0;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.toByte("127");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(127, result);
		//
		result = NumberHelper.toByte(new Byte((byte) 123));
		System.out.println(result);
		result = NumberHelper.toByte(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 93 mills.
	// 1000000 times: 109 mills.
	// 1000000 times: 94 mills.
	public void toBytes() {
		Byte[] values = new Byte[] { new Byte((byte) 1), null, new Byte((byte) 128) };
		SystemHelper.println(values);

		byte[] results = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			results = NumberHelper.toBytes(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(results);
		assertEquals(values.length, results.length);
	}

	@Test
	// 1000000 times: 636 mills.
	// 1000000 times: 627 mills.
	// 1000000 times: 649 mills.
	public void toShort() {
		short result = 0;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.toShort("32767");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals((short) 32767, result);
		//
		result = NumberHelper.toByte(new Short((short) 123));
		System.out.println(result);
		result = NumberHelper.toByte(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 36 mills.
	// 1000000 times: 33 mills.
	// 1000000 times: 35 mills.
	public void toShorts() {
		Short[] values = new Short[] { new Short((short) 1), null, new Short((short) 32768) };
		SystemHelper.println(values);

		short[] results = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			results = NumberHelper.toShorts(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(results);
		assertEquals(values.length, results.length);
	}

	@Test
	// 1000000 times: 1701 mills.
	// 1000000 times: 1710 mills.
	// 1000000 times: 1689 mills.
	public void toFloat() {
		float result = 0f;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.toFloat("3.14159265358979323846");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(3.1415927f, result);
		//
		result = NumberHelper.toFloat(new Float(123.1));
		System.out.println(result);
		result = NumberHelper.toFloat(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 93 mills.
	// 1000000 times: 109 mills.
	// 1000000 times: 94 mills.
	public void toFloats() {
		Float[] values = new Float[] { new Float(1), null, new Float(100), Float.NaN };
		SystemHelper.println(values);

		float[] results = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			results = NumberHelper.toFloats(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(results);
		assertEquals(values.length, results.length);
	}

	@Test
	// #issue 太慢
	// 1000000 times: 2616 mills.
	// 1000000 times: 2415 mills.
	// 1000000 times: 2470 mills.
	//
	// #fix cache
	// 1000000 times: 1597 mills.
	// 1000000 times: 1758 mills.
	// 1000000 times: 1833 mills.
	public void toDouble() throws ParseException {
		double result = 0d;
		//
		DecimalFormat df = new DecimalFormat();
		System.out.println(df.parse("1"));
		//
		result = NumberHelper.toDouble("1");
		System.out.println(result);
		//
		result = NumberHelper.toDouble("3.14159265358979323846");
		System.out.println(result);
		assertEquals(0, Double.compare(3.141592653589793, result));
		//
		result = NumberHelper.toDouble(new Double(123.1));
		System.out.println(result);
		result = NumberHelper.toDouble(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 93 mills.
	// 1000000 times: 109 mills.
	// 1000000 times: 94 mills.
	public void toDoubles() {
		Double[] values = new Double[] { new Double(1), null, new Double(100), Double.NaN };
		SystemHelper.println(values);

		double[] results = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			results = NumberHelper.toDoubles(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(results);
		assertEquals(values.length, results.length);
	}

	@Test
	// 1000000 times: 827 mills.
	// 1000000 times: 843 mills.
	// 1000000 times: 827 mills.
	public void toStringz() {
		String result = null;

		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.toString(new Double(987.123));// 987.123
			System.out.println(result);

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("987.123", result);
		//
		result = NumberHelper.toString(987.123d);
		System.out.println(result);
		result = NumberHelper.toString(new Float(987.123));
		System.out.println(result);
		result = NumberHelper.toString(987.123f);
		System.out.println(result);
		result = NumberHelper.toString(new Date());
		System.out.println(result);
	}

	@Test
	// 1000000 times: 1034 mills
	// 1000000 times: 1039 mills
	// 1000000 times: 1011 mills
	//
	// 1000000 times: 1471 mills.
	// 1000000 times: 1554 mills.
	// 1000000 times: 1515 mills.
	public void add() {
		double score = 0.01d;
		double notSafeResult = 0d;

		double result = 0d;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.add(result, score);// 10000.0
			notSafeResult += score;

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("use NumberHelper.add(a,b) safe: " + result);// 10000.0
		// 有誤差
		System.out.println("use '+'  Not safe: " + notSafeResult);// 10000.000000171856

		assertEquals(0, Double.compare(10000d, result));
		//
		System.out.println(NumberHelper.add(10, 2));// 12.0
		System.out.println(NumberHelper.add(10, 3));// 13.0
	}

	@Test
	// 1000000 times: 77 mills.
	// 1000000 times: 88 mills.
	// 1000000 times: 81 mills.
	public void addByBigDecimal() {
		BigDecimal score = new BigDecimal(0.01d);

		BigDecimal result = new BigDecimal(0d);

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = result.add(score);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.doubleValue());// 10000.0
		assertEquals(10000.0, result.doubleValue());
	}

	@Test
	// 1000000 times: 553 mills.
	// 1000000 times: 498 mills.
	// 1000000 times: 523 mills.
	public void addByNan() {
		double result = 0d;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.add((Double) 100d, (Double) Double.NaN);// 10000.0
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("double + Nan: " + result);
		assertEquals(100d, result);
		//
		result = NumberHelper.add((Double) Double.NaN, (Double) 100d);
		System.out.println("Nan + double: " + result);
		result = NumberHelper.add((Double) Double.NaN, (Double) Double.NaN);
		System.out.println("NaN + Nan: " + result);

	}

	@Test
	// 1000000 times: 895 mills
	// 1000000 times: 917 mills
	// 1000000 times: 902 mills
	public void subtract() {
		double score = 0.01d;
		double notSafeResult = 0d;

		double result = 0d;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.subtract(result, score);// -10000.0
			notSafeResult -= score;

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("use NumberHelper.subtract(a,b) safe: " + result);// -10000.0
		// 有誤差
		System.out.println("use '+'  Not safe: " + notSafeResult);// -10000.000000171856

		assertEquals(-10000.0, result);
		//
		System.out.println(NumberHelper.subtract(10, 2));// 12.0
		System.out.println(NumberHelper.subtract(10, 3));// 13.0
	}

	@Test
	// 1000000 times: 895 mills
	// 1000000 times: 917 mills
	// 1000000 times: 902 mills
	public void multiply() {
		double score = 0.01d;
		double notSafeResult = 0d;

		double result = 1d;

		int count = 5;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.multiply(result, score);// 1.0E-10
			notSafeResult *= score;

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("use NumberHelper.multiply(a,b) safe: " + result);// 1.0E-10
		// 有誤差
		System.out.println("use '+'  Not safe: " + notSafeResult);// 0.0

		assertEquals(1.0E-10, result);
		//
		System.out.println(NumberHelper.multiply(10, 2));// 20.0
		System.out.println(NumberHelper.multiply(10, 3));// 30.0
	}

	@Test
	// 1000000 times: 895 mills
	// 1000000 times: 917 mills
	// 1000000 times: 902 mills
	public void divide() {
		double score = 0.01d;
		double notSafeResult = 1d;

		double result = 1d;

		int count = 5;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 累除 1/0.01
			result = NumberHelper.divide(result, score);// 1.0E10
			notSafeResult /= score;

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("use NumberHelper.divide(a,b) safe: " + result);// 1.0E10
		// 有誤差
		System.out.println("use '+'  Not safe: " + notSafeResult);// 0.0

		assertEquals(1.0E10, result);
		//
		System.out.println(NumberHelper.divide(10, 2));// 5.0
		System.out.println(NumberHelper.divide(10, 3));// 3.3333333333
														// //小數點以下10位
	}

	@Test
	public void divide2() {
		System.out.println("2000/10000.0 = " + (1 + 2000 / 10000.0));
		System.out.println("2000.0/10000.0 = " + (1 + 2000.0 / 10000.0));
	}

	@Test
	public void round() {
		double result = 0d;
		//
		// result = NumberHelper.round(300.54321);// 300.5432
		//
		// System.out.println(result);
		// assertEquals(300.5432, result);
		//
		// result = NumberHelper.round(new Double(300.543), 0);// 301.0
		// System.out.println(result);
		//
		System.out.println(NumberHelper.round(10));// 10.0
		System.out.println(NumberHelper.round(10.5, 0));// 11.0
	}

	@Test
	// 1000000 times: 776 mills.
	// 1000000 times: 771 mills.
	// 1000000 times: 739 mills.
	public void up() {
		double result = 0d;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.up(300.54321);// 300.5432
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(300.5433, result);
		//
		result = NumberHelper.up(new Double(300.543), 0);// 301.0
		System.out.println(result);
		//
		System.out.println(NumberHelper.up(10));// 10.0
		System.out.println(NumberHelper.up(10.5, 0));// 11.0
	}

	@Test
	// 1000000 times: 776 mills.
	// 1000000 times: 771 mills.
	// 1000000 times: 739 mills.
	public void down() {
		double result = 0d;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.down(300.54321);// 300.5432
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);

		assertEquals(300.5432, result);
		//
		result = NumberHelper.down(new Double(300.543), 0);// 301.0
		System.out.println(result);
		//
		System.out.println(NumberHelper.down(10));// 10.0
		System.out.println(NumberHelper.down(10.5, 0));// 10.0
	}

	@Test
	// 1000000 times: 191 mills.
	// 1000000 times: 187 mills.
	// 1000000 times: 213 mills.
	public void isNumeric() {
		boolean result = false;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.isNumeric("-300");// true
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(true, result);
		//
		System.out.println("isNumeric2: " + NumberHelper.___isNumeric2("-300"));
		System.out.println("isNumeric3: " + NumberHelper.___isNumeric3("-300"));
		System.out.println("isNumeric4: " + NumberHelper.___isNumeric4("-300"));
	}

	@Test
	// 1000000 times: 276 mills.
	// 1000000 times: 285 mills.
	// 1000000 times: 292 mills.
	// verified
	public void extractNumeric() {
		String result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.extractNumeric("新台幣:3,300元");// 3300
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("3300", result);
	}

	@Test
	// int
	// 1000000 times: 111 mills.
	// 1000000 times: 108 mills.
	// 1000000 times: 101 mills.
	// verified
	public void randomWin() {
		boolean result = false;
		int winTimes = 0;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomWin(7000, 10000);// 7000/10000=70%機率
			if (result) {
				winTimes += 1;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("winTimes: " + winTimes);
		//
		winTimes = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomWin(7000, 30000);// 7000/30000=23.33%機率
			if (result) {
				winTimes += 1;
			}
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("winTimes: " + winTimes);
		//
		winTimes = 0;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomWin(1000);// 1000/10000=10%機率
			if (result) {
				winTimes += 1;
			}
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("winTimes: " + winTimes);
	}

	@Test
	// int
	// 1000000 times: 111 mills.
	// 1000000 times: 108 mills.
	// 1000000 times: 101 mills.
	// verified
	public void randomIntBySeed() {
		int result = 0;
		//
		int count = 10;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomInt(1, 10);
			System.out.println(result);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void winByOne() {
		boolean result = false;
		int winTimes = 0;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomWin(0.7);// 70%機率
			if (result) {
				winTimes += 1;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("winTimes: " + winTimes);

	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomBoolean() {
		boolean result = false;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomBoolean();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// true,false
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomByte() {
		byte result = 0;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.randomByte();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// -119,24
		//
		result = NumberHelper.randomByte((byte) 100);
		System.out.println(result);// 82
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomShort() {
		short result = 0;
		result = NumberHelper.randomShort();

		System.out.println(result);// 17359,-17582
		//
		result = NumberHelper.randomShort((short) 100);
		System.out.println(result);// 80
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomInt() {
		int result = 0;
		result = NumberHelper.randomInt(0, 100);
		System.out.println(result);
		assertTrue(result >= 0);
		//
		result = NumberHelper.randomInt(100);
		System.out.println(result);
		//
		result = NumberHelper.randomInt(1);
		System.out.println(result);// 0
		//
		result = NumberHelper.randomInt(-10, -1);
		System.out.println(result);// -8
		//
		result = NumberHelper.randomInt();
		System.out.println(result);// 248922116,-733928975
		//
		result = NumberHelper.randomInt(5, 5);
		System.out.println(result);
		//
		result = NumberHelper.randomInt(-2, -1);
		System.out.println(result);
		//
		result = NumberHelper.randomInt(0, 1);
		System.out.println(result);
		//
		result = NumberHelper.randomInt(1, 5);
		System.out.println(result);
		//
		for (int i = 0; i < 10; i++) {
			result = NumberHelper.randomInt(1, 2);
			System.out.println(result);
		}
	}

	@Test
	// 1000000 times: 175 mills.
	// 1000000 times: 169 mills.
	// 1000000 times: 163 mills.
	public void randomUniqueInt() {
		int[] numbers = null;
		//
		for (int i = 0; i < 10; i++) {
			numbers = NumberHelper.randomUniqueInt(0, 3, 2);
			SystemHelper.println(numbers);
			assertEquals(2, numbers.length);
		}
	}

	@Test
	// 1000000 times: 119 mills.
	// 1000000 times: 123 mills.
	// 1000000 times: 119 mills.
	//
	// verified
	public void randomLong() {
		long result = 0L;
		result = NumberHelper.randomLong(0, 100);
		System.out.println(result);
		assertTrue(result >= 0);
		//
		result = NumberHelper.randomLong(2);
		System.out.println(result);
		//
		result = NumberHelper.randomLong();
		System.out.println(result);// 8662847137822939746,-6641512979589146752
		for (int i = 0; i < 10; i++) {
			result = NumberHelper.randomLong(1L, 5L);
			System.out.println(result);
		}
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomFloat() {
		float result = 0f;
		result = NumberHelper.randomFloat(0, 100);

		System.out.println(result);
		assertTrue(result >= 0);
		//
		result = NumberHelper.randomFloat(100);
		System.out.println(result);
		//
		result = NumberHelper.randomFloat();
		System.out.println(result);// 0.8514332,0.7591181
		//
		result = NumberHelper.randomFloat(1f, 5f);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomDouble() {
		double result = 0d;
		result = NumberHelper.randomDouble(0, 100);

		System.out.println(result);
		assertTrue(result >= 0);
		//
		result = NumberHelper.randomDouble(100);
		System.out.println(result);
		//
		result = NumberHelper.randomDouble();
		System.out.println(result);// 0.6000276081360958
		//
		//
		result = NumberHelper.randomDouble(1, 5);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 258 mills.
	// 1000000 times: 262 mills.
	// 1000000 times: 262 mills.
	//
	// verified
	public void secureRandomInt() {
		int result = 0;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = NumberHelper.secureRandomInt(0, 100);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result >= 0);
		//
		result = NumberHelper.secureRandomInt(100);
		System.out.println(result);
		//
		result = NumberHelper.secureRandomInt();
		System.out.println(result);// -1667663275,1879823349
		//
		byte[] bytes = SecurityHelper.md("" + result);
		String encodeByHex = EncodingHelper.encodeHex(bytes);// e19db4f307a5ae5fb82260e3ff043e0e
		System.out.println("encodeByHex: " + encodeByHex);
		//

		byte[] randomBytes = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(randomBytes);
		System.out.println(secureRandom.nextInt());
		System.out.println(secureRandom.nextInt());
	}

	@Test
	public void random20_50() {
		// 20=<x<=50
		// 產生10個20-50之間的亂數
		int[] box = new int[10];
		for (int i = 0; i < 10; i++) {
			int n = (int) (Math.random() * 30 + 20);
			box[i] = n;
		}
		for (int t : box)
			System.out.print(t + ",");
		//
	}

	// //error
	// //#fix NumberHelper.nextLong
	// public void testRandomLong()
	// {
	// long x = 1234567L;
	// long y = 23456789L;
	// Random r = new Random();
	// long number = x + ((long) r.nextDouble() * (y - x));
	// System.out.println(number);
	// }
	@Test
	public void toHexString() {
		System.out.println(Integer.toHexString(100));// 64
	}

	// --------------------------------------------------------
	@Test
	// 依權重比,隨機取寶物
	public void weight() {
		Treasure t1 = new Treasure();
		t1.setId("a");
		t1.setName("荔枝");
		t1.setWeight(100);
		//
		Treasure t2 = new Treasure();
		t2.setId("b");
		t2.setName("摃龜");
		t2.setWeight(100);
		//
		Treasure t3 = new Treasure();
		t3.setId("c");
		t3.setName("BAR");
		t3.setWeight(1);
		//
		Treasure t4 = new Treasure();
		t4.setId("d");
		t4.setName("777");
		t4.setWeight(1);
		//
		Treasure t5 = new Treasure();
		t5.setId("e");
		t5.setName("銅鐘");
		t5.setWeight(50);
		//
		List<Treasure> list = new LinkedList<Treasure>();
		list.add(t1);
		list.add(t2);
		list.add(t3);
		list.add(t4);
		list.add(t5);

		Treasure treasure = null;
		int bingo = 0;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			treasure = randomTreasure(list);
			// System.out.println(treasure);
			if (treasure.getWeight() <= 1) {
				bingo += 1;
				System.out.println("中大獎: " + treasure.getName() + " 總計次數: " + bingo);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	// 1.取所有權重加總
	private int calcTreasureWeight(List<Treasure> list) {

		int totalWeight = 0;
		for (Treasure treasure : list) {
			totalWeight += treasure.getWeight();
		}
		return totalWeight;
	}

	// 2.依權重隨機取
	private Treasure randomTreasure(List<Treasure> list) {
		Treasure treasure = null;
		// 1.取所有權重加總
		int totalWeight = calcTreasureWeight(list);
		int random = NumberHelper.randomInt(0, totalWeight);
		int accumulateWeight = 0;

		for (Treasure entry : list) {
			int lowLimit = accumulateWeight;
			int highLimit = accumulateWeight + entry.getWeight();
			if (random >= lowLimit && random < highLimit) {
				treasure = entry;
			}
			accumulateWeight = highLimit;
		}
		if (treasure != null) {
			treasure = (Treasure) treasure.clone();
		}
		return treasure;
	}

	// 寶物權重
	public static class Treasure implements Cloneable {

		private String id;

		private String name;

		// 權重
		private Integer weight;

		public Treasure() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getWeight() {
			return weight;
		}

		public void setWeight(Integer weight) {
			this.weight = weight;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}

		public Object clone() {
			Treasure copy = null;
			try {
				copy = (Treasure) super.clone();
			} catch (CloneNotSupportedException ex) {
				// throw new InternalError();
				ex.printStackTrace();
			}
			return copy;
		}
	}

	@Test
	// 1000000 times: 66 mills.
	// 1000000 times: 67 mills.
	// 1000000 times: 73 mills.
	//
	// verified
	public void safeGet() {
		Integer value = null;
		//
		Integer result = null;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = NumberHelper.safeGet(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	// 1000000 times: 66 mills.
	// 1000000 times: 67 mills.
	// 1000000 times: 73 mills.
	//
	// verified
	public void isAddOverflowByInteger() {
		boolean result = false;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = NumberHelper.isAddOverflow(0, Integer.MAX_VALUE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isAddOverflow(1, Integer.MAX_VALUE);
		System.out.println(result);
		assertTrue(result);
		//
		result = NumberHelper.isAddOverflow(1, Integer.MAX_VALUE - 1);
		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isAddOverflow(2, Integer.MAX_VALUE - 1);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// 1000000 times: 66 mills.
	// 1000000 times: 67 mills.
	// 1000000 times: 73 mills.
	//
	// verified
	public void isMultiplyOverflowByInteger() {
		boolean result = false;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = NumberHelper.isMultiplyOverflow(0, Integer.MAX_VALUE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isMultiplyOverflow(2, Integer.MAX_VALUE);
		System.out.println(result);
		assertTrue(result);
		//
		result = NumberHelper.isMultiplyOverflow(1, Integer.MAX_VALUE - 1);
		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isMultiplyOverflow(2, Integer.MAX_VALUE - 1);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// 1000000 times: 66 mills.
	// 1000000 times: 67 mills.
	// 1000000 times: 73 mills.
	//
	// verified
	public void isAddOverflowByLong() {
		boolean result = false;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = NumberHelper.isAddOverflow(0L, Long.MAX_VALUE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isAddOverflow(1L, Long.MAX_VALUE);
		System.out.println(result);
		assertTrue(result);
		//
		result = NumberHelper.isAddOverflow(1L, Long.MAX_VALUE - 1);
		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isAddOverflow(2L, Long.MAX_VALUE - 1);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// 1000000 times: 66 mills.
	// 1000000 times: 67 mills.
	// 1000000 times: 73 mills.
	//
	// verified
	public void isMultiplyOverflowByLong() {
		boolean result = false;
		int count = 1000000;
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = NumberHelper.isMultiplyOverflow(0L, Long.MAX_VALUE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isMultiplyOverflow(2L, Long.MAX_VALUE);
		System.out.println(result);
		assertTrue(result);
		//
		result = NumberHelper.isMultiplyOverflow(1L, Long.MAX_VALUE - 1);
		System.out.println(result);
		assertFalse(result);
		//
		result = NumberHelper.isMultiplyOverflow(2L, Long.MAX_VALUE - 1);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	public void math() {
		System.out.println(Math.ceil(100 / 15)); // 6
		System.out.println(Math.ceil((float) 100 / 15)); // 7
		System.out.println(Math.floor(100 / 15)); // 6
		BigDecimal a = new BigDecimal(100);
		BigDecimal b = new BigDecimal(15);
		System.out.println(a.divide(b, BigDecimal.ROUND_UP)); // 7
		//

		System.out.println((int) Math.ceil(23993 / 1800000)); // 6
		System.out.println((int) Math.ceil((float) 23993 / 1800000)); // 7
		//
		System.out.println((float) Math.ceil(16.39975));
		System.out.println((float) Math.ceil(16.1));
		System.out.println((float) Math.ceil(16.9));
		System.out.println((long) 50.0 * 9.4f);
		System.out.println((long) 50.0 * 9.4d);
		//
		int aaa = 1;
		Float bbb = 0.5f;

		System.out.println(aaa * bbb);
		float ccc = aaa * bbb;
		System.out.println(ccc);
		//
		System.out.println(Math.ceil(29 / 10));// 2.0
		System.out.println((int) Math.ceil(29 / 10));// 2
		System.out.println(Math.ceil((double) 29 / 10));// 3.0
		System.out.println((int) Math.ceil((double) 29 / 10));// 3
	}

	@Test
	public void ratio() {
		System.out.println(NumberHelper.ratio(20000));// 2.0
		System.out.println(NumberHelper.ratio(10000));// 1.0
		System.out.println(NumberHelper.ratio(5000));// 0.5
		System.out.println(NumberHelper.ratio(10));// 0.0010
		//
		System.out.println(100 / 21);// 4
		System.out.println(100 / 521);// 0
	}

	@Test
	public void mod() {
		int CONNECTED = 0x00000001;
		int DISCONNECTED = 0x00000002;
		int REFUSED = 0x00000004;

		System.out.println(CONNECTED);
		System.out.println(DISCONNECTED);
		System.out.println(REFUSED);
		//
		System.out.println(0x00000080);// 128
		System.out.println(0x00000100);// 256
		System.out.println(0x00000200);// 512
		//
		// int mod = CONNECTED | DISCONNECTED | REFUSED; //7
		int mod = CONNECTED | DISCONNECTED;// 3
		System.out.println("mod: " + mod);
		System.out.println((mod & CONNECTED) != 0);
		System.out.println((mod & DISCONNECTED) != 0);
		System.out.println((mod & REFUSED) != 0);

	}
}
