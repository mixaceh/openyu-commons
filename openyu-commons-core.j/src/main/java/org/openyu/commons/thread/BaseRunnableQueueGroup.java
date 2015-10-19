package org.openyu.commons.thread;

public interface BaseRunnableQueueGroup<E> {

	/**
	 * 加入queues
	 * 
	 * @return
	 */
	BaseRunnableQueue<E>[] getQueues();

	void setQueues(BaseRunnableQueue<E>[] queues);

	/**
	 * 加入元素
	 * 
	 * @param e
	 * @return
	 */
	boolean offer(E e);

	/**
	 * 啟動
	 */
	void start();

	/**
	 * 關閉
	 */
	void shutdown();

	/**
	 * 是否關閉
	 * 
	 * @return
	 */
	boolean isShutdown();

}
