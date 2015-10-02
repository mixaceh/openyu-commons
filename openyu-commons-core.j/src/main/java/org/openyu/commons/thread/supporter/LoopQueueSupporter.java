package org.openyu.commons.thread.supporter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openyu.commons.thread.LoopQueue;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoopQueueSupporter<E> extends BaseRunnableSupporter
		implements LoopQueue<E> {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(LoopQueueSupporter.class);

	private Queue<E> elements = new ConcurrentLinkedQueue<E>();

	/**
	 * 預設監聽毫秒
	 */
	private static final long DEFAULT_LISTEN_MILLS = 1L;

	/**
	 * 監聽毫秒
	 */
	private long listenMills = DEFAULT_LISTEN_MILLS;

	protected transient Lock lock = new ReentrantLock();

	public LoopQueueSupporter(ThreadService threadService) {
		super(threadService);
	}

	public LoopQueueSupporter(ExecutorService executorService) {
		super(executorService);
	}

	public long getListenMills() {
		return listenMills;
	}

	public void setListenMills(long listenMills) {
		this.listenMills = listenMills;
	}

	public boolean offer(E e) {
		boolean result = false;
		// issue: synchronized慢
		// synchronized (elements) {
		// result = elements.offer(e);
		// }
		//
		// fix: 改用lock
		try {
			lock.lockInterruptibly();
			try {
				result = elements.offer(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		} catch (Exception ex) {
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
				ThreadHelper.sleep(listenMills);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * 執行
	 */
	protected void execute() {
		E e = null;
		try {
			// issue: synchronized慢
			// synchronized (elements) {
			// while (!elements.isEmpty()) {
			// try {
			// e = elements.poll();
			// // 真正要處理的邏輯
			// if (e != null) {
			// process(e);
			// }
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
			// }
			// }
			//
			// fix: 改用lock
			lock.lockInterruptibly();
			try {
				while (!elements.isEmpty()) {
					try {
						e = elements.poll();
						if (e != null) {
							doExecute(e);
						}
					} catch (Exception ex) {
						LOGGER.error("Failed", ex);
					}
				}
			} catch (Exception ex) {
				LOGGER.error("Failed", ex);
			} finally {
				lock.unlock();
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
