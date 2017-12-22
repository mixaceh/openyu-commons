package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.StackCacheFactory;
import org.openyu.commons.service.supporter.BaseServiceFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StackCacheFactory工廠
 */
public final class StackCacheFactoryFactoryBean<T, U extends StackCacheFactory<T>>
		extends BaseServiceFactoryBeanSupporter<StackCacheFactory<T>> {

	private static final long serialVersionUID = -1768623650806244189L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(StackCacheFactoryFactoryBean.class);

	/**
	 * 所有屬性
	 */
	public final static String[] ALL_PROPERTIES = {};

	protected PoolableCacheFactory<T> poolableCacheFactory;

	public StackCacheFactoryFactoryBean() {
	}

	public PoolableCacheFactory<T> getPoolableCacheFactory() {
		return poolableCacheFactory;
	}

	public void setPoolableCacheFactory(PoolableCacheFactory<T> poolableCacheFactory) {
		this.poolableCacheFactory = poolableCacheFactory;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected StackCacheFactory<T> createService() throws Exception {
		StackCacheFactoryImpl<T> result = null;
		try {
			result = new StackCacheFactoryImpl<T>(poolableCacheFactory);
			// result.setPoolableCacheFactory(poolableCacheFactory);
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);

			/**
			 * extendedProperties
			 */

			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
			try {
				result = (StackCacheFactoryImpl<T>) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
