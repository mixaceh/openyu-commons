package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.service.supporter.BaseServiceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SoftReferenceCacheFactory工廠
 */
public final class SoftReferenceCacheFactoryFactoryBean<T, U extends SoftReferenceCacheFactory<T>>
		extends BaseServiceFactorySupporter<SoftReferenceCacheFactory<T>> {

	private static final long serialVersionUID = 7441283283901230776L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(SoftReferenceCacheFactoryFactoryBean.class);

	/**
	 * 所有屬性
	 */
	public final static String[] ALL_PROPERTIES = {};

	protected CacheableObjectFactory<T> cacheableObjectFactory;

	public SoftReferenceCacheFactoryFactoryBean() {
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
	protected SoftReferenceCacheFactory<T> createService() throws Exception {
		SoftReferenceCacheFactoryImpl<T> result = null;
		try {
			result = new SoftReferenceCacheFactoryImpl<T>(cacheableObjectFactory);
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
				result = (SoftReferenceCacheFactoryImpl<T>) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
