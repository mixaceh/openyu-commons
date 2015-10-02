package org.openyu.commons.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/*
 //char -> String
 char a='a';
 String.valueOf(a); -> "a"
 或
 Charcter.toString(a); ->"a"

 //String -> char
 String a="abc";
 a.charAt(0); -> 97
 */
/**
 * The Class CharHelper.
 */
public class CharHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(CharHelper.class);
	//
	// public static final char DEFAULT_VALUE = '\u0000';
	/** The Constant DEFAULT_VALUE. */
	public static final char DEFAULT_VALUE = 0;

	/**
	 * Instantiates a new helper.
	 */
	public CharHelper() {
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
		// private static final CharHelper INSTANCE = new CharHelper();
		private static CharHelper INSTANCE = new CharHelper();
	}

	/**
	 * Gets the single instance of CharHelper.
	 *
	 * @return single instance of CharHelper
	 */
	public synchronized static CharHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new CharHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	// -----------------------------------------------------------------------

	/**
	 * To char.
	 *
	 * @param value
	 *            the value
	 * @return the char
	 */
	public static char toChar(String value) {
		return toChar(value, 0);
	}

	/**
	 * To char.
	 *
	 * @param value
	 *            the value
	 * @param index
	 *            the index
	 * @return the char
	 */
	public static char toChar(String value, int index) {
		return toChar(value, index, DEFAULT_VALUE);
	}

	/**
	 * To char.
	 *
	 * @param value
	 *            the value
	 * @param index
	 *            the index
	 * @param defaultValue
	 *            the default value
	 * @return the char
	 */
	public static char toChar(String value, int index, char defaultValue) {
		char result = defaultValue;
		try {
			if (!StringHelper.isEmpty(value)) {
				result = value.charAt(index);
			}
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if successful
	 */
	public static boolean equals(int x, int y) {
		char value1 = (char) x;
		char value2 = (char) y;
		return equals(value1, value2);
	}

	/**
	 * Equals.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if successful
	 */
	public static boolean equals(char x, char y) {
		return x == y;
	}

	/**
	 * Equals ignore case.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if successful
	 */
	public static boolean equalsIgnoreCase(int x, int y) {
		char value1 = (char) x;
		char value2 = (char) y;
		return equalsIgnoreCase(value1, value2);
	}

	/**
	 * Equals ignore case.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if successful
	 */
	public static boolean equalsIgnoreCase(char x, char y) {
		String value1 = String.valueOf(x);
		String value2 = String.valueOf(y);
		//
		return StringHelper.equalsIgnoreCase(value1, value2);
	}

	/**
	 * Contains.
	 *
	 * @param values
	 *            the array
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean contains(char[] values, char value) {
		boolean result = false;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				result = equals(values[i], value);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Contains.
	 *
	 * @param values
	 *            the array
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean contains(int[] values, char value) {
		boolean result = false;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				char element = (char) values[i];
				result = equals(element, value);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Contains ignore case.
	 *
	 * @param values
	 *            the array
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean containsIgnoreCase(char[] values, char value) {
		boolean result = false;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				result = equalsIgnoreCase(values[i], value);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Contains ignore case.
	 *
	 * @param values
	 *            the array
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean containsIgnoreCase(int[] values, char value) {
		boolean result = false;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				char element = (char) values[i];
				result = equalsIgnoreCase(element, value);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Random char.
	 *
	 * @return the char
	 */
	public static char randomChar() {
		return StringHelper.randomAlphabet().charAt(0);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the char
	 */
	public static char safeGet(Character value) {
		return (value != null ? value : DEFAULT_VALUE);
	}

	/**
	 * To string.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toString(Character value) {
		String result = null;
		if (value != null) {
			result = toString((char) value);
		}
		return result;
	}

	/**
	 * To string.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toString(char value) {
		return String.valueOf(value);
	}
}
