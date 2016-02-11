package org.openyu.commons.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * 测试 对于ArrayList的iterator遍历和for遍历的效率问题 注： 这个测试结果对其他容器类不一定成立 比如LinkedList
 * 
 * iterator慢
 * 
 * for快
 * 
 */
public class IteratorTest {

	static int size = 1000000;

	static List<Object> list1 = new ArrayList<Object>();

	static List<Object> list2 = new ArrayList<Object>();
	static {
		for (int i = 0; i < size; i++) {
			list1.add(new Object());
			list2.add(new Object());
		}
	}

	@Test
	public void iteratorTest() {
		long start = System.currentTimeMillis();
		for (Iterator<Object> iterator = list1.iterator(); iterator.hasNext();) {
			Object entry = (Object) iterator.next();
			// System.out.print(entry);
		}
		System.out.println("\niterator " + (System.currentTimeMillis() - start)
				+ " ms");
	}

	// @Test(dependsOnMethods = "iteratorTest")
	// public void forTest() {
	// long start = System.currentTimeMillis();
	// for (int i = 0, size = list2.size(); i < size; i++) {// 这里是一次性求出size()
	// // ，假如在遍历的过程中list的内容有增减的话，遍历逻辑会出错
	// Object entry = list2.get(i);
	// // System.out.print(entry);
	// }
	// System.out.println("\nfor " + (System.currentTimeMillis() - start)
	// + " ms");
	// }
	//
	// @Test(groups = "alone", invocationCount = 10)
	// // testng 测试10次
	// public void compareTest() {
	// long start = System.currentTimeMillis();
	// for (Iterator<Object> iterator = list1.iterator(); iterator.hasNext();) {
	// Object entry = (Object) iterator.next();
	// // System.out.print(entry);
	// }
	// long time = System.currentTimeMillis() - start;
	// start = System.currentTimeMillis();
	// for (int i = 0, size = list2.size(); i < size; i++) {
	// Object entry = list2.get(i);
	// // System.out.print(entry);
	// }
	// long time2 = System.currentTimeMillis() - start;
	// System.out.println("iterator " + time + " ms, for " + time2 + " ms");
	// }
}