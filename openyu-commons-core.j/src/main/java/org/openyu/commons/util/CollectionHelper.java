package org.openyu.commons.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(CollectionHelper.class);

	public static final Set EMPTY_SET = Collections.EMPTY_SET;

	public static final List EMPTY_LIST = Collections.EMPTY_LIST;

	public static final Map EMPTY_MAP = Collections.EMPTY_MAP;

	/**
	 * Instantiates a new CollectionHelper.
	 */
	private CollectionHelper() {
		if (InstanceHolder.INSTANCE != null) {
			throw new UnsupportedOperationException("Can not construct.");
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		private static final CollectionHelper INSTANCE = new CollectionHelper();
	}

	/**
	 * Gets the single instance of CollectionHelper.
	 *
	 * @return single instance of CollectionHelper
	 */
	public static CollectionHelper getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 轉成map
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(K[] keys, V[] values) {
		return toMap(keys, values, LinkedHashMap.class);
	}

	/**
	 * 轉成map
	 * 
	 * @param keys
	 * @param values
	 * @param clazz
	 * @return
	 */
	public static <K, V, T extends Map<K, V>> Map<K, V> toMap(K[] keys,
			V[] values, Class<T> clazz) {
		Map<K, V> result = ClassHelper.newInstance(clazz);
		//
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				result.put(keys[i], values[i]);
			}
		}
		return result;
	}

	/**
	 * 轉成concurrentMap
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> toConcurrentMap(K[] keys,
			V[] values) {
		ConcurrentMap<K, V> result = new ConcurrentHashMap<K, V>();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				V value = values[i];
				// null,不放入ConcurrentMap,會有null ex
				if (value != null) {
					result.put(keys[i], value);
				}

			}
		}
		return result;
	}

	/**
	 * 轉成list
	 * 
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E, T extends List<E>> List<E> toList(E[] values) {
		return toList(values, LinkedList.class);
	}

	/**
	 * 轉成list
	 * 
	 * @param values
	 * @param clazz
	 * @return
	 */
	public static <E, T extends List<E>> List<E> toList(E[] values,
			Class<T> clazz) {
		List<E> result = ClassHelper.newInstance(clazz);
		//
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				result.add(values[i]);
			}
		}
		return result;
	}

	/**
	 * 轉成set
	 * 
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E, T extends Set<E>> Set<E> toSet(E[] values) {
		return toSet(values, LinkedHashSet.class);
	}

	/**
	 * 轉成set
	 * 
	 * @param values
	 * @param clazz
	 * @return
	 */
	public static <E, T extends Set<E>> Set<E> toSet(E[] values, Class<T> clazz) {
		Set<E> result = ClassHelper.newInstance(clazz);
		//
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				result.add(values[i]);
			}
		}
		return result;
	}

	/**
	 * 取得 map keys
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> List<K> getKeys(Map<K, V> values) {
		List<K> result = new LinkedList<K>();
		if (notEmpty(values)) {
			for (K key : values.keySet()) {
				result.add(key);
			}
		}
		return result;
	}

	/**
	 * 取得 map keys by value
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param value
	 * @return
	 */
	public static <K, V> List<K> getKeysByValue(Map<K, V> values, V value) {
		List<K> result = new LinkedList<K>();
		if (notEmpty(values)) {
			for (Map.Entry<K, V> entry : values.entrySet()) {
				if (value != null && entry.getValue() != null
						&& entry.getValue().equals(value)) {
					result.add(entry.getKey());
				} else if (value == null && entry.getValue() == null) {
					result.add(entry.getKey());
				}
			}
		}
		return result;
	}

	/**
	 * 取得 map values
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> List<V> getValues(Map<K, V> values) {
		List<V> result = new LinkedList<V>();
		if (notEmpty(values)) {
			for (V value : values.values()) {
				result.add(value);
			}
		}
		return result;
	}

	// [0]=class
	// [1]=value
	public static <E> Object[] getFirstValue(Collection<E> values) {
		Object[] result = new Object[2];
		if (notEmpty(values)) {
			for (E entry : values) {
				if (entry instanceof Collection) {
					result = getFirstValue((Collection<?>) entry);
				} else if (entry instanceof Map) {
					result = getFirstValue((Map<?, ?>) entry);
				} else if (entry != null) {
					result[0] = entry.getClass();
					result[1] = entry;
				}
				//
				if (result[1] != null) {
					break;
				}
			}
		}
		return result;
	}

	// [0]=class
	// [1]=value
	// 不取key作判斷,取value作判斷
	public static <K, V> Object[] getFirstValue(Map<K, V> values) {
		Object[] result = new Object[2];
		if (notEmpty(values)) {
			for (V entry : values.values()) {
				if (entry instanceof Map) {
					result = getFirstValue((Map<?, ?>) entry);
				}
				if (entry instanceof Collection) {
					result = getFirstValue((Collection<?>) entry);
				} else if (entry != null) {
					result[0] = entry.getClass();
					result[1] = entry;
					// System.out.println(objects[0]+"===="+objects[1]);
				}
				// 當null時,則略過,往下一個找直到非null
				if (result[1] != null) {
					break;
				}
			}
		}
		return result;
	}

	// [0]=class
	// [1]=value
	// 取key作判斷
	public static <K, V> Object[] getFirstKey(Map<K, V> values) {
		Object[] result = new Object[2];
		if (notEmpty(values)) {
			for (K entry : values.keySet()) {
				if (entry instanceof Map) {
					result = getFirstKey((Map<?, ?>) entry);
				}
				if (entry instanceof Collection) {
					result = getFirstValue((Collection<?>) entry);
				} else if (entry != null) {
					result[0] = entry.getClass();
					result[1] = entry;
					// System.out.println(objects[0]+"===="+objects[1]);
				}
				// 當null時,則略過,往下一個找直到非null
				if (result[1] != null) {
					break;
				}
			}
		}
		return result;
	}

	// 20120201
	public static <E> List<E> reverse(List<E> values) {
		List<E> result = new LinkedList<E>();
		if (notEmpty(values)) {
			result = ClassHelper.newInstance(values);// 為了傳回原本的實作class
			result.addAll(values);
			Collections.reverse(result);
		}
		return result;
	}

	public static <E> Set<E> reverse(Set<E> values) {
		Set<E> result = new LinkedHashSet<E>();
		if (notEmpty(values)) {
			result = ClassHelper.newInstance(values);// 為了傳回原本的實作class
			List<E> list = new LinkedList<E>();
			list.addAll(values);
			Collections.reverse(list);
			result.addAll(list);
		}
		return result;
	}

	public static <K, V> Map<K, V> reverse(Map<K, V> values) {
		Map<K, V> result = new LinkedHashMap<K, V>();
		if (notEmpty(values)) {
			result = ClassHelper.newInstance(values);
			Set<K> keySet = values.keySet();
			Object[] keys = (Object[]) keySet.toArray();
			int length = (keys != null ? keys.length : 0);
			for (int i = length - 1; i >= 0; i--) {
				K key = (K) keys[i];
				V value = values.get(key);
				result.put(key, value);
			}
		}
		return result;
	}

	// 20120319
	public static <E> boolean isEmpty(Collection<E> values) {
		return (values == null || values.isEmpty());
	}

	public static <K, V> boolean isEmpty(Map<K, V> values) {
		return (values == null || values.isEmpty());
	}

	public static <E> boolean notEmpty(Collection<E> values) {
		return (values != null && !values.isEmpty());
	}

	public static <K, V> boolean notEmpty(Map<K, V> values) {
		return (values != null && !values.isEmpty());
	}

	public static <E> int size(Collection<E> values) {
		int result = 0;
		if (values != null) {
			result = values.size();
		}
		return result;
	}

	public static <K, V> int size(Map<K, V> values) {
		int result = 0;
		if (values != null) {
			result = values.size();
		}
		return result;
	}

	/**
	 * 過濾null,empty,blank
	 * 
	 * @param receivers
	 * @return
	 */
	public static List<String> safeGet(List<String> receivers) {
		List<String> result = new LinkedList<String>();
		if (CollectionHelper.notEmpty(receivers)) {
			for (String receive : receivers) {
				if (StringHelper.notBlank(receive)) {
					result.add(receive);
				}
			}
		}
		return result;
	}

	/**
	 * 當size=100w時,用此方式取Queue的element會明顯變慢
	 * 
	 * @param queue
	 * @param index
	 * @return
	 */
	public static <E> E get(Queue<E> queue, int index) {
		E result = null;
		int i = 0;
		for (E e : queue) {
			if (i == index) {
				result = e;
				break;
			}
			i++;
		}
		return result;
	}

	/**
	 * 取得map的最後一個entry
	 * 
	 * @param values
	 * @return
	 */
	public static <K, V> Map.Entry<K, V> getLastEntry(Map<K, V> values) {
		Map.Entry<K, V> result = null;
		//
		int size = (values != null ? values.size() : 0);

		@SuppressWarnings("unchecked")
		Map.Entry<K, V>[] buffs = new Map.Entry[size];
		values.entrySet().toArray(buffs);
		if (size > 0) {
			// 最後一個
			result = buffs[size - 1];
		}
		return result;
	}

	/**
	 * 累計value
	 * 
	 * @param values
	 * @param key
	 * @param value
	 * @return
	 */
	public static <K> boolean accuValue(Map<K, Integer> values, K key, int value) {
		boolean result = false;
		if (key != null) {
			int origValue = NumberHelper.safeGet(values.get(key));
			origValue += value;
			values.put((K) key, origValue);
			result = true;
		}
		return result;
	}

	/**
	 * 累計value
	 * 
	 * @param values
	 * @return
	 */
	public static <K> boolean accuValue(Map<K, Integer> values,
			Map<K, Integer> destValues) {
		boolean result = false;
		// 原始道具
		Map<K, Integer> origValues = new LinkedHashMap<K, Integer>(values);
		//
		boolean added = true;
		for (Map.Entry<K, Integer> entry : destValues.entrySet()) {
			added &= accuValue(values, entry.getKey(), entry.getValue());
			// 當有一個加入失敗時,則還原
			if (!added) {
				values = origValues;
				break;
			}
			result = added;
		}
		return result;
	}

	/**
	 * 集合內元素轉成Integer
	 * 
	 * @param values
	 * @return
	 */
	public static List<Serializable> toInts(List<Serializable> values) {
		List<Serializable> result = new LinkedList<Serializable>();
		if (notEmpty(values)) {
			for (Serializable entry : values) {
				Integer buff = NumberHelper.toInt(entry);
				result.add(buff);
			}
		}
		return result;
	}

	/**
	 * 集合內元素轉成Long
	 * 
	 * @param values
	 * @return
	 */
	public static List<Serializable> toLongs(List<Serializable> values) {
		List<Serializable> result = new LinkedList<Serializable>();
		if (notEmpty(values)) {
			for (Serializable entry : values) {
				Long buff = NumberHelper.toLong(entry);
				result.add(buff);
			}
		}
		return result;
	}

	public static <T> String toString(Collection<T> values) {
		return toString(values, StringHelper.COMMA_SPACE);
	}

	public static <T> String toString(Collection<T> values, String splitter) {
		StringBuilder result = new StringBuilder();
		if (values != null) {
			int i = 0;
			int size = values.size();
			for (T entry : values) {
				result.append(entry != null ? entry.toString() : "null");
				//
				if (i < size - 1) {
					result.append(splitter);
				}
				i++;
			}
		}
		return result.toString();
	}

	public static <E> Set<E> checkDuplicate(List<E> values) {
		Set<E> result = new LinkedHashSet<E>();
		//
		Set<E> buff = new LinkedHashSet<E>();
		for (E e : values) {
			if (!buff.add(e)) {
				result.add(e);
			}
		}
		return result;
	}
}
