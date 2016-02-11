package org.openyu.commons.commons.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.openyu.commons.service.BaseService;

public interface CacheableObjectFactory<T> extends BaseService,
		PoolableObjectFactory<T> {

}
