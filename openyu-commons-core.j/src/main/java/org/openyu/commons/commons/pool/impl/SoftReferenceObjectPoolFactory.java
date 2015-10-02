package org.openyu.commons.commons.pool.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.ObjectPoolFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.SoftReferenceObjectPool;

/**
 * pool-1.6 沒有 SoftReferenceObjectPoolFactory, 這倒是很奇怪
 * 
 * 所以自行添加 SoftReferenceObjectPoolFactory 來彌補
 * 
 * @param <T>
 */
public class SoftReferenceObjectPoolFactory<T> implements ObjectPoolFactory<T> {

	private PoolableObjectFactory<T> factory;

	public SoftReferenceObjectPoolFactory(PoolableObjectFactory<T> factory) {
		this.factory = factory;
	}

	public ObjectPool<T> createPool() throws IllegalStateException {
		return new SoftReferenceObjectPool<T>(factory);
	}

}
