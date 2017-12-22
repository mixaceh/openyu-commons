package org.openyu.commons.commons.pool.impl;

import org.apache.commons.pool.impl.StackObjectPool;
import org.openyu.commons.commons.pool.CacheFactory;
import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.StackCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.supporter.CacheFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackCacheFactoryImpl<T> extends CacheFactorySupporter<T> implements StackCacheFactory<T> {

	private static final long serialVersionUID = 2226617146581060878L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(StackCacheFactoryImpl.class);

	/**
	 * 物件池
	 */
	private StackObjectPool<T> objectPool;

	public StackCacheFactoryImpl(PoolableCacheFactory<T> poolableCacheFactory) {
		super(poolableCacheFactory);
	}

	public StackCacheFactoryImpl() {
		this(null);
	}

	/**
	 * 建立工廠
	 * 
	 * @return
	 * @throws CacheException
	 */
	@Override
	protected synchronized CacheFactory<T> createCacheFactory() throws CacheException {
		if (this.instance != null) {
			if (this.instance.isClosed()) {
				throw new CacheException("PoolingCacheFactory was already closed");
			}
			return this.instance;
		}
		// 建立物件池
		createObjectPool();
		// 建立副本
		createInstance();
		//
		try {
			// 檢驗可池化工廠
			validatePoolableFactory();
			// 初始大小
			for (int i = 0; i < getInitialSize(); ++i) {
				this.objectPool.addObject();
			}
		} catch (Exception e) {
			throw new CacheException("Error preloading the pool", e);
		}
		//
		LOGGER.info("Create CacheFactory");
		return this.instance;
	}

	/**
	 * 建立物件池
	 */
	@Override
	protected void createObjectPool() {
		// 設定factory
		StackObjectPool<T> objectPool = new StackObjectPool<T>(getPoolableCacheFactory());
		this.objectPool = objectPool;
	}

	/**
	 * 建立副本
	 * 
	 * @throws CacheException
	 */
	@Override
	protected void createInstance() throws CacheException {
		this.instance = new PoolingCacheFactory<T>(this.objectPool);
	}

}
