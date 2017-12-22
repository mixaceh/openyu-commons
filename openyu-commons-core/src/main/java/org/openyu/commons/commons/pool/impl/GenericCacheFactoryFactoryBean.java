package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.openyu.commons.service.supporter.BaseServiceFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GenericCacheFactory工廠
 */
public final class GenericCacheFactoryFactoryBean<T, U extends GenericCacheFactory<T>>
		extends BaseServiceFactoryBeanSupporter<GenericCacheFactory<T>> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(GenericCacheFactoryFactoryBean.class);

	/**
	 * 所有屬性
	 */
	public final static String[] ALL_PROPERTIES = {};

	protected PoolableCacheFactory<T> poolableCacheFactory;

	public GenericCacheFactoryFactoryBean() {
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
	protected GenericCacheFactory<T> createService() throws Exception {
		GenericCacheFactoryImpl<T> result = null;
		try {
			result = new GenericCacheFactoryImpl<T>(poolableCacheFactory);
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
				result = (GenericCacheFactoryImpl<T>) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
