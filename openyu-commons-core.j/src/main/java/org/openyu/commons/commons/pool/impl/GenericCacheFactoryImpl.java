package org.openyu.commons.commons.pool.impl;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.commons.pool.GenericCacheFactory;
import org.openyu.commons.commons.pool.supporter.CacheFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericCacheFactoryImpl<T> extends CacheFactorySupporter<T>
		implements GenericCacheFactory<T> {

	private static final long serialVersionUID = -5019167084750351983L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(GenericCacheFactoryImpl.class);

	private GenericObjectPool.Config config;

	public GenericCacheFactoryImpl(
			CacheableObjectFactory<T> cacheableObjectFactory,
			GenericObjectPool.Config config) {
		this.cacheableObjectFactory = cacheableObjectFactory;
		this.config = config;
	}

	public GenericCacheFactoryImpl(
			CacheableObjectFactory<T> cacheableObjectFactory) {
		this(cacheableObjectFactory, new GenericObjectPool.Config());
	}

	public GenericCacheFactoryImpl() {
		this(null, null);
	}

	// GenericObjectPool.Config
	public synchronized int getMaxActive() {
		return config.maxActive;
	}

	public synchronized void setMaxActive(int maxActive) {
		this.config.maxActive = maxActive;
	}

	public synchronized int getMaxIdle() {
		return config.maxIdle;
	}

	public synchronized void setMaxIdle(int maxIdle) {
		this.config.maxIdle = maxIdle;
	}

	public synchronized int getMinIdle() {
		return config.minIdle;
	}

	public synchronized void setMinIdle(int minIdle) {
		this.config.minIdle = minIdle;
	}

	public synchronized long getMaxWait() {
		return config.maxWait;
	}

	public synchronized void setMaxWait(long maxWait) {
		this.config.maxWait = maxWait;
	}

	public synchronized boolean isTestOnBorrow() {
		return config.testOnBorrow;
	}

	public synchronized void setTestOnBorrow(boolean testOnBorrow) {
		this.config.testOnBorrow = testOnBorrow;
	}

	public synchronized boolean isTestOnReturn() {
		return config.testOnReturn;
	}

	public synchronized void setTestOnReturn(boolean testOnReturn) {
		this.config.testOnReturn = testOnReturn;
	}

	public synchronized long getTimeBetweenEvictionRunsMillis() {
		return this.config.timeBetweenEvictionRunsMillis;
	}

	public synchronized void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		this.config.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public synchronized int getNumTestsPerEvictionRun() {
		return this.config.numTestsPerEvictionRun;
	}

	public synchronized void setNumTestsPerEvictionRun(
			int numTestsPerEvictionRun) {
		this.config.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public synchronized long getMinEvictableIdleTimeMillis() {
		return this.config.minEvictableIdleTimeMillis;
	}

	public synchronized void setMinEvictableIdleTimeMillis(
			long minEvictableIdleTimeMillis) {
		this.config.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public synchronized boolean isTestWhileIdle() {
		return config.testWhileIdle;
	}

	public synchronized void setTestWhileIdle(boolean testWhileIdle) {
		this.config.testWhileIdle = testWhileIdle;
	}

	public synchronized byte getWhenExhaustedAction() {
		return config.whenExhaustedAction;
	}

	public synchronized void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.config.whenExhaustedAction = whenExhaustedAction;
	}

	public synchronized long getSoftMinEvictableIdleTimeMillis() {
		return config.softMinEvictableIdleTimeMillis;
	}

	public synchronized void setSoftMinEvictableIdleTimeMillis(
			long softMinEvictableIdleTimeMillis) {
		this.config.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public synchronized boolean isLifo() {
		return config.lifo;
	}

	public synchronized void setLifo(boolean lifo) {
		this.config.lifo = lifo;
	}

	/**
	 * new建構
	 * 
	 * @return
	 */
	public static <T> GenericCacheFactory<T> createInstance(
			CacheableObjectFactory<T> cacheableObjectFactory,
			GenericObjectPool.Config config) {
		GenericCacheFactoryImpl<T> result = null;
		try {
			result = new GenericCacheFactoryImpl<T>(cacheableObjectFactory,
					config);
			result.setCreateInstance(true);
			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(
					new StringBuilder().append(
							"Exception encountered during createInstance()")
							.toString(), e);
			result = (GenericCacheFactoryImpl<T>) shutdownInstance(result);
		}
		return result;
	}

	public static <T> GenericCacheFactory<T> createInstance(
			CacheableObjectFactory<T> cacheableObjectFactory) {
		return createInstance(cacheableObjectFactory,
				new GenericObjectPool.Config());
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		super.doStart();
		//
		this.objectPoolFactory = new GenericObjectPoolFactory<T>(
				cacheableObjectFactory, config);
		//
		this.objectPool = objectPoolFactory.createPool();
	}

}
