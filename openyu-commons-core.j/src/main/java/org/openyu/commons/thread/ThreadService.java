package org.openyu.commons.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.openyu.commons.service.BaseService;

/**
 * 線程服務
 */
public interface ThreadService extends BaseService {

	int getMaxExecutorSize();

	void setMaxExecutorSize(int maxExecutorSize);

	int getCorePoolSize();

	void setCorePoolSize(int corePoolSize);

	int getKeepAliveSeconds();

	void setKeepAliveSeconds(int keepAliveSeconds);

	int getMaxPoolSize();

	void setMaxPoolSize(int maxPoolSize);

	int getQueueCapacity();

	void setQueueCapacity(int queueCapacity);

	/**
	 * 提交 Callable
	 * 
	 * @param callable
	 * @return
	 */
	Future<?> submit(Callable<?> callable);

	/**
	 * 提交 Runnable
	 * 
	 * @param runnable
	 * @return
	 */
	Future<?> submit(Runnable runnable);
}
