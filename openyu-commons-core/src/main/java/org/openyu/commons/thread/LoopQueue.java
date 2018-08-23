package org.openyu.commons.thread;

import java.util.Comparator;

/**
 * 輪循佇列
 */
public interface LoopQueue<E> extends BaseRunnableQueue<E> {

	/**
	 * 監聽毫秒
	 * 
	 * @return
	 */
	long getListenMills();

	void setListenMills(long listenMills);

	/**
	 * 排序比較器
	 * 
	 * @return
	 */
	Comparator<E> getComparator();

	void setComparator(Comparator<E> comparator);
}
