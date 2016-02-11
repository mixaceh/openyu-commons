package org.openyu.commons.lang;

import java.lang.reflect.InvocationHandler;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObjectHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ObjectHelper.class);

	public static final Object EMPTY_OBJECT = new Object();

	private ObjectHelper() {
		throw new HelperException(
				new StringBuilder().append(ObjectHelper.class.getName()).append(" can not construct").toString());
	}

	public static Object createObject(InvocationHandler handler) {
		return ClassHelper.newProxyInstance(new Class[] { Object.class }, handler);
	}

	public static boolean contains(Object[] array, Object value) {
		return ArrayHelper.contains(array, value);
	}

	public static Object toObject(Object value) {
		return toObject(value, null);
	}

	public static Object toObject(Object value, Object defaultValue) {
		return (value != null) ? value : defaultValue;
	}

	/**
	 * <p>
	 * Compares two objects for equality, where either one or both objects may
	 * be <code>null</code>.
	 * </p>
	 *
	 * <pre>
	 * ObjectHelper.equals(null, null)                  = true
	 * ObjectHelper.equals(null, "")                    = false
	 * ObjectHelper.equals("", null)                    = false
	 * ObjectHelper.equals("", "")                      = true
	 * ObjectHelper.equals(Boolean.TRUE, null)          = false
	 * ObjectHelper.equals(Boolean.TRUE, "true")        = false
	 * ObjectHelper.equals(Boolean.TRUE, Boolean.TRUE)  = true
	 * ObjectHelper.equals(Boolean.TRUE, Boolean.FALSE) = false
	 * </pre>
	 *
	 * @param x
	 *            Object the first object, may be <code>null</code>
	 * @param y
	 *            Object the second object, may be <code>null</code>
	 * @return <code>true</code> if the values of both objects are the same
	 */
	public static boolean equals(Object x, Object y) {
		if (x == y) {
			return true;
		}
		if ((x == null) || (y == null)) {
			return false;
		}
		return x.equals(y);
	}

	public static boolean equals(byte[] x, byte[] y) {
		if (x == y) {
			return true;
		}
		if ((x == null) || (y == null)) {
			return false;
		}
		//
		if (x.length == y.length) {
			for (int i = 0; i < x.length; i++) {
				if (!(x[i] == y[i]))
					return false;
			}
			return true;
		}
		return false;
	}

	// 20111206
	public static boolean equals(Object[] x, Object[] y) {
		if (x == y) {
			return true;
		}
		if ((x == null) || (y == null)) {
			return false;
		}
		//
		if (x.length == y.length) {
			for (int i = 0; i < x.length; i++) {
				if (!x[i].equals(y[i]))
					return false;
			}
			return true;
		}
		return false;
	}

	public static String identityToString(Object object) {
		if (object == null) {
			return null;
		}
		return appendIdentityToString(null, object).toString();
	}

	public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
		if (object == null) {
			return null;
		}
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		return buffer.append(object.getClass().getName()).append('@')
				.append(Integer.toHexString(System.identityHashCode(object)));
	}

	// ------------------------------------------------------

	public static <T> String toString(T value) {
		return toString(value, StringHelper.COMMA_SPACE);
	}

	public static <T> String toString(T value, String splitter) {
		StringBuilder result = new StringBuilder();
		if (ArrayHelper.isArray(value))// 基本型別array
		{
			// Class<?> clazz = value.getClass().getComponentType();
			StringBuilder className = new StringBuilder(value.getClass().getName());
			char type = className.charAt(1);
			if (type != '[' && type != 'L') {
				switch (type) {
				case 'Z':
					boolean[] booleans = (boolean[]) value;
					for (int i = 0; i < booleans.length; i++) {
						boolean element = booleans[i];
						result.append(element);
						if (i < booleans.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'C':
					char[] chars = (char[]) value;
					for (int i = 0; i < chars.length; i++) {
						char element = chars[i];
						result.append(element);
						if (i < chars.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'B':
					byte[] bytes = (byte[]) value;
					for (int i = 0; i < bytes.length; i++) {
						byte element = bytes[i];
						result.append(element);
						if (i < bytes.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'S':
					short[] shorts = (short[]) value;
					for (int i = 0; i < shorts.length; i++) {
						short element = shorts[i];
						result.append(element);
						if (i < shorts.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'I':
					int[] ints = (int[]) value;
					for (int i = 0; i < ints.length; i++) {
						int element = ints[i];
						result.append(element);
						if (i < ints.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'J':
					long[] longs = (long[]) value;
					for (int i = 0; i < longs.length; i++) {
						long element = longs[i];
						result.append(element);
						if (i < longs.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'F':
					float[] floats = (float[]) value;
					for (int i = 0; i < floats.length; i++) {
						float element = floats[i];
						result.append(element);
						if (i < floats.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'D':
					double[] doubles = (double[]) value;
					for (int i = 0; i < doubles.length; i++) {
						double element = doubles[i];
						result.append(element);
						if (i < doubles.length - 1) {
							result.append(splitter);
						}
					}
					break;
				}
			}
		} else {
			result.append(value);
		}
		// return (value != null ? value.toString() : null);
		return result.toString();
	}

	public static <T> String toString(T[] values) {
		return toString(values, StringHelper.COMMA_SPACE);
	}

	public static <T> String toString(T[] values, String splitter) {
		StringBuilder result = new StringBuilder();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				T element = values[i];
				result.append(toString(element));
				//
				if (i < values.length - 1) {
					result.append(splitter);
				}
			}
		}
		return result.toString();
	}

	// ------------------------------------------------------

	// public static String toString(byte[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(byte[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// byte element = objects[i];
	// sb.append(element);
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }
	//
	// public static String toString(short[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(short[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// short element = objects[i];
	// sb.append(element);
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }
	//
	// public static String toString(int[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(int[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// int element = objects[i];
	// sb.append(element);
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }
	//
	// public static String toString(long[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(long[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// long element = objects[i];
	// sb.append(element);
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }
	//
	// public static String toString(float[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(float[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// float element = objects[i];
	// sb.append(element);
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }

	// public static String toString(Object[] objects)
	// {
	// return toString(objects, StringHelper.COMMA_SPACE);
	// }
	//
	// public static String toString(Object[] objects, String regex)
	// {
	// StringBuffer sb = new StringBuffer();
	// if (objects != null)
	// {
	// for (int i = 0; i < objects.length; i++)
	// {
	// Object element = objects[i];
	// sb.append(toString(element, "null"));
	// if (i < objects.length - 1)
	// {
	// sb.append(StringHelper.COMMA_SPACE);
	// }
	// }
	// }
	// return sb.toString();
	// }

	public static Object maskNull(Object object) {
		return (object == null) ? EMPTY_OBJECT : object;
	}

	public static Object unmaskNull(Object object) {
		return (object == EMPTY_OBJECT) ? null : object;
	}

	public static boolean isArray(Object obj) {
		return ((obj != null) && (obj.getClass().isArray()));
	}

	public static boolean notArray(Object obj) {
		return ((obj == null) || (!obj.getClass().isArray()));
	}

	public static boolean isEmpty(Object[] array) {
		return ((array == null) || (array.length == 0));
	}

	public static boolean notEmpty(Object[] array) {
		return ((array != null) && (array.length > 0));
	}

}
