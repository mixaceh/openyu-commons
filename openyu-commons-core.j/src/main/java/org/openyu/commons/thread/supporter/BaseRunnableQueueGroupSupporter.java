package org.openyu.commons.thread.supporter;

import java.util.concurrent.atomic.AtomicInteger;

import org.openyu.commons.thread.BaseRunnableQueue;
import org.openyu.commons.thread.BaseRunnableQueueGroup;

public class BaseRunnableQueueGroupSupporter<E> implements BaseRunnableQueueGroup<E> {

	private BaseRunnableQueue<E> queues[] = null;

	private int queueSize;

	private AtomicInteger counter = new AtomicInteger(0);
	private boolean shutdown;

	@Override
	public BaseRunnableQueue<E>[] getQueues() {
		return queues;
	}

	@Override
	public void setQueues(BaseRunnableQueue<E>[] queues) {
		this.queues = queues;
		this.queueSize = (queues != null ? queues.length : 0);
	}

	// public BaseRunnableQueueGroupSupporter(LoopQueue<E> listQueues[]) {
	// try {
	// this.queues = listQueues;
	// this.queueSize = listQueues.length;
	// for (LoopQueue<E> loopQueue : listQueues) {
	// Thread thread = new Thread(loopQueue);
	// thread.start();
	// }
	// } catch (Exception ex) {
	//
	// }
	// }

	protected BaseRunnableQueue<E> getNextQueue() {
		if (Integer.MAX_VALUE == this.counter.get()) {
			this.counter.set(0);
		}
		int index = this.counter.getAndIncrement() % this.queueSize;
		return this.queues[index];
	}

	public boolean offer(E e) {
		return getNextQueue().offer(e);
	}

	/**
	 * 啟動
	 */
	@Override
	public void start() {
		for (BaseRunnableQueue<E> queue : queues) {
			queue.start();
		}
	}

	/**
	 * 關閉
	 */
	@Override
	public void shutdown() {
		for (BaseRunnableQueue<E> queue : queues) {
			queue.shutdown();
		}
	}

	/**
	 * 是否關閉
	 */
	@Override
	public boolean isShutdown() {
		return shutdown;
	}

}
