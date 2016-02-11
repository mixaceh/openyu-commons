package org.openyu.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.CloneHelper;
import org.openyu.commons.lang.SystemHelper;

public class CollectionHelperTest extends BaseTestSupporter {

	@Test
	// 1000000 times: 327 mills.
	// 1000000 times: 322 mills.
	// 1000000 times: 342 mills.
	@SuppressWarnings("unchecked")
	public void toMap() {
		String[] keys = new String[] { "a", "b", "c", "d", "e", "f" };
		Object[] values = new Object[] { 1L, "bbb", 119, "xxx", true, "yyy" };
		Map<String, Object> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toMap(keys, values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.getClass().getSimpleName() + ": " + result);
		//
		result = CollectionHelper.toMap(keys, values, Hashtable.class);
		System.out.println(result.getClass().getSimpleName() + ": " + result);
	}

	@Test
	// 1000000 times: 692 mills.
	// 1000000 times: 687 mills.
	// 1000000 times: 683 mills.
	public void toConcurrentMap() {
		String[] keys = new String[] { "a", "b", "c", "d", "e", "f" };
		Object[] values = new Object[] { 1L, "bbb", 119, "xxx", null, "yyy" };
		Map<String, Object> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toConcurrentMap(keys, values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	// 1000000 times: 327 mills.
	// 1000000 times: 322 mills.
	// 1000000 times: 342 mills.
	@SuppressWarnings("unchecked")
	public void toList() {
		Object[] values = new Object[] { 1L, "bbb", 119, "xxx", true, "yyy" };
		List<Object> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toList(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.getClass().getSimpleName() + ": " + result);
		//
		result = CollectionHelper.toList(values, ArrayList.class);
		System.out.println(result.getClass().getSimpleName() + ": " + result);
	}

	@Test
	// 1000000 times: 327 mills.
	// 1000000 times: 322 mills.
	// 1000000 times: 342 mills.
	@SuppressWarnings("unchecked")
	public void toSet() {
		Object[] values = new Object[] { 1L, "bbb", 119, "xxx", true, "yyy" };
		Set<Object> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toSet(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.getClass().getSimpleName() + ": " + result);
		//
		result = CollectionHelper.toSet(values, HashSet.class);
		System.out.println(result.getClass().getSimpleName() + ": " + result);
	}

	// shallow
	@Test
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void cloneList() {
		Date date = new Date();
		LinkedList<Object> aaa = new LinkedList<Object>();
		aaa.add(date);

		LinkedList<Object> bbb = (LinkedList<Object>) aaa.clone();

		System.out.println("before -------------------------");
		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
		//
		date.setYear(100);// 1900+100
		System.out.println("after -------------------------");
		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
	}

	// shallow
	@Test
	@SuppressWarnings("deprecation")
	public void copyCollection() {
		Date date = new Date();
		List<Object> aaa = new LinkedList<Object>();
		aaa.add(date);

		List<Object> bbb = new LinkedList<Object>();
		bbb.add("aaa");

		Collections.copy(bbb, aaa);

		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
		//
		date.setYear(100);// 1900+100
		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
	}

	@Test
	// deep
	public void cloneByCloneHelper() {
		Date date = new Date();
		List aaa = new LinkedList();
		aaa.add(date);

		LinkedList bbb = (LinkedList) CloneHelper.clone(aaa);

		System.out.println("cloneHelperClone -------------------------");
		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
		//
		date.setYear(100);// 1900+100
		System.out.println("aaa: " + aaa);
		System.out.println("bbb: " + bbb);
	}

	@Test
	// 1000000 times: 161 mills.
	// 1000000 times: 167 mills.
	// 1000000 times: 168 mills.
	public void getKeys() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "aaa");
		map.put(2, "aaa");
		map.put(3, "bbb");
		map.put(4, "ccc");

		List<Integer> list = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			list = CollectionHelper.getKeys(map);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("list: " + list);
	}

