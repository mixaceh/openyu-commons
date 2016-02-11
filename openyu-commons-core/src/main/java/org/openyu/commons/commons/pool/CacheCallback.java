package org.openyu.commons.commons.pool;

import org.openyu.commons.commons.pool.ex.CacheException;

public interface CacheCallback<T> {

	Object doInAction(T obj) throws CacheException;
}
