package org.openyu.commons.commons.pool.impl;

import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.CacheFactory;
import org.openyu.commons.commons.pool.CacheTemplate;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.ex.CacheTemplateException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;

public class CacheTemplateImpl<T> extends BaseServiceSupporter implements CacheTemplate<T> {

	private static final long serialVersionUID = -567329326929278199L;

	private CacheFactory<T> cacheFactory;

	public CacheTemplateImpl(CacheFactory<T> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	public CacheTemplateImpl() {
		this(null);
	}

	@Override
	public CacheFactory<T> getCacheFactory() {
		return cacheFactory;
	}

	@Override
	public void setCacheFactory(CacheFactory<T> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	@Override
	public T openCache() throws CacheException {
		try {
			return cacheFactory.openCache();
		} catch (Exception ex) {
			throw new CacheTemplateException("Could not open Cache", ex);
		}
	}

	@Override
	public void closeCache() throws CacheException {
		try {
			cacheFactory.closeCache();
		} catch (Exception ex) {
			throw new CacheTemplateException("Could not close Cache", ex);
		}
	}

	@Override
	public Object execute(CacheCallback<T> action) throws CacheException {
		return doExecute(action);
	}

	protected Object doExecute(CacheCallback<T> action) throws CacheException {
		Object result = null;
		//
		AssertHelper.notNull(action, "The CacheCallback must not be null");
		//
		T cache = null;
		try {
			cache = openCache();
			result = action.doInAction(cache);
			return result;
		} catch (CacheException e) {
			throw new CacheTemplateException(e);
		} catch (Throwable e) {
			throw new CacheTemplateException(e);
		} finally {
			if (cache != null) {
				closeCache();
			}
		}
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
}
