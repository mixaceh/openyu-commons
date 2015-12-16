package org.openyu.commons.lang;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class ArrayHelper.
 *
 * 1.array to list
 *
 * 2.array to set
 */
public final class ArrayHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ArrayHelper.class);

	/** The Constant EMPTY_BYTE. */
	public static final byte[] EMPTY_BYTE = new byte[0];

	/** The Constant EMPTY_BYTE_OBJECT. */
	public static final Byte[] EMPTY_BYTE_OBJECT = new Byte[0];

	/** The Constant EMPTY_SHORT. */
	public static final short[] EMPTY_SHORT = new short[0];

	/** The Constant EMPTY_SHORT_OBJECT. */
	public static final Short[] EMPTY_SHORT_OBJECT = new Short[0];

	/** The Constant EMPTY_INT. */
	public static final int[] EMPTY_INT = new int[0];

	/** The Constant EMPTY_INTEGER_OBJECT. */
	public static final Integer[] EMPTY_INTEGER_OBJECT = new Integer[0];

	/** The Constant EMPTY_LONG. */
	public static final long[] EMPTY_LONG = new long[0];

	/** The Constant EMPTY_LONG_OBJECT. */
	public static final Long[] EMPTY_LONG_OBJECT = new Long[0];

	//
	/** The Constant EMPTY_DOUBLE. */
	public static final double[] EMPTY_DOUBLE = new double[0];

	/** The Constant EMPTY_DOUBLE_OBJECT. */
	public static final Double[] EMPTY_DOUBLE_OBJECT = new Double[0];

	/** The Constant EMPTY_FLOAT. */
	public static final float[] EMPTY_FLOAT = new float[0];

	/** The Constant EMPTY_FLOAT_OBJECT. */
	public static final Float[] EMPTY_FLOAT_OBJECT = new Float[0];

	//
	/** The Constant EMPTY_CHAR. */
	public static final char[] EMPTY_CHAR = new char[0];

	/** The Constant EMPTY_CHARACTER_OBJECT. */
	public static final Character[] EMPTY_CHARACTER_OBJECT = new Character[0];

	/** The Constant EMPTY_BOOLEAN. */
	public static final boolean[] EMPTY_BOOLEAN = new boolean[0];

	/** The Constant EMPTY_BOOLEAN_OBJECT. */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT = new Boolean[0];

	//
	/** The Constant EMPTY_OBJECT. */
	public static final Object[] EMPTY_OBJECT = new Object[0];

	/** The Constant EMPTY_STRING. */
	public static final String[] EMPTY_STRING = new String[0];

	/** The Constant EMPTY_CLASS. */
	public static final Class<?>[] EMPTY_CLASS = new Class[0];

	private ArrayHelper() {
		throw new HelperException(
				new StringBuilder().append(ArrayHelper.class.getSimpleName()).append(" can not construct").toString());
	}
	// --------------------------------------------------

	/**
	 * Copy of.
	 *
	 * @param <T>
	 *            the generic type
	 * @param <U>
	 *            the generic type
	 * @param original
	 *            the original
	 * @param newLength
	 *            the new length
	 * @param newType
	 *            the new type
	 * @return the t[]
	 */
	// object[] -> string[]
	// String[] stringArray = Arrays.copyOf(objectArray, objectArray.length,
	// String[].class);
	// Arrays.asList(Object_Array).toArray(new String[Object_Array.length]);
	public static <T, U> T[] copyOf(final U[] original, final int newLength, final Class<? extends T[]> newType) {
		return Arrays.copyOf(original, newLength, newType);
	}

	/**
	 * 合併陣列,元素不重複,有唯一性.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param newType
	 *            the new type
	 * @return the t[]
	 */
	public static <T> T[] addUnique(final Object[] x, final Object[] y, final Class<? extends T[]> newType) {
		//
		int xlength = 0;
		if (x != null) {
			xlength = x.length;
		}
		//
		List<Object> buff = new LinkedList<Object>();
		if (y != null) {
			for (int i = 0; i < y.length; i++) {
				// by value
				boolean contains = contains(x, y[i]);
				// not equals
				if (!contains) {
					buff.add(y[i]);
				}
			}
		}
		//
		Object[] result = new Object[xlength + buff.size()];
		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				result[i] = x[i];
			}
		}
		for (int i = 0; i < buff.size(); i++) {
			result[xlength + i] = buff.get(i);
		}
		//
		return copyOf(result, result.length, newType);
	}

	/**
	 * 合併陣列,元素可重複.
	 *
	 * @param <T>
	 *            the generic type
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param newType
	 *            the new type
	 * @return the t[]
	 */
	public static <T> T[] add(final Object[] x, final Object[] y, final Class<? extends T[]> newType) {

		Object[] result = null;
		//
		int xlength = 0;
		if (x != null) {
			xlength = x.length;
		}
		//
		int ylength = 0;
		if (y != null) {
			ylength = y.length;
		}
		result = new Object[xlength + ylength];
		//
		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				result[i] = x[i];
			}
		}
		//
		if (y != null) {
			for (int i = 0; i < y.length; i++) {
				result[xlength + i] = y[i];
			}
		}
		return copyOf(result, result.length, newType);
	}

	/**
	 * 合併陣列,元素可重複.
	 * 
	 * 類似 UnsafeHelper.putByteArray(byte[] data, int offset, byte[] value)
	 * 
	 * 但無法指定啟始位置
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the byte[]
	 */
	public static byte[] add(final byte[] x, final byte[] y) {
		byte[] result = new byte[x.length + y.length];
		// 1. round: 0.01
		// for (int i = 0; x != null && i < x.length; i++) {
		// result[i] = x[i];
		// }
		// for (int i = 0; y != null && i < y.length; i++) {
		// result[x.length + i] = y[i];
		// }

		// 1.1 round: 0.01
		// for (int i = 0; i < result.length; ++i)
		// {
		// result[i] = i < x.length ? x[i] : y[i - x.length];
		// }

		// 2.System.arraycopy round: 0.01
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);

		// 3.ByteArrayOutputStream round: 0.08
		// ByteArrayOutputStream out = null;
		// try {
		// out = new ByteArrayOutputStream();
		// out.write(x);
		// out.write(y);
		// //
		// result=out.toByteArray();
		// } catch (Exception ex) {
		// } finally {
		// IoHelper.close(out);
		// }

		// 4.ByteBuffer round: 0.05
		// ByteBuffer buff = ByteBuffer.allocate(x.length + y.length);
		// buff.put(x);
		// buff.put(y);
		// buff.flip();
		// //
		// result = new byte[buff.limit()];
		// buff.get(result);

		// 5.ArrayUtils.addAll round: 0.01 (System.arraycopy)
		// result = ArrayUtils.addAll(x, y);
		//
		return result;
	}

	/**
	 * byte[][] 加入一個 byte[].
	 *
	 * @param y
	 *            the y
	 * @param buffs
	 *            the buffs
	 * @return the byte[][]
	 */
	public static byte[][] add(final byte[] y, final byte[]... buffs) {
		byte[][] result = new byte[buffs.length + 1][];
		//
		for (int i = 0; i < buffs.length; i++) {
			result[i] = buffs[i];
		}
		result[buffs.length] = y;
		return result;
	}

	/**
	 * 比較位址跟值, false 不存在, true 存在.
	 *
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static <T> boolean contains(final T[] values, final T value) {
		boolean result = false;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (ObjectHelper.equals(values[i], value)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 判斷是否為(多維1,2...n)array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return true, if is array
	 */
	public static <T> boolean isArray(final T value) {
		boolean result = false;
		if (value != null) {
			Class<?> clazz = value.getClass().getComponentType();
			if (clazz != null) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Gets the dimension.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return the dimension
	 */
	public static <T> int getDimension(final T value) {
		int dimension = 0;
		if (value != null) {
			StringBuffer buff = new StringBuffer();
			buff.append(value.getClass().getName());
			// 判斷是否一維/多維陣列
			while (buff.charAt(dimension) == '[') {
				dimension += 1;
			}
		}
		return dimension;
	}

	/**
	 * Gets the first element.
	 *
	 * @param values
	 *            the values
	 * @return the first element, [0]=class, [1]=value
	 */
	public static Object[] getFirstEntry(final Object[] values) {
		Object[] objects = new Object[2];
		if (values != null) {
			for (Object entry : values) {
				if (isArray(entry)) {
					objects = getFirstEntry((Object[]) entry);
				} else if (entry != null) {
					objects[0] = entry.getClass();
					objects[1] = entry;
				}
				//
				if (objects[1] != null) {
					break;
				}
			}
		}
		return objects;
	}

	/**
	 * 取陣列元素.
	 *
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @param index
	 *            the index
	 * @return the t
	 */
	public static <T> T get(final T[] values, final int index) {
		T result = null;
		try {
			if (notEmpty(values)) {
				result = values[index];
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 設定陣列元素.
	 *
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static <T> boolean set(final T[] values, final int index, final T value) {
		boolean result = false;
		try {
			if (notEmpty(values)) {
				values[index] = value;
				result = true;
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 陣列是否為空值.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return true, if is empty
	 */
	public static <T> boolean isEmpty(final T[] value) {
		return (value == null || value.length == 0);
	}

	/**
	 * 陣列是否不為空值.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return true, if is not empty
	 */
	public static <T> boolean notEmpty(final T[] value) {
		return (value != null && value.length > 0);
	}
}