	@Test
	// 1000000 times: 161 mills.
	// 1000000 times: 164 mills.
	// 1000000 times: 159 mills.
	public void getKeysByValue() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "aaa");
		map.put(2, "aaa");
		map.put(3, "bbb");
		map.put(4, "ccc");
		map.put(5, null);

		List<Integer> list = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// list = CollectionHelper.getKeysByValue(map, "aaa");
			list = CollectionHelper.getKeysByValue(map, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("list: " + list);
	}

	@Test
	// 1000000 times: 161 mills.
	// 1000000 times: 167 mills.
	// 1000000 times: 168 mills.
	public void getValues() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "aaa");
		map.put(2, "aaa");
		map.put(3, "bbb");
		map.put(4, "ccc");

		List<String> list = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			list = CollectionHelper.getValues(map);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("list: " + list);
	}

	@Test
	// 1000000 times: 161 mills.
	// 1000000 times: 164 mills.
	// 1000000 times: 159 mills.
	public void getFirstValue() {
		List list = new LinkedList();

		List list2 = new LinkedList();
		list2.add(null);
		// list2.add("aaa");
		list2.add(new String[] { "aaa" });
		list2.add("bbb");
		list.add(list2);

		Map map = new LinkedHashMap();
		map.put(1, new Date());
		map.put(2, new StringBuilder());
		list.add(map);

		Object[] objects = null;
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			objects = CollectionHelper.getFirstValue(list);
			objects = CollectionHelper.getFirstValue(map);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(objects);
	}

	@Test
	// 1000000 times: 161 mills.
	// 1000000 times: 164 mills.
	// 1000000 times: 159 mills.
	public void getFirstKey() {
		Map map = new LinkedHashMap();
		map.put(null, null);
		map.put(1, new Date());
		map.put(2, new StringBuilder());

		Object[] objects = null;
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			objects = CollectionHelper.getFirstKey(map);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(objects);
	}

	@Test
	// 1000000 times: 326 mills.
	// 1000000 times: 321 mills.
	// 1000000 times: 327 mills.
	public void reverseList() {
		List list = new LinkedList();
		list.add(111);

		List list2 = new LinkedList();
		list2.add(null);
		// list2.add("aaa");
		list2.add(new String[] { "aaa" });
		list2.add("bbb");
		list2.add("ccc");
		list.add(list2);

		List<?> ret = null;

		// System.out.println(list);
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ret = CollectionHelper.reverse(list);
			// ret = CollectionHelper.reverse((List)null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(list);
		System.out.println(ret);
		//
	}

	@Test
	// 1000000 times: 548 mills.
	// 1000000 times: 559 mills.
	// 1000000 times: 548 mills.
	public void reverseSet() {
		Set set = new LinkedHashSet();
		set.add(111);

		Set set2 = new LinkedHashSet();
		set2.add(null);
		// set2.add("aaa");
		set2.add(new String[] { "aaa" });
		set2.add("bbb");
		set2.add("ccc");
		set.add(set2);

		Set ret = null;

		// System.out.println(list);
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ret = CollectionHelper.reverse(set);
			// ret = CollectionHelper.reverse((Set)null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(set);
		System.out.println(ret);
		//
	}

	@Test
	// 1000000 times: 587 mills.
	// 1000000 times: 560 mills.
	// 1000000 times: 615 mills.
	public void reverseMap() {
		Map map = new LinkedHashMap();
		map.put(0, null);
		map.put(1, new Date());
		map.put(2, new StringBuilder().append("aaa"));

		Map ret = null;
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ret = CollectionHelper.reverse(map);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(map);
		System.out.println(ret);
	}

	@Test
	public void reverseOrder() {
		String elements[] = { "S", "P", "E", "G", "P" };
		Set set = new TreeSet(Collections.reverseOrder());
		for (int i = 0, n = elements.length; i < n; i++) {
			set.add(elements[i]);
		}
		System.out.println(set);

		//
		LinkedList<Integer> ll = new LinkedList<Integer>();
		ll.add(-8);
		ll.add(20);
		ll.add(-20);
		ll.add(8);

		Comparator<Integer> r = Collections.reverseOrder();

		Collections.sort(ll, r);

		System.out.print("List sorted in reverse: ");
		for (int i : ll) {
			System.out.print(i + " ");
		}

		System.out.println();

		Collections.shuffle(ll);

		System.out.print("List shuffled: ");
		for (int i : ll)
			System.out.print(i + " ");

		System.out.println();

		System.out.println("Minimum: " + Collections.min(ll));
		System.out.println("Maximum: " + Collections.max(ll));
	}

	@Test
	// 1000000 times: 71 mills.
	// 1000000 times: 69 mills.
	// 1000000 times: 69 mills.
	public void isEmpty() {
		List value = new LinkedList();

		boolean result = false;

		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.isEmpty(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);

		Map map = new LinkedHashMap();
		result = CollectionHelper.isEmpty(map);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 71 mills.
	// 1000000 times: 69 mills.
	// 1000000 times: 69 mills.
	public void isNotEmpty() {
		List value = new LinkedList();
		value.add(111);

		boolean result = false;

		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.notEmpty(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);

		Map map = new LinkedHashMap();
		map.put(1, 111);
		result = CollectionHelper.notEmpty(map);
		System.out.println(result);
	}

	@Test
	// 1000000 times: 71 mills.
	// 1000000 times: 69 mills.
	// 1000000 times: 69 mills.
	public void size() {
		List value = new LinkedList();

		int result = 0;

		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.size(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals(0, value.size());

		Map map = new LinkedHashMap();
		map.put(1, 111);
		result = CollectionHelper.size(map);
		System.out.println(result);
		assertEquals(1, map.size());
	}

	@Test
	// 10000 times: 317 mills.
	// 10000 times: 355 mills.
	// 10000 times: 311 mills.
	public void getByQueue() {
		Queue<Integer> value = new ConcurrentLinkedQueue<Integer>();
		//
		int count = 1000000;
		for (int i = 0; i < count; i++) {
			value.offer(i);
		}
		//
		Integer result = null;
		count = 10000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.get(value, i);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void arrayList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("data_function_020");
		list.add("data_function_021");
		list.add("data_function_022");
		list.add("data_function_023");
		System.out.println(list);
		//
		list.add("030");
		list.add("aaa_031");
		list.add("123_032");
		list.add("xxx_033");
		System.out.println(list);

	}

	@Test
	public void getLastEntry() {
		Map<Integer, String> value = new LinkedHashMap<Integer, String>();
		value.put(1, "aaa");
		value.put(2, "bbb");
		value.put(3, "ccc");
		//
		Map.Entry<Integer, String> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.getLastEntry(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		//
		assertNotNull(result);
		assertEquals(new Integer(3), result.getKey());
		assertEquals("ccc", result.getValue());
	}

	@Test
	public void put() {
		Map<Integer, String> value = new LinkedHashMap<Integer, String>();
		String ret = value.put(1, "aaa");
		System.out.println(ret);// null
		System.out.println(value);
		//
		ret = value.put(1, "aaaAAA");
		System.out.println(ret);// aaa
		System.out.println(value);
		//
		value.put(2, "bbb");
		ret = value.remove(2);// bbb
		System.out.println(ret);
		System.out.println(value);
		//
		ret = value.remove(3);// null
		System.out.println(ret);
		System.out.println(value);
	}

	@Test
	public void accuValue() {
		Map<String, Integer> origValue = new LinkedHashMap<String, Integer>();
		origValue.put("aaa", 1);
		origValue.put("bbb", 1);
		origValue.put("ccc", 1);
		//
		CollectionHelper.accuValue(origValue, "aaa", 1);
		CollectionHelper.accuValue(origValue, "ddd", 1);
		System.out.println(origValue);
		assertEquals(2, (int) origValue.get("aaa"));
		//
		Map<String, Integer> value = new LinkedHashMap<String, Integer>();
		value.put("aaa", 1);
		value.put("bbb", 1);
		value.put("ccc", 1);
		//
		value.put("fff", 10);
		//
		CollectionHelper.accuValue(origValue, value);
		System.out.println(origValue);
		assertEquals(3, (int) origValue.get("aaa"));
		assertEquals(2, (int) origValue.get("bbb"));
	}

	@Test
	// 1000000 times: 7036 mills.
	// 1000000 times: 6608 mills.
	// 1000000 times: 6920 mills.
	public void toLongs() {
		List<Serializable> values = new LinkedList<Serializable>();
		values.add("100");
		values.add("101");
		values.add("aaa");
		//
		List<Serializable> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toLongs(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);// [100, 101, 0]
		//
		assertNotNull(result);
		assertEquals(100L, result.get(0));
		assertEquals(101L, result.get(1));
		assertEquals(0L, result.get(2));
		//
		values.clear();
		values.add(5L);
		values.add(25);
		values.add("aaa");
		result = CollectionHelper.toLongs(values);
		System.out.println(result);// [5, 25, 0]
		//
		assertNotNull(result);
		assertEquals(5L, result.get(0));
		assertEquals(25L, result.get(1));
		assertEquals(0L, result.get(2));
	}

	@Test
	// 1000000 times: 763 mills.
	public void toStringz() {
		List<Serializable> values = new LinkedList<Serializable>();
		values.add("100");
		values.add("101");
		values.add("aaa");
		values.add(null);
		//
		String result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.toString(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);// 100, 101, aaa, null
	}

	@Test
	// 1000000 times: 763 mills.
	public void checkDuplicate() {
		List<String> values = new LinkedList<String>();
		values.add("100");
		values.add("101");
		values.add("101");
		values.add(null);
		values.add(null);
		//
		Set<String> result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = CollectionHelper.checkDuplicate(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);// 101, null
	}
}
