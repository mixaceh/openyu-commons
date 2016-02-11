package org.openyu.commons.commons.pool;

import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.service.BaseService;

/**
 * pool-1.6, 類似 ObjectPoolFactory
 * 
 * pool-2.2, 原PoolableObjectFactory -> PooledObjectFactory, 多了一個wrap,
 * PooledObject來包裝原本的物件
 * 
 * 1.使用 ThreadLocal
 * 
 * 2.使用 callback
 * 
 * @param <T>
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
public interface CacheFactory<T> extends BaseService {

	/**
	 * 開啟cache, 要再調用closeCache()關閉cache, 讓cache返回pool
	 * 
	 * @return
	 * @throws CacheException
	 */
	T openCache() throws CacheException;

	/**
	 * 關閉cache, 讓cache返回pool
	 * 
	 * @throws CacheException
	 */
	void closeCache() throws CacheException;

	/**
	 * 使用回呼, 不需開啟cache與關閉cache
	 * 
	 * @param action
	 * @return
	 * @throws CacheException
	 */
	Object execute(CacheCallback<T> action) throws CacheException;

	/**
	 * 關閉
	 * 
	 * @throws CacheException
	 */
	void close() throws CacheException;


	/**
	 * 清除pool裡所有物件
	 * 
	 * @throws CacheException
	 */
	void clear() throws CacheException;

	/**
	 * 是否已清除
	 * 
	 * @return
	 */
	boolean isCleared();

}
