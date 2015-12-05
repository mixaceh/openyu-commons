package org.openyu.commons.thread.supporter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.BaseRunnable;
import org.openyu.commons.thread.ThreadService;

/**
 * 1.ThreadService
 *
 * 2.ExecutorService
 * 
 * 3.new Thread()
 */
public abstract class BaseRunnableSupporter implements BaseRunnable, Supporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseRunnableSupporter.class);

	private transient ThreadService threadService;

	private transient ExecutorService executorService;

	private transient boolean starting;

	private transient boolean started;

	private transient boolean shuttingdown;

	private transient boolean shutdown;

	private transient boolean createThread;

	private transient String displayName;

	/**
	 * start/shutdown lock
	 */
	protected transient final Lock lock = new ReentrantLock();

	public BaseRunnableSupporter(ThreadService threadService) {
		this.threadService = threadService;
	}

	public BaseRunnableSupporter(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public BaseRunnableSupporter() {
		this((ExecutorService) null);
	}

	protected String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(ClassHelper.getSimpleName(getClass()));
			buff.append(" @" + Integer.toHexString(hashCode()));
			displayName = buff.toString();
		}
		return displayName;
	}

	public final void run() {
		try {
			if (createThread) {
				LOGGER.info(new StringBuilder().append("T[" + Thread.currentThread().getId() + "] ")
						.append("Using new Thread() instead of pool to run ").append(getDisplayName()).toString());
			} else {
				LOGGER.info(new StringBuilder().append("T[" + Thread.currentThread().getId() + "] ").append("Running ")
						.append(getDisplayName()).toString());
			}
			// --------------------------------------------------
			doRun();
			// --------------------------------------------------
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during run()").toString(), e);
		}
		//
		if (this.shutdown) {
			LOGGER.info(new StringBuilder().append("T[" + Thread.currentThread().getId() + "] ").append("Interrupted ")
					.append(getDisplayName()).toString());
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doRun() throws Exception;

	/**
	 * 啟動
	 */
	@Override
	public final void start() throws Exception {
		try {
			this.lock.lockInterruptibly();
			try {
				if (this.starting) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" is starting").toString());
				}
				//
				if (this.started) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was already started").toString());
				}
				//
				this.starting = true;
				// LOGGER.info(new StringBuilder().append("Starting
				// ").append(getDisplayName()).toString());
				// --------------------------------------------------
				if (threadService != null) {
					this.threadService.submit(this);
				} else if (executorService != null) {
					this.executorService.submit(this);
				} else {
					// use thread
					Thread thread = new Thread(this);
					thread.start();
					this.createThread = true;
				}
				// --------------------------------------------------
				this.starting = false;
				this.started = true;
				this.shutdown = false;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
				throw e;
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
			throw e;
		}
	}

	/**
	 * 是否啟動
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	/**
	 * 關閉
	 */
	@Override
	public final void shutdown() throws Exception {
		try {
			this.lock.lockInterruptibly();
			try {
				if (this.shuttingdown) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" is shuttingdown").toString());
				}
				//
				if (this.shutdown) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was already shutdown").toString());
				}
				//
				this.shuttingdown = true;
				// LOGGER.info(new StringBuilder().append("Shutting down
				// ").append(getDisplayName()).toString());
				// --------------------------------------------------
				this.shutdown = true;
				// --------------------------------------------------
				this.shuttingdown = false;
				this.started = false;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
				throw e;
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
			throw e;
		}
	}

	/**
	 * 是否關閉
	 */
	@Override
	public boolean isShutdown() {
		return shutdown;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
