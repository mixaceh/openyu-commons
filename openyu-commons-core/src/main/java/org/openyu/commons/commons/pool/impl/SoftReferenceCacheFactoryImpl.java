package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.commons.pool.supporter.CacheFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftReferenceCacheFactoryImpl<T> extends CacheFactorySupporter<T>
		implements SoftReferenceCacheFactory<T> {

	private static final long serialVersionUID = 8416281904106156439L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(SoftReferenceCacheFactoryImpl.class);

	public SoftReferenceCacheFactoryImpl(
			CacheableObjectFactory<T> cacheableObjectFactory) {
		super(cacheableObjectFactory);
	}

	public SoftReferenceCacheFactoryImpl() {
		this(null);
	}

//	/**
//	 * new建構
//	 * remove to SoftReferenceCacheFactoryFactoryBean.createService()
//	 * 
//	 * @return
//	 */
//	public static <T> SoftReferenceCacheFactory<T> createInstance(
//			CacheableObjectFactory<T> cacheableObjectFactory) {
//		SoftReferenceCacheFactoryImpl<T> result = null;
//		try {
//			result = new SoftReferenceCacheFactoryImpl<T>(
//					cacheableObjectFactory);
//			result.setCreateInstance(true);
//			// 啟動
//			result.start();
//		} catch (Exception e) {
//			LOGGER.error(
//					new StringBuilder().append(
//							"Exception encountered during createInstance()")
//							.toString(), e);
//			result = (SoftReferenceCacheFactoryImpl<T>) shutdownInstance(result);
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
		this.objectPoolFactory = new SoftReferenceObjectPoolFactory<T>(
				cacheableObjectFactory);
		//
		this.objectPool = objectPoolFactory.createPool();
	}
}
