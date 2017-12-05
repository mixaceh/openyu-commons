package org.openyu.commons.commons.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.openyu.commons.service.BaseService;

public interface PoolableCacheFactory<T> extends BaseService, PoolableObjectFactory<T> {

}
