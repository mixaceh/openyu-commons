package org.openyu.commons.lang;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * Boolean輔助類
 */
public final class BooleanHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BooleanHelper.class);

	/** The Constant DEFAULT_VALUE. */
	public static final boolean DEFAULT_VALUE = false;

	/** The Constant FALSE_STRING. */
	public static final String FALSE_STRING = "0";

	/** The Constant TRUE_STRING. */
	public static final String TRUE_STRING = "1";

	/** The Constant FALSE_INT. */
	public static final int FALSE_INT = 0;

	/** The Constant TRUE_INT. */
	public static final int TRUE_INT = 1;

	/**
	 * The Constant random.
	 *
	 * 每次執行,若seed都是一樣,則都會得到相同的結果.
	 */
	public static final Random RANDOM = new Random(System.nanoTime());

	private BooleanHelper() {
		throw new HelperException(
				new StringBuilder().append(BooleanHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * Creates the boolean.
	 *
	 * @param value
	 *            the value
	 * @return the boolean
	 */
	public static Boolean createBoolean(final boolean value) {
		if (value) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Creates the boolean.
	 *
	 * @param value
	 *            the value
	 * @return the boolean
	 */
	public static Boolean createBoolean(final String value) {
		if (value == null) {
			return DEFAULT_VALUE;
		} else {
			final boolean buff = toBoolean(value, DEFAULT_VALUE);
			if (buff) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}

	/**
	 * To boolean.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean toBoolean(final String value) {
		return toBoolean(value, DEFAULT_VALUE);
	}

	/**
	 * To boolean.
	 *
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @return true, if successful
	 */
	public static boolean toBoolean(final String value, final boolean defaultValue) {
		if (value == null || value.length() == 0) {
			return defaultValue;
		} else {
			if ("true".equalsIgnoreCase(value)) {
				return true;
			}
			if ("on".equalsIgnoreCase(value)) {
				return true;
			}
			if ("yes".equalsIgnoreCase(value)) {
				return true;
			}
			//
			if ("false".equalsIgnoreCase(value)) {
				return false;
			}
			if ("off".equalsIgnoreCase(value)) {
				return false;
			}
			if ("no".equalsIgnoreCase(value)) {
				return false;
			}
			//
			switch (value.charAt(0)) {
			case '1':
			case 't':
			case 'T':
			case 'y':
			case 'Y':
				return true;
			case '0':
			case 'f':
			case 'F':
			case 'n':
			case 'N':
				return false;
			default:
				return defaultValue;
			}
		}
	}

	/**
	 * Random boolean.
	 *
	 * @return
	 */
	public static boolean randomBoolean() {
		return RANDOM.nextBoolean();
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean safeGet(final Boolean value) {
		if (value == null) {
			return DEFAULT_VALUE;
		}
		return value.booleanValue();
	}

	/**
	 * To string.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toString(final Boolean value) {
		if (value == null) {
			return FALSE_STRING;
		}
		return toString(value.booleanValue());
	}

	/**
	 * To string.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toString(final boolean value) {
		if (value) {
			return TRUE_STRING;
		}
		return FALSE_STRING;
	}

	/**
	 * To int.
	 *
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int toInt(final Boolean value) {
		if (value == null) {
			return FALSE_INT;
		}
		return toInt(value.booleanValue());
	}

	/**
	 * To int.
	 *
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int toInt(final boolean value) {
		if (value) {
			return TRUE_INT;
		}
		return FALSE_INT;
	}

	//
	// /**
	// * Read resolve.
	// *
	// * 如果Singleton實現了Serializable介面, 那麼這個類的實例就可能被反序列化復原, 就有可能有多個單例類的實例.
	// *
	// * #fix return instance
	// *
	// * @return the object
	// * @throws ObjectStreamException the object stream exception
	// */
	// private Object readResolve() throws ObjectStreamException {
	// return getInstance();
	// }
}
