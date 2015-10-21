package org.openyu.commons.thread.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openyu.commons.thread.BaseRunnableQueue;
import org.openyu.commons.thread.RunnableQueueGroup;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnableQueueGroupImpl<E> implements RunnableQueueGroup<E> {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(RunnableQueueGroupImpl.class);

	private BaseRunnableQueue<E> queues[];

	private int queueSize;

	private AtomicInteger counter = new AtomicInteger(0);

	private transient boolean starting;

	private transient boolean started;

	private transient boolean shuttingdown;

	private transient boolean shutdown;

	private transient String displayName;

	/**
	 * start/shutdown lock
	 */
	protected transient final Lock lock = new ReentrantLock();

	public RunnableQueueGroupImpl(BaseRunnableQueue<E>[] queues) {
		this.queues = queues;
		this.queueSize = (this.queues != null ? this.queues.length : 0);
	}

	public RunnableQueueGroupImpl() {
		this(null);
	}

	@Override
	public BaseRunnableQueue<E>[] getQueues() {
		return queues;
	}

	@Override
	public void setQueues(BaseRunnableQueue<E>[] queues) {
		if (this.started) {
			throw new IllegalStateException(new StringBuilder().append(getDisplayName())
					.append(" was already started. Must shutdown and setQueues()").toString());
		}
		//
		this.queues = queues;
		this.queueSize = (this.queues != null ? this.queues.length : 0);
	}

	protected BaseRunnableQueue<E> getNextQueue() {
		if (Integer.MAX_VALUE == this.counter.get()) {
			this.counter.set(0);
		}
		int index = this.counter.getAndIncrement() % this.queueSize;
		return this.queues[index];
	}

	public boolean offer(E e) throws Exception {
		return getNextQueue().offer(e);
	}

	protected String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(getClass().getSimpleName());
			buff.append(" @" + Integer.toHexString(hashCode()));
			displayName = buff.toString();
		}
		return displayName;
	}

	/**
	 * 啟動
	 */
	@Override
	public void start() throws Exception {
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
				AssertHelper.notNull(queues, "The Queues must not be null");
				//
				this.starting = true;
				LOGGER.info(new StringBuilder().append("Starting ").append(getDisplayName()).toString());
				// --------------------------------------------------
				for (BaseRunnableQueue<E> queue : queues) {
					queue.start();
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
	public void shutdown() throws Exception {
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
				LOGGER.info(new StringBuilder().append("Shutting down ").append(getDisplayName()).toString());
				// --------------------------------------------------
				for (BaseRunnableQueue<E> queue : queues) {
					queue.shutdown();
				}
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

}
