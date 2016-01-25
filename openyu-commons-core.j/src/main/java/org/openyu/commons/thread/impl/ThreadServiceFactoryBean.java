package org.openyu.commons.thread.impl;

import org.openyu.commons.service.supporter.BaseServiceFactoryBeanSupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadService工廠
 */
public final class ThreadServiceFactoryBean<T extends ThreadService>
		extends BaseServiceFactoryBeanSupporter<ThreadService> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ThreadServiceFactoryBean.class);

	public static final String MAX_EXECUTOR_SIZE = "maxExecutorSize";
	/**
	 * 預設executor的最大數目
	 */
	public static final int DEFAULT_MAX_EXECUTOR_SIZE = 1;

	public static final String CORE_POOL_SIZE = "corePoolSize";

	public static final int DEFAULT_CORE_POOL_SIZE = 8;

	public static final String KEEP_ALIVE_SECONDS = "keepAliveSeconds";

	public static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;

	public static final String MAX_POOL_SIZE = "maxPoolSize";

	/**
	 * 預設thread的最大數目
	 */
	public static final int DEFAULT_MAX_POOL_SIZE = 8;

	public static final String QUEUE_CAPACITY = "queueCapacity";

	public static final int DEFAULT_QUEUE_CAPACITY = 8;

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = { MAX_EXECUTOR_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_SECONDS,
			MAX_POOL_SIZE, QUEUE_CAPACITY };

	public ThreadServiceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected ThreadService createService() throws Exception {
		ThreadServiceImpl result = null;
		try {
			result = new ThreadServiceImpl();
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);
			/**
			 * extendedProperties
			 */
			result.setMaxExecutorSize(extendedProperties.getInt(MAX_EXECUTOR_SIZE, DEFAULT_MAX_EXECUTOR_SIZE));
			result.setCorePoolSize(extendedProperties.getInt(CORE_POOL_SIZE, DEFAULT_CORE_POOL_SIZE));
			result.setKeepAliveSeconds(extendedProperties.getInt(KEEP_ALIVE_SECONDS, DEFAULT_KEEP_ALIVE_SECONDS));
			result.setMaxPoolSize(extendedProperties.getInt(MAX_POOL_SIZE, DEFAULT_MAX_POOL_SIZE));
			result.setQueueCapacity(extendedProperties.getInt(QUEUE_CAPACITY, DEFAULT_QUEUE_CAPACITY));

			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
			try {
				result = (ThreadServiceImpl) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
