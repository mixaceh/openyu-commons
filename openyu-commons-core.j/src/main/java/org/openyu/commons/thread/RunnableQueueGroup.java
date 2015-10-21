package org.openyu.commons.thread;

public interface RunnableQueueGroup<E> {

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
	boolean offer(E e) throws Exception;

	/**
	 * 啟動
	 */
	void start() throws Exception;


	/**
	 * 關閉
	 */
	void shutdown() throws Exception;
}
