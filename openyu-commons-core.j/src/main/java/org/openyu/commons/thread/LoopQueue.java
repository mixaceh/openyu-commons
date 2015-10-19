package org.openyu.commons.thread;

/**
 * 固定時間的輪循佇列
 */
public interface LoopQueue<E> extends BaseRunnableQueue<E> {

	/**
	 * 監聽毫秒
	 * 
	 * @return
	 */
	long getListenMills();

	void setListenMills(long listenMills);

//	/**
//	 * 加入元素
//	 * 
//	 * remove to BaseRunnableQueue.offer
//	 * 
//	 * @param e
//	 * @return
//	 */
//	boolean offer(E e);

}
