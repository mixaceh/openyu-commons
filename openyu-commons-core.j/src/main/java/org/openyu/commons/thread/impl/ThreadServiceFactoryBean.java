package org.openyu.commons.thread.impl;

import org.openyu.commons.service.supporter.BaseServiceFactorySupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadService工廠
 */
public final class ThreadServiceFactoryBean<T extends ThreadService>
		extends BaseServiceFactorySupporter<ThreadService> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ThreadServiceFactoryBean.class);

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
}
