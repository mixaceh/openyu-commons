package org.openyu.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class SearchHelper.
 */
public class SearchHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(SearchHelper.class);

	/**
	 * Instantiates a new blank helper.
	 */
	public SearchHelper() {
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
		// private static final SearchHelper INSTANCE = new SearchHelper();
		private static SearchHelper INSTANCE = new SearchHelper();
	}

	/**
	 * Gets the single instance of SearchHelper.
	 *
	 * @return single instance of SearchHelper
	 */
	public synchronized static SearchHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new SearchHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 循序搜尋法, 不需排序
	 * 
	 * Linear/Sequential
	 * 
	 * @param array
	 * @param num
	 * @return
	 */
	public static int sequential(int[] array, int num) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == num) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 二元搜尋法, 需先排序
	 * 
	 * 迭代法
	 * 
	 * @param array
	 * @param num
	 * @return
	 */
	public static int iterative(int[] array, int num) {
		int left = 0, right = array.length - 1;
		while (left <= right) {
			int middle = (right + left) / 2;
			if (array[middle] == num)
				return middle;
			if (array[middle] > num)
				right = middle - 1;
			else
				left = middle + 1;
		}
		return -1;
	}

	/**
	 * 二元搜尋法, 需先排序
	 * 
	 * DivideAndConquer
	 * 
	 * @param array
	 * @param num
	 * @return
	 */
	public static int divideAndConquer(int[] array, int num) {
		return divideAndConquer(array, num, 0, array.length - 1);
	}

	/**
	 * 二元搜尋, 需先排序
	 * 
	 * DivideAndConquer
	 * 
	 * @param array
	 * @param num
	 * @param left
	 * @param right
	 * @return
	 */
	public static int divideAndConquer(int[] array, int num, int left, int right) {
		if (left > right)
			return -1;
		int middle = (right + left) / 2;
		if (array[middle] == num)
			return middle;
		if (array[middle] > num)
			return divideAndConquer(array, num, left, middle - 1);
		return divideAndConquer(array, num, middle + 1, right);
	}
}
