package org.openyu.commons.bean;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.openyu.commons.bean.supporter.BaseBeanSupporter;
import org.openyu.commons.bean.supporter.LocaleNameBeanSupporter;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.vo.impl.DogImpl;

public class BeanHelperTest extends BaseTestSupporter {

	@Test
	// 1000000 times: 114 mills.
	// 1000000 times: 114 mills.
	// 1000000 times: 115 mills.
	// verified
	public void getNameByLocale() {
		Set<LocaleNameBean> value = new LinkedHashSet<LocaleNameBean>();
		//
		LocaleNameBean name = new LocaleNameBeanSupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		value.add(name);
		//
		name = new LocaleNameBeanSupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		value.add(name);
		//
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = BeanHelper.getName(value, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("拉拉", result);
		//
		result = BeanHelper.getName(value, Locale.US);
		System.out.println(result);
		assertEquals("LaLa", result);
	}

	@Test
	// 1000000 times: 159 mills.
	// 1000000 times: 162 mills.
	// 1000000 times: 158 mills.
	// verified
	public void processFilterName() {
		NamesBean value = new NamesBeanSupporter();
		//
		LocaleNameBean name = new LocaleNameBeanSupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		value.getNames().add(name);
		//
		name = new LocaleNameBeanSupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		value.getNames().add(name);
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			BeanHelper.processFilterName(value, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(value);
		assertEquals(1, value.getNames().size());
	}

	@Test
	// 1000000 times: 159 mills.
	// 1000000 times: 162 mills.
	// 1000000 times: 158 mills.
	// verified
	public void filterName() {
		DogImpl value = new DogImpl();
		//
		value.setName(Locale.TRADITIONAL_CHINESE, "拉拉");
		value.setName(Locale.US, "LaLa");
		//
		value.setDescription(Locale.TRADITIONAL_CHINESE, "拉拉看起來像拉拉");
		value.setDescription(Locale.US, "LaLa looks like LaLa");
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			BeanHelper.filterName(value, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(value);
		assertEquals(1, value.getNames().size());
		assertEquals(1, value.getDescriptions().size());
		// 沒簡中
		BeanHelper.filterName(value, Locale.SIMPLIFIED_CHINESE);
		System.out.println(value);
		assertEquals(0, value.getNames().size());
	}

	// --------------------------------------------------------
	@Test
	// 1000000 times: 109 mills.
	// 1000000 times: 109 mills.
	// 1000000 times: 113 mills.
	// verified
	public void sumOf() {
		Map<String, Item> items = mockItems();
		//
		int result = 0;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = BeanHelper.sumOf(items.values());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// 253
		assertEquals(253, result);
	}

	@Test
	// #issue
	// 1000000 times: 192 mills.
	// 1000000 times: 201 mills.
	// 1000000 times: 197 mills.
	//
	// #fix
	// 1000000 times: 78 mills.
	// 1000000 times: 78 mills.
	// 1000000 times: 78 mills.
	public void randomOf() {
		Map<String, Item> items = mockItems();
		//
		int sum = BeanHelper.sumOf(items);
		//
		Item result = null;
		int times = 0;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = BeanHelper.randomOf(items, sum);
			//
			if (result != null && result.getName().equals("777")) {
				times += 1;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(items);
		System.out.println("中 " + times + " 次");
	}

	@Test
	// 1000000 times: 2052 mills.
	// 1000000 times: 2060 mills.
	// 1000000 times: 2048 mills.
	// verified
	public void probSumOf() {
		Map<String, Item> items = mockItems();
		// 塞一下機率
		BeanHelper.randomOf(items, BeanHelper.sumOf(items));
		//
		double result = 0;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = BeanHelper.probSumOf(items);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// 1
		assertEquals(0, Double.compare(1d, result));
	}

	@Test
	// #issue
	// 1000000 times: 192 mills.
	// 1000000 times: 201 mills.
	// 1000000 times: 197 mills.
	//
	// #fix
	// 1000000 times: 78 mills.
	// 1000000 times: 78 mills.
	// 1000000 times: 78 mills.
	public void probRandomOf() {
		Map<String, Item> items = mockItems();
		// 塞一下機率
		BeanHelper.randomOf(items, BeanHelper.sumOf(items));
		//
		double sum = BeanHelper.probSumOf(items);
		//
		Item result = null;
		int times = 0;

		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = BeanHelper.probRandomOf(items, sum);
			if (result != null && result.getName().equals("777")) {
				times += 1;
			}

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(items);
		System.out.println("中 " + times + " 次");
	}

	// --------------------------------------------------------

	public Map<String, Item> mockItems() {
		Map<String, Item> result = new LinkedHashMap<String, Item>();
		//
		Item item = new Item();
		item.setId("a");
		item.setName("荔枝");
		item.setWeight(100);
		result.put(item.getId(), item);
		//
		item = new Item();
		item.setId("b");
		item.setName("摃龜");
		item.setWeight(100);
		result.put(item.getId(), item);
		//
		item = new Item();
		item.setId("c");
		item.setName("BAR");
		item.setWeight(2);
		result.put(item.getId(), item);
		//
		item = new Item();
		item.setId("d");
		item.setName("777");
		item.setWeight(1);
		result.put(item.getId(), item);
		//
		item = new Item();
		item.setId("e");
		item.setName("銅鐘");
		item.setWeight(50);
		result.put(item.getId(), item);
		//
		return result;
	}

	// private Collection mockItemCollection()
	// {
	// return mockItemMap().values();
	// }

	// 道具機率,implement ProbabilityBean
	// 道具權重,implement WeightBean(->ProbabilityBean)
	public static class Item extends BaseBeanSupporter implements WeightBean,
			Cloneable {
		private String id;

		private String name;

		private int weight;

		private double probability;

		public Item() {

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

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;

		}

		public double getProbability() {
			return probability;
		}

		public void setProbability(double probability) {
			this.probability = probability;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this,
					ToStringStyle.MULTI_LINE_STYLE);
		}
	}

}
