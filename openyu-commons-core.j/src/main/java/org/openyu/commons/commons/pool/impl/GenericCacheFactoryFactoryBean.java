package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.openyu.commons.service.supporter.BaseServiceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GenericCacheFactory工廠
 */
public final class GenericCacheFactoryFactoryBean<T, U extends GenericCacheFactory<T>>
		extends BaseServiceFactorySupporter<GenericCacheFactory<T>> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(GenericCacheFactoryFactoryBean.class);

	/**
	 * 所有屬性
	 */
	public final static String[] ALL_PROPERTIES = {};

	protected CacheableObjectFactory<T> cacheableObjectFactory;

	public GenericCacheFactoryFactoryBean() {
	}

	public CacheableObjectFactory<T> getCacheableObjectFactory() {
		return cacheableObjectFactory;
	}

	public void setCacheableObjectFactory(CacheableObjectFactory<T> cacheableObjectFactory) {
		this.cacheableObjectFactory = cacheableObjectFactory;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected GenericCacheFactory<T> createService() throws Exception {
		GenericCacheFactoryImpl<T> result = null;
		try {
			result = new GenericCacheFactoryImpl<T>(cacheableObjectFactory);
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
