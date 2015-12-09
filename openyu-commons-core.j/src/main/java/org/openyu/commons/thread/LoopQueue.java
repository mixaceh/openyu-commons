package org.openyu.commons.thread;

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

}
