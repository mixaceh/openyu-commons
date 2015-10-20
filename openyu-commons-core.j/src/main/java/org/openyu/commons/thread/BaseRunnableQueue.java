package org.openyu.commons.thread;

public interface BaseRunnableQueue<E> extends BaseRunnable {

	/**
	 * 加入元素
	 * 
	 * @param e
	 * @return
	 */
	boolean offer(E e) throws Exception;

}
