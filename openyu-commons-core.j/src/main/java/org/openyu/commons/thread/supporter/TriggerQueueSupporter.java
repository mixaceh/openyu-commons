package org.openyu.commons.thread.supporter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.TriggerQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TriggerQueueSupporter<E> extends BaseRunnableQueueSupporter<E>implements TriggerQueue<E> {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(TriggerQueueSupporter.class);

	private Queue<E> elements = new ConcurrentLinkedQueue<E>();

	private Lock lock = new ReentrantLock();

	private Condition notEmpty = lock.newCondition();

	public TriggerQueueSupporter(ThreadService threadService) {
		super(threadService);
	}

	public TriggerQueueSupporter(ExecutorService executorService) {
		super(executorService);
	}

	public boolean offer(E e) {
		boolean result = false;
		try {
			lock.lockInterruptibly();
			try {
				result = elements.offer(e);
				if (result) {
					notEmpty.signalAll();
				}
			} finally {
				lock.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 來自於BaseRunnable.run,緒處理
	 */
	@Override
	protected void doRun() throws Exception {
		while (true) {
			try {
				if (isShutdown()) {
					break;
				}
				//
				execute();
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * 內部執行
	 */
	protected void execute() {
		E e = null;
		try {
			lock.lockInterruptibly();
			try {
				try {
					while (elements.isEmpty()) {
						notEmpty.await();
					}
					e = elements.poll();
				} catch (Exception ex) {
					LOGGER.error("Failed", ex);
				}
			} finally {
				lock.unlock();
			}
			// 真正要處理的邏輯
			if (e != null) {
				doExecute(e);
			}
		} catch (Exception ex) {
			LOGGER.error("Failed", ex);
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doExecute(E e) throws Exception;
}
