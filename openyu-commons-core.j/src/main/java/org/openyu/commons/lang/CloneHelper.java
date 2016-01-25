package org.openyu.commons.lang;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public final class CloneHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(CloneHelper.class);

	/**
	 * https://code.google.com/p/cloning/wiki/Usage
	 * 
	 * Cloner is thread safe.
	 */
	private static Cloner cloner = new Cloner();

	private CloneHelper() {
		throw new HelperException(
				new StringBuilder().append(CloneHelper.class.getName()).append(" can not construct").toString());

	}

	/**
	 * clone,已實現深度clone
	 * 
	 * 1.需實作Serializable,或Cloneable其中之一個,或是同時實作兩個
	 * 
	 * 2.若都無實作Serializable或Cloneable,則會傳回null
	 * 
	 * @param value
	 * @return
	 */
	public static <T> T ___clone(Object value) {
		T result = null;
		if (value != null) {
			result = deepClone(value);
		}
		return result;
	}

	/**
	 * 2015/01/25
	 * 
	 * 改為用 cloner
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(Object value) {
		T result = null;
		if (value != null) {
			// result = deepClone(value);
			result = (T) cloner.deepClone(value);
		}
		return result;
	}

	// 遞迴
	protected static <T> T deepClone(Object value) {
		Object object = null;
		if (value != null) {
			try {
				StringBuilder className = new StringBuilder(value.getClass().getName());
				// check if it's an array
				// if ('[' == className.charAt(0))

				// 1.array
				if (ArrayHelper.isArray(value)) {
					// handle 1 dimensional primitive arrays
					// 一維基本型別, [Ljava.lang.String;
					char type = className.charAt(1);
					if (type != '[' && type != 'L') {
						switch (type) {
						case 'Z':
							object = ((boolean[]) value).clone();
							break;
						case 'C':
							object = ((char[]) value).clone();
							break;
						case 'B':
							object = ((byte[]) value).clone();
							break;
						case 'S':
							object = ((short[]) value).clone();
							break;
						case 'I':
							object = ((int[]) value).clone();
							break;
						case 'J':
							object = ((long[]) value).clone();
							break;
						case 'F':
							object = ((float[]) value).clone();
							break;
						case 'D':
							object = ((double[]) value).clone();
							break;
						}
					}
					// 一/多維非基本型別, class [[Ljava.lang.String;
					else {
						// get the base type and the dimension count of the
						// array
						int dimension = ArrayHelper.getDimension(value);
						Class<?> baseClass = ClassHelper.getBaseClass(value);

						// instantiate the array but make all but the first
						// dimension 0.
						int[] dimensions = new int[dimension];
						dimensions[0] = Array.getLength(value);
						for (int i = 1; i < dimension; i += 1) {
							dimensions[i] = 0;
						}
						// 本身
						Object copy = Array.newInstance(baseClass, dimensions);

						// now fill in the next level down by recursion.
						for (int i = 0; i < dimensions[0]; i += 1) {
							Array.set(copy, i, deepClone(Array.get(value, i)));
						}
						object = copy;
					}
				}
				// handle cloneable collections
				// 先copy 集合本身,再copy element
				else if (value instanceof Collection) {
					Collection<?> collection = (Collection<?>) value;
					// Collection<T> copy = genericClone(collection); //慢
					// copy.clear();
					Collection<T> copy = ClassHelper.newInstance(value.getClass());// 快

					// 有些原本的list會無法建構,如:com.opensymphony.xwork2.util.XWorkList
					// 就用LinkedList,LinkedHashSet做為預設
					if (copy == null) {
						if (value instanceof List) {
							copy = new LinkedList<T>();
						} else if (value instanceof Set) {
							copy = new LinkedHashSet<T>();
						}
					}
					// clone all the values in the collection individually
					for (Object entry : collection) {
						copy.add((T) deepClone(entry));
					}
					object = copy;
				}
				// handle cloneable maps
				// 先copy 集合本身,再copy element, key跟value
				else if (value instanceof Map) {
					Map<?, ?> map = (Map<?, ?>) value;
					// Map<T, T> copy = genericClone(value);
					// copy.clear();
					Map<T, T> copy = ClassHelper.newInstance(value.getClass());// 快
					// now clone all the keys and values of the entries
					for (Map.Entry entry : map.entrySet()) {
						copy.put((T) deepClone(entry.getKey()), (T) deepClone(entry.getValue()));
					}
					object = copy;
				}
				//
				else {
					Object copy = genericClone(value);
					object = copy;
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return (T) object;
	}

	// 無遞迴, 泛化 clone
	protected static <T> T genericClone(Object value) {
		Object object = null;
		if (value != null) {
			// ------------------------------------------------------------
			// if this is a pure value and not an extending class, just don't
			// clone
			// it since it's most probably a thread monitor lock
			if (value.getClass().equals(Object.class)) {
				object = value;
			}
			// strings can't be cloned, but they are immutable, so skip over it
			else if (value.getClass().equals(String.class)) {
				object = value;
			}
			// stringbuffers can't be cloned, so just create a new one
			else if (value.getClass().equals(StringBuffer.class)) {
				object = new StringBuffer(value.toString());
			} else if (value.getClass().equals(StringBuilder.class)) {
				object = new StringBuilder(value.toString());
			}
			// exceptions can't be cloned, but they are simply indicative, so
			// skip
			// over it
			else if (value instanceof Throwable) {
				object = value;
			}
			// handle the reference counterparts of the primitives that are
			// not cloneable in the jdk, they are immutable
			else if (ClassHelper.isPrimitiveOrWrapper(value)) {
				object = value;
			}
			// BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short
			else if (value instanceof Number) {
				object = value;
			}
			// ------------------------------------------------------------
			else if (!(value instanceof Cloneable)) {
				if (value instanceof Serializable) {
					object = SerializationUtils.clone((Serializable) value);
				} else {
					object = null;
				}
			}
			// cloneable
			else {
				try {
					// Method method = value.getClass().getMethod("clone",
					// (Class[]) null);
					Method method = ClassHelper.getMethod(value.getClass(), "clone");
					method.setAccessible(true);
					// System.out.println(method);
					object = method.invoke(value, (Object[]) null);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return (T) object;
	}

}