package org.openyu.commons.thread.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.openyu.commons.thread.LoopQueue;
import org.openyu.commons.thread.LoopQueueGroup;
import org.springframework.beans.factory.BeanInitializationException;

public class LoopQueueGroupImpl<E> implements LoopQueueGroup<E> {

	private LoopQueue<E> listQueues[] = null;

	private int listQueueSize;

	private AtomicInteger counter = new AtomicInteger(0);

	public LoopQueueGroupImpl(LoopQueue<E> listQueues[]) {
		try {
			this.listQueues = listQueues;
			this.listQueueSize = listQueues.length;
			for (LoopQueue<E> loopQueue : listQueues) {
				Thread thread = new Thread(loopQueue);
				thread.start();
			}
		} catch (Exception ex) {
			throw new BeanInitializationException("Initialization of LoopQueueGroupImpl failed", ex);
		}
	}

	protected LoopQueue<E> getNextQueue() {
		if (Integer.MAX_VALUE == counter.get()) {
			counter.set(0);
		}
		int index = counter.getAndIncrement() % listQueueSize;
		return listQueues[index];
	}

	public boolean offer(E e) {
		return getNextQueue().offer(e);
	}
}
