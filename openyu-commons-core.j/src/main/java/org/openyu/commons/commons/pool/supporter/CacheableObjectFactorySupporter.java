package org.openyu.commons.commons.pool.supporter;

import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public abstract class CacheableObjectFactorySupporter<T> extends
		BaseServiceSupporter implements CacheableObjectFactory<T> {

	private static final long serialVersionUID = 2017177244010565594L;

	public CacheableObjectFactorySupporter() {

	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	/**
	 * 建構object
	 */
	public abstract T makeObject() throws Exception;

	public void destroyObject(T obj) throws Exception {

	}

	public boolean validateObject(T obj) {
		return true;
	}

	public void activateObject(T obj) throws Exception {

	}

	public void passivateObject(T obj) throws Exception {
	}
}
