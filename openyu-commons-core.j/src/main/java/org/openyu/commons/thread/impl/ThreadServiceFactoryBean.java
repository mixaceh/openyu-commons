package org.openyu.commons.thread.impl;

import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadService工廠
 */
public final class ThreadServiceFactoryBean<T> extends BaseFactorySupporter<ThreadService> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ThreadServiceFactoryBean.class);

	private ThreadService threadService;

	public ThreadServiceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected ThreadService createInstance() throws Exception {
		ThreadServiceImpl result = null;
		try {
			result = new ThreadServiceImpl();
			result.setCreateInstance(true);

			/**
			 * extendedProperties
			 */
			result.setMaxExecutorSize(
					extendedProperties.getInt("maxExecutorSize", ThreadServiceImpl.DEFAULT_MAX_EXECUTOR_SIZE));
			result.setCorePoolSize(extendedProperties.getInt("corePoolSize", ThreadServiceImpl.DEFAULT_CORE_POOL_SIZE));
			result.setKeepAliveSeconds(
					extendedProperties.getInt("keepAliveSeconds", ThreadServiceImpl.DEFAULT_KEEP_ALIVE_SECONDS));
			result.setMaxPoolSize(extendedProperties.getInt("maxPoolSize", ThreadServiceImpl.DEFAULT_MAX_POOL_SIZE));
			result.setQueueCapacity(
					extendedProperties.getInt("queueCapacity", ThreadServiceImpl.DEFAULT_QUEUE_CAPACITY));

			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			try {
				result = (ThreadServiceImpl) shutdownInstance();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected ThreadService shutdownInstance() throws Exception {
		try {
			if (this.threadService != null) {
				ThreadService oldInstance = this.threadService;
				oldInstance.shutdown();
				this.threadService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
			throw e;
		}
		return this.threadService;

	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected ThreadService restartInstance() throws Exception {
		try {
			if (this.threadService != null) {
				ThreadService oldInstance = this.threadService;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
			throw e;
		}
		return this.threadService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.threadService = createInstance();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.threadService = shutdownInstance();
	}

	@Override
	protected void doRestart() throws Exception {
		this.threadService = restartInstance();
	}

	@Override
	public ThreadService getObject() throws Exception {
		return threadService;
	}

	@Override
	public Class<? extends ThreadService> getObjectType() {
		return ((this.threadService != null) ? this.threadService.getClass() : ThreadService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
