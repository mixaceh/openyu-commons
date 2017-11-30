package org.openyu.commons.commons.pool;

import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.service.BaseService;

public interface CacheTemplate<T> extends BaseService {

	CacheFactory<T> getCacheFactory();

	void setCacheFactory(CacheFactory<T> cacheFactory);

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

}
