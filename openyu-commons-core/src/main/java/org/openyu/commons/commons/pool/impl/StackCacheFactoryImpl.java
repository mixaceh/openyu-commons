package org.openyu.commons.commons.pool.impl;

import org.apache.commons.pool.impl.StackObjectPoolFactory;
import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.StackCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackCacheFactoryImpl<T> extends PoolingCacheFactory<T>
		implements StackCacheFactory<T> {

	private static final long serialVersionUID = -5019167084750351983L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(StackCacheFactoryImpl.class);

	public StackCacheFactoryImpl(
			PoolableCacheFactory<T> cacheableObjectFactory) {
		super(cacheableObjectFactory);
	}

	public StackCacheFactoryImpl() {
	}

//	/**
//	 * new建構
//	 * 
//	 * remove to StackCacheFactoryFactoryBean.createService()
//	 * 
//	 * @return
//	 */
//	public static <T> StackCacheFactory<T> createInstance(
//			CacheableObjectFactory<T> cacheableObjectFactory) {
//		StackCacheFactoryImpl<T> result = null;
//		try {
//			result = new StackCacheFactoryImpl<T>(cacheableObjectFactory);
//			result.setCreateInstance(true);
//			// 啟動
//			result.start();
//		} catch (Exception e) {
//			LOGGER.error(
//					new StringBuilder().append(
//							"Exception encountered during createInstance()")
//							.toString(), e);
//			result = (StackCacheFactoryImpl<T>) shutdownInstance(result);
//		}
//		return result;
//	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		super.doStart();
		//
		// this.objectPool = new StackObjectPool<T>(cacheableObjectFactory);
		this.objectPoolFactory = new StackObjectPoolFactory<T>(poolableCacheFactory);
		this.objectPool = objectPoolFactory.createPool();
	}

}
