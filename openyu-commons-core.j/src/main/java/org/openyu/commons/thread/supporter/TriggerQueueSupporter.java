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

	private transient Condition notEmpty = lock.newCondition();

	public TriggerQueueSupporter(ThreadService threadService) {
		super(threadService);
	}

	public TriggerQueueSupporter(ExecutorService executorService) {
		super(executorService);
	}

	public boolean offer(E e) throws Exception {
		boolean result = false;
		try {
			this.lock.lockInterruptibly();
			try {
				// 沒啟動,不加入元素
				if (!isStarted()) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was not started").toString());
				}
				//
				result = elements.offer(e);
				if (result) {
					notEmpty.signalAll();
				}
			} catch (Throwable ex) {
				LOGGER.error(new StringBuilder("Exception encountered during offer()").toString(), ex);
				throw ex;
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException ex) {
			LOGGER.error(new StringBuilder("Exception encountered during offer()").toString(), ex);
			throw ex;
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
	protected void execute() throws Exception {
		E e = null;
		try {
			this.lock.lockInterruptibly();
			try {
				while (elements.isEmpty()) {
					notEmpty.await();
				}
				try {

					e = elements.poll();
					if (e != null) {
						doExecute(e);
					}
				} catch (Throwable ex) {
					LOGGER.error(new StringBuilder("Exception encountered during execute()").toString(), ex);
				}
			} catch (Throwable ex) {
				throw ex;
			} finally {
				this.lock.unlock();
			}
			// if (e != null) {
			// doExecute(e);
			// }
		} catch (InterruptedException ex) {
			LOGGER.error(new StringBuilder("Exception encountered during execute()").toString(), ex);
			throw ex;
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doExecute(E e) throws Exception;
}
