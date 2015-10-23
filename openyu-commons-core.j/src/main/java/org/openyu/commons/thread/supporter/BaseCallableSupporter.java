package org.openyu.commons.thread.supporter;

import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.BaseCallable;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.util.AssertHelper;

/**
 * 1.使用 ThreadService
 *
 * 2.或是 ExecutorService
 */
public abstract class BaseCallableSupporter<V> implements BaseCallable<V>, Supporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseCallableSupporter.class);

	private transient ThreadService threadService;

	private transient ExecutorService executorService;

	private boolean shutdown;

	public BaseCallableSupporter(ThreadService threadService) {
		this.threadService = threadService;
	}

	public BaseCallableSupporter(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public V call() {
		try {
			LOGGER.info(new StringBuilder().append("Calling ").append("T[" + Thread.currentThread().getId() + "] ")
					.append(ClassHelper.getSimpleName(getClass())).append(" @" + Integer.toHexString(hashCode()))
					.toString());
			// --------------------------------------------------
			return doCall();
			// --------------------------------------------------
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during run()").toString(), e);
		}
		//
		if (this.shutdown) {
			LOGGER.info(new StringBuilder().append("Interrupted ").append("T[" + Thread.currentThread().getId() + "] ")
					.append(ClassHelper.getSimpleName(getClass())).append(" @" + Integer.toHexString(hashCode()))
					.toString());
		}
		return null;
	}

	/**
	 * 內部執行
	 */
	protected abstract V doCall() throws Exception;

	/**
	 * 啟動
	 */
	public void start() {
		if (threadService != null) {
			this.shutdown = false;
			this.threadService.submit(this);
		} else if (executorService != null) {
			this.shutdown = false;
			this.executorService.submit(this);
		} else {
			AssertHelper.notNull(null, "The ThreadService or ExecutorService must not be null");
		}
	}

	/**
	 * 關閉
	 */
	public void shutdown() {
		this.shutdown = true;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
