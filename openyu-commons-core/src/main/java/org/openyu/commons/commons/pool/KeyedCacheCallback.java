package org.openyu.commons.commons.pool;

import org.openyu.commons.commons.pool.ex.KeyedCacheException;

public interface KeyedCacheCallback<K, V> {

	Object doInAction(K key, V obj) throws KeyedCacheException;
}
