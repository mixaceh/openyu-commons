package org.openyu.commons.commons.pool.supporter;

import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.impl.PoolingCacheFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.commons.pool.CacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CacheFactorySupporter<T> extends BaseServiceSupporter implements CacheFactory<T> {

	private static final long serialVersionUID = -9010088794083429006L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(CacheFactorySupporter.class);

	/**
	 * 初始大小
	 */
	private int initialSize = 1;

	/**
	 * 是否需重啟
	 */
	private volatile boolean restartNeeded;

	/**
	 * 副本
	 */
	protected volatile PoolingCacheFactory<T> instance;

	/**
	 * 可池化工廠
	 */
	private PoolableCacheFactory<T> poolableCacheFactory;

	public CacheFactorySupporter(PoolableCacheFactory<T> poolableCacheFactory) {
		this.poolableCacheFactory = poolableCacheFactory;
	}

	public CacheFactorySupporter() {
		this(null);
	}

	public PoolableCacheFactory<T> getPoolableCacheFactory() {
		return poolableCacheFactory;
	}

	public void setPoolableCacheFactory(PoolableCacheFactory<T> poolableCacheFactory) {
		this.poolableCacheFactory = poolableCacheFactory;
	}

	public synchronized int getInitialSize() {
		return this.initialSize;
	}

	public synchronized void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
		this.restartNeeded = true;
	}

	public synchronized boolean isRestartNeeded() {
		return this.restartNeeded;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(poolableCacheFactory, "PoolableCacheFactory must not be null");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		close();
	}

	@Override
	public T openCache() throws CacheException {
		return createCacheFactory().openCache();
	}

	@Override
	public void closeCache() throws CacheException {
		if (this.instance != null) {
			this.instance.closeCache();
		}
	}

	@Override
	public synchronized void close() throws CacheException {
		if (this.instance != null) {
			this.instance.close();
			//
			this.instance = null;
		}
	}

	@Override
	public synchronized boolean isClosed() {
		if (this.instance != null) {
			return this.instance.isClosed();
		}
		return false;
	}

	@Override
	public synchronized void clear() throws CacheException {
		if (this.instance != null) {
			this.instance.close();
			//
			this.instance = null;
		}
	}

	@Override
	public synchronized boolean isCleared() {
		if (this.instance != null) {
			return this.instance.isClosed();
		}
		return false;
	}

	public synchronized int getNumIdle() {
		if (this.instance != null) {
			return instance.getNumIdle();
		}
		return 0;
	}

	public synchronized int getNumActive() {
		if (this.instance != null) {
			return instance.getNumActive();
		}
		return 0;
	}

	/**
	 * 建立工廠
	 * 
	 * @return
	 * @throws CacheException
	 */
	protected abstract CacheFactory<T> createCacheFactory() throws CacheException;

	/**
	 * 建立物件池
	 * 
	 * GenericObjectPool
	 */
	protected abstract void createObjectPool();

	/**
	 * 建立副本
	 * 
	 * @throws CacheException
	 */
	protected abstract void createInstance();

	/**
	 * 檢驗可池化工廠
	 * 
	 * @param poolableCacheFactory
	 * @throws Exception
	 */
	protected void validatePoolableFactory() throws Exception {
		T object = null;
		try {
			object = this.poolableCacheFactory.makeObject();
			this.poolableCacheFactory.activateObject(object);
			this.poolableCacheFactory.passivateObject(object);
		} finally {
			this.poolableCacheFactory.destroyObject(object);
		}
	}

}
