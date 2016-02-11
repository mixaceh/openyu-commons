package org.openyu.commons.commons.pool;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.openyu.commons.service.BaseService;

public interface KeyedCacheableObjectFactory<K, V> extends BaseService,
		KeyedPoolableObjectFactory<K, V> {

}
