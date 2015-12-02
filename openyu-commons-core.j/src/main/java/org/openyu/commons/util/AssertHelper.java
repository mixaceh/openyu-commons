package org.openyu.commons.util;

import java.util.Collection;
import java.util.Map;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.lang.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 檢核
 */
public class AssertHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(AssertHelper.class);

	/**
	 * Instantiates a new AssertHelper.
	 */
	private AssertHelper() {
		if (InstanceHolder.INSTANCE != null) {
			throw new UnsupportedOperationException("Can not construct.");
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		private static final AssertHelper INSTANCE = new AssertHelper();
	}

	/**
	 * Gets the single instance of AssertHelper.
	 *
	 * @return single instance of AssertHelper
	 */
	public static AssertHelper getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public static void isTrue(boolean expression, String message) {
		if (!(expression))
			throw new IllegalArgumentException(message);
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null)
			throw new IllegalArgumentException(message);
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void notEmpty(String text, String message) {
		if (!(StringHelper.notEmpty(text)))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(String text) {
		notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void notBlank(String text, String message) {
		if (!(StringHelper.notBlank(text)))
			throw new IllegalArgumentException(message);
	}

	public static void notBlank(String text) {
		notBlank(text,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void doesNotContain(String textToSearch, String substring, String message) {
		if ((!(StringHelper.notEmpty(textToSearch))) || (!(StringHelper.notEmpty(substring)))
				|| (!(textToSearch.contains(substring))))
			return;
		throw new IllegalArgumentException(message);
	}

	public static void doesNotContain(String textToSearch, String substring) {
		doesNotContain(textToSearch, substring,
				new StringBuilder().append("[Assertion failed] - this String argument must not contain the substring [")
						.append(substring).append("]").toString());
	}

	public static void notEmpty(Object[] array, String message) {
		if (ObjectHelper.isEmpty(array))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Object[] array) {
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null)
			for (Object element : array)
				if (element == null)
					throw new IllegalArgumentException(message);
	}

	public static void noNullElements(Object[] array) {
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void notEmpty(Collection<?> collection, String message) {
		if (CollectionHelper.isEmpty(collection))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Collection<?> collection) {
		notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static void notEmpty(Map<?, ?> map, String message) {
		if (CollectionHelper.isEmpty(map))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Map<?, ?> map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	public static void isInstanceOf(Class<?> clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (type.isInstance(obj)) {
			return;
		}
		throw new IllegalArgumentException(new StringBuilder()
				.append((StringHelper.notEmpty(message)) ? new StringBuilder().append(message).append(" ").toString()
						: "")
				.append("Object of class [").append((obj != null) ? obj.getClass().getName() : "null")
				.append("] must be an instance of ").append(type).toString());
	}

	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if ((subType == null) || (!(superType.isAssignableFrom(subType))))
			throw new IllegalArgumentException(new StringBuilder().append(message).append(subType)
					.append(" is not assignable to ").append(superType).toString());
	}

	/**
	 * state = isTrue
	 * 
	 * @param expression
	 * @param message
	 */
	public static void state(boolean expression, String message) {
		if (!(expression))
			throw new IllegalStateException(message);
	}

	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	public static void unsupported(String message) {
		throw new UnsupportedOperationException(message);
	}

	public static void unsupported() {
		unsupported("[Assertion failed] - is unsupported");
	}

	public static void isBetween(int value, int min, int max, String message) {
		if (!(value >= min && value <= max))
			throw new IllegalArgumentException(message);
	}
	
	public static void isBetween(long value, long min, long max, String message) {
		if (!(value >= min && value <= max))
			throw new IllegalArgumentException(message);
	}
}
