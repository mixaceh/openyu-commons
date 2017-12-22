package org.openyu.commons.commons.pool.impl;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.openyu.commons.commons.pool.PoolableCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.supporter.CacheFactorySupporter;
import org.openyu.commons.commons.pool.CacheFactory;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericCacheFactoryImpl<T> extends CacheFactorySupporter<T> implements GenericCacheFactory<T> {

	private static final long serialVersionUID = -5019167084750351983L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(GenericCacheFactoryImpl.class);

	/**
	 * 物件池
	 */
	private GenericObjectPool<T> objectPool;

	/**
	 * 物件池設定
	 */
	private GenericObjectPool.Config poolConfig;

	public GenericCacheFactoryImpl(PoolableCacheFactory<T> poolableCacheFactory, GenericObjectPool.Config poolConfig) {
		super(poolableCacheFactory);
		this.poolConfig = poolConfig;
	}

	public GenericCacheFactoryImpl(PoolableCacheFactory<T> poolableCacheFactory) {
		this(poolableCacheFactory, new GenericObjectPool.Config());
	}

	public GenericCacheFactoryImpl() {
		this(null, null);
	}

	// GenericObjectPool.Config
	public synchronized int getMaxActive() {
		return poolConfig.maxActive;
	}

	public synchronized void setMaxActive(int maxActive) {
		this.poolConfig.maxActive = maxActive;
	}

	public synchronized int getMaxIdle() {
		return poolConfig.maxIdle;
	}

	public synchronized void setMaxIdle(int maxIdle) {
		this.poolConfig.maxIdle = maxIdle;
	}

	public synchronized int getMinIdle() {
		return poolConfig.minIdle;
	}

	public synchronized void setMinIdle(int minIdle) {
		this.poolConfig.minIdle = minIdle;
	}

	public synchronized long getMaxWait() {
		return poolConfig.maxWait;
	}

	public synchronized void setMaxWait(long maxWait) {
		this.poolConfig.maxWait = maxWait;
	}

	public synchronized boolean isTestOnBorrow() {
		return poolConfig.testOnBorrow;
	}

	public synchronized void setTestOnBorrow(boolean testOnBorrow) {
		this.poolConfig.testOnBorrow = testOnBorrow;
	}

	public synchronized boolean isTestOnReturn() {
		return poolConfig.testOnReturn;
	}

	public synchronized void setTestOnReturn(boolean testOnReturn) {
		this.poolConfig.testOnReturn = testOnReturn;
	}

	public synchronized long getTimeBetweenEvictionRunsMillis() {
		return this.poolConfig.timeBetweenEvictionRunsMillis;
	}

	public synchronized void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.poolConfig.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public synchronized int getNumTestsPerEvictionRun() {
		return this.poolConfig.numTestsPerEvictionRun;
	}

	public synchronized void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.poolConfig.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public synchronized long getMinEvictableIdleTimeMillis() {
		return this.poolConfig.minEvictableIdleTimeMillis;
	}

	public synchronized void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.poolConfig.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public synchronized boolean isTestWhileIdle() {
		return poolConfig.testWhileIdle;
	}

	public synchronized void setTestWhileIdle(boolean testWhileIdle) {
		this.poolConfig.testWhileIdle = testWhileIdle;
	}

	public synchronized byte getWhenExhaustedAction() {
		return poolConfig.whenExhaustedAction;
	}

	public synchronized void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.poolConfig.whenExhaustedAction = whenExhaustedAction;
	}

	public synchronized long getSoftMinEvictableIdleTimeMillis() {
		return poolConfig.softMinEvictableIdleTimeMillis;
	}

	public synchronized void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
		this.poolConfig.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public synchronized boolean isLifo() {
		return poolConfig.lifo;
	}

	public synchronized void setLifo(boolean lifo) {
		this.poolConfig.lifo = lifo;
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
		GenericObjectPool<T> objectPool = new GenericObjectPool<T>(getPoolableCacheFactory());
		objectPool.setConfig(poolConfig);
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
