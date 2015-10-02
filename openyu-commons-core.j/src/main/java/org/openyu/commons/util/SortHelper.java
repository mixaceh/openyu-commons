package org.openyu.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class SortHelper.
 */
public class SortHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(SortHelper.class);

	/**
	 * Instantiates a new blank helper.
	 */
	public SortHelper() {
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
		// private static final SortHelper INSTANCE = new SortHelper();
		private static SortHelper INSTANCE = new SortHelper();
	}

	/**
	 * Gets the single instance of SortHelper.
	 *
	 * @return single instance of SortHelper
	 */
	public synchronized static SortHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new SortHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	// --------------------------------------------------
	// 選擇排序
	// 將要排序的對象分作兩部份，一個是已排序的，一個是未排序的。如果排序是由小而大，
	// 從後端未排序部份選擇一個最小值，並放入前端已排序部份的最後一個。例如：
	//
	// 排序前：70 80 31 37 10 1 48 60 33 80
	//
	// [1] 80 31 37 10 70 48 60 33 80；選出最小值1
	// [1 10] 31 37 80 70 48 60 33 80；選出最小值10
	// [1 10 31] 37 80 70 48 60 33 80；選出最小值31
	// [1 10 31 33] 80 70 48 60 37 80 ......
	// [1 10 31 33 37] 70 48 60 80 80 ......
	// [1 10 31 33 37 48] 70 60 80 80 ......
	// [1 10 31 33 37 48 60] 70 80 80 ......
	// [1 10 31 33 37 48 60 70] 80 80 ......
	// [1 10 31 33 37 48 60 70 80] 80 ......

	/**
	 * 選擇排序
	 * 
	 * 將要排序的對象分作兩部份，一個是已排序的，一個是未排序的。如果排序是由小而大，
	 * 
	 * 從後端未排序部份選擇一個最小值，並放入前端已排序部份的最後一個。例如：
	 * 
	 * 排序前：70 80 31 37 10 1 48 60 33 80
	 * 
	 * [1] 80 31 37 10 70 48 60 33 80；選出最小值1
	 * 
	 * [1 10] 31 37 80 70 48 60 33 80；選出最小值10
	 * 
	 * [1 10 31] 37 80 70 48 60 33 80；選出最小值31
	 * 
	 * [1 10 31 33] 80 70 48 60 37 80 ......
	 * 
	 * [1 10 31 33 37] 70 48 60 80 80 ......
	 * 
	 * [1 10 31 33 37 48] 70 60 80 80 ......
	 * 
	 * [1 10 31 33 37 48 60] 70 80 80 ......
	 * 
	 * [1 10 31 33 37 48 60 70] 80 80 ......
	 * 
	 * [1 10 31 33 37 48 60 70 80] 80 ......
	 *
	 * @param values
	 *            the values
	 */
	public static void selection(final int[] values) {
		int minIndex;
		int buff;
		int length = values.length;
		for (int i = 0; i < length - 1; i++) {
			minIndex = i;
			for (int j = i + 1; j < length; j++) {
				if (values[j] < values[minIndex]) {
					minIndex = j;
				}
			}
			if (minIndex != i) {
				buff = values[i];
				values[i] = values[minIndex];
				values[minIndex] = buff;
			}
		}
	}

	/**
	 * 插入排序
	 * 
	 * 將要排序的對象分作兩部份，一個是已排序的，一個是未排序的。每次從後端未排序部份取得最前端的值，
	 * 
	 * 然後插入前端已排序部份的適當位置。例如：
	 *
	 * 排序前：92 77 67 8 6 84 55 85 43 67
	 *
	 * [77 92] 67 8 6 84 55 85 43 67；將77插入92前
	 * 
	 * [67 77 92] 8 6 84 55 85 43 67；將67插入77前
	 * 
	 * [8 67 77 92] 6 84 55 85 43 67；將8插入67前
	 * 
	 * [6 8 67 77 92] 84 55 85 43 67；將6插入8前
	 * 
	 * [6 8 67 77 84 92] 55 85 43 67；將84插入92前
	 * 
	 * [6 8 55 67 77 84 92] 85 43 67；將55插入67前
	 * 
	 * [6 8 55 67 77 84 85 92] 43 67 ......
	 * 
	 * [6 8 43 55 67 77 84 85 92] 67 ......
	 * 
	 * [6 8 43 55 67 67 77 84 85 92] ......
	 *
	 * @param values
	 *            the values
	 */
	public static void insertion(int[] values) {
		int i, j, newValue;
		for (i = 1; i < values.length; i++) {
			newValue = values[i];
			j = i;
			while (j > 0 && values[j - 1] > newValue) {
				values[j] = values[j - 1];
				j--;
			}
			values[j] = newValue;
		}
	}

	/**
	 * 氣泡排序法
	 * 
	 * 將要排序的對象分作兩部份，一個是已排序的，一個是未排序的。排序時若是從小到大，
	 * 
	 * 排序前：95 27 90 49 80 58 6 9 18 50
	 *
	 * @param values
	 *            the values
	 */
	public static void bubble(int[] values) {
		boolean swapped = true;
		int j = 0;
		int tmp;
		while (swapped) {
			swapped = false;
			j++;
			for (int i = 0; i < values.length - j; i++) {
				if (values[i] > values[i + 1]) {
					tmp = values[i];
					values[i] = values[i + 1];
					values[i + 1] = tmp;
					swapped = true;
				}
			}
		}
	}
}
