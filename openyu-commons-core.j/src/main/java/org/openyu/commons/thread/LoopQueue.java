package org.openyu.commons.thread;

/**
 * 固定時間的輪循佇列
 */
public interface LoopQueue<E> extends BaseRunnable {

	/**
	 * 監聽毫秒
	 * 
	 * @return
	 */
	long getListenMills();

	void setListenMills(long listenMills);

	/**
	 * 加入元素
	 * 
	 * @param e
	 * @return
	 */
	boolean offer(E e);

//	/**
//	 * 處理真正要處理的邏輯
//	 * 
//	 * @param e
//	 */
//	void process(E e);
}
