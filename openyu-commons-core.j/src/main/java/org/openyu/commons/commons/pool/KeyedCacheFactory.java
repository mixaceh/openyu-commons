package org.openyu.commons.commons.pool;

import org.openyu.commons.commons.pool.ex.KeyedCacheException;
import org.openyu.commons.service.BaseService;

/**
 * pool-1.6, 類似 KeedObjectPoolFactory
 * 
 * pool-2.2, 原PoolableObjectFactory -> PooledObjectFactory, 多了一個wrap,
 * PooledObject來包裝原本的物件
 * 
 * 1.使用 ThreadLocal
 * 
 * 2.使用 callback
 * 
 * @param <K>
 *            key
 * @param <V>
 *            操作的物件
 * @param <R>
 *            操作的物件傳回值
 * 
 *            使用方式:
 * 
 *            1.使用openCache() 開啟cache, 要再調用closeCache()關閉cache, 讓cache返回pool
 * 
 *            2.使用execute()回呼, 不需開啟cache與關閉cache
 */
public interface KeyedCacheFactory<K, V> extends BaseService {

	/**
	 * 開啟cache, 要再調用closeCache()關閉cache, 讓cache返回pool
	 * 
	 * @param key
	 * @return
	 * @throws KeyedCacheException
	 */
	V openCache(K key) throws KeyedCacheException;

	/**
	 * 關閉cache, 讓cache返回pool
	 * 
	 * @param key
	 * @param obj
	 * @throws KeyedCacheException
	 */
	void closeCache(K key, V obj) throws KeyedCacheException;

	/**
	 * 使用回呼, 不需開啟cache與關閉cache
	 * 
	 * @param key
	 * @param action
	 * @return
	 * @throws KeyedCacheException
	 */
	Object execute(K key, KeyedCacheCallback<K, V> action)
			throws KeyedCacheException;

//	 void close() throws KeyedCacheException;
//	
//	 boolean isClosed();
//	
//	 void clear() throws KeyedCacheException;

}
