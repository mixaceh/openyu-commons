package org.openyu.commons.commons.pool.supporter;

import org.openyu.commons.commons.pool.KeyedCacheableObjectFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public abstract class KeyedCacheableObjectFactorySupporter<K, V> extends
		BaseServiceSupporter implements KeyedCacheableObjectFactory<K, V> {

	private static final long serialVersionUID = 1306852547323899698L;

	public KeyedCacheableObjectFactorySupporter() {

	}

	/**
	 * 建構object
	 * 
	 * @param key
	 */
	public abstract V makeObject(K key) throws Exception;

	public void destroyObject(K key, V obj) throws Exception {
	}

	public boolean validateObject(K key, V obj) {
		return true;
	}

	public void activateObject(K key, V obj) throws Exception {
	}

	public void passivateObject(K key, V obj) throws Exception {
	}

}
