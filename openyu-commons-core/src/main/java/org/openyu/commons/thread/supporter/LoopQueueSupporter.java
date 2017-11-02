package org.openyu.commons.thread.supporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.openyu.commons.thread.LoopQueue;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.util.CollectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoopQueueSupporter<E> extends BaseRunnableQueueSupporter<E> implements LoopQueue<E> {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(LoopQueueSupporter.class);

	private Queue<E> elements = new ConcurrentLinkedQueue<E>();

	/**
	 * 預設監聽毫秒
	 */
	private static final long DEFAULT_LISTEN_MILLS = 1L;

	/**
	 * 監聽毫秒
	 */
	private long listenMills = DEFAULT_LISTEN_MILLS;

	public LoopQueueSupporter(ThreadService threadService) {
		super(threadService);
	}

	public LoopQueueSupporter(ExecutorService executorService) {
		super(executorService);
	}

	public LoopQueueSupporter() {
		super();
	}

	public long getListenMills() {
		return listenMills;
	}

	public void setListenMills(long listenMills) {
		this.listenMills = listenMills;
	}

	public final boolean offer(E e) {
		boolean result = false;
		// issue: synchronized慢
		// synchronized (elements) {
		// result = elements.offer(e);
		// }
		//

		// fix: 改用lock
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
			} catch (Throwable ex) {
				LOGGER.error(new StringBuilder("Exception encountered during offer()").toString(), ex);
				throw ex;
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException ex) {
			LOGGER.error(new StringBuilder("Exception encountered during offer()").toString(), ex);
		}
		return result;
	}

	/**
	 * 來自於BaseRunnable.run,緒處理
	 */
	@Override
	protected void doRun() throws Exception {
		for (;;) {
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
	protected void execute() throws Exception {
		// E e = null;
		List<E> list = new ArrayList<E>();
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
			this.lock.lockInterruptibly();
			try {
				while (!elements.isEmpty()) {
					// try {
					E e = elements.poll();
					if (e != null) {
						// doExecute(e);
						list.add(e);
					}
					// } catch (Throwable ex) {
					// LOGGER.error(new StringBuilder("Exception encountered during
					// execute()").toString(), ex);
					// }
				}
			} catch (Throwable ex) {
				throw ex;
			} finally {
				this.lock.unlock();
			}
			//
			if (CollectionHelper.notEmpty(list)) {
				doList(list);
			}
		} catch (InterruptedException ex) {
			LOGGER.error(new StringBuilder("Exception encountered during execute()").toString(), ex);
			throw ex;
		}
	}

	protected void doList(List<E> list) {
		for (E e : list) {
			try {
				doExecute(e);
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during doExecute()", ex);
			}
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doExecute(E e) throws Exception;
}
