package org.openyu.commons.commons.pool.supporter;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.openyu.commons.commons.pool.KeyedCacheCallback;
import org.openyu.commons.commons.pool.KeyedCacheFactory;
import org.openyu.commons.commons.pool.KeyedCacheableObjectFactory;
import org.openyu.commons.commons.pool.ex.KeyedCacheException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;

public abstract class KeyedCacheFactorySupporter<K, V> extends
		BaseServiceSupporter implements KeyedCacheFactory<K, V> {

	private static final long serialVersionUID = -6347898991888534081L;

	protected KeyedObjectPoolFactory<K, V> keyedObjectPoolFactory;

	protected KeyedCacheableObjectFactory<K, V> keyedCacheableObjectFactory;

	// protected Map<K, KeyedObjectPool<K, V>> objectPools = new
	// ConcurrentHashMap<K, KeyedObjectPool<K, V>>();
	// protected KeyedObjectPool<K, V> objectPool;

	protected ThreadLocal<KeyedObjectPool<K, V>> keyedObjectPoolHolder = new ThreadLocal<KeyedObjectPool<K, V>>();

	protected boolean closeFactory = false;

	public KeyedCacheFactorySupporter(
			KeyedCacheableObjectFactory<K, V> cacheableObjectFactory) {
		this.keyedCacheableObjectFactory = cacheableObjectFactory;
	}

	public KeyedCacheFactorySupporter() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(keyedCacheableObjectFactory,
				"KeyedCacheableObjectFactory is required");
	}

	// protected KeyedObjectPool<K, V> getObjectPool(final K key) {
	// KeyedObjectPool<K, V> pool;
	// synchronized (objectPools) {
	// pool = objectPools.get(key);
	// if (pool == null) {
	// pool = new GenericKeyedObjectPool<K, V>();
	// objectPools.put(key, pool);
	// }
	// }
	// return pool;
	// }

	// public Object borrowObject(final Object key) throws Exception {
	// assertOpen();
	// final ObjectPool pool = getObjectPool(key);
	// try {
	// if (keys != null) {
	// keys.set(key);
	// }
	// return pool.borrowObject();
	// } finally {
	// if (keys != null) {
	// keys.set(null); // unset key
	// }
	// }
	// }

	public V openCache(K key) throws KeyedCacheException {
		V result = null;
		try {
			KeyedObjectPool<K, V> keyedObjectPool = keyedObjectPoolHolder.get();
			if (keyedObjectPool == null) {
				keyedObjectPool = keyedObjectPoolFactory.createPool();
				keyedObjectPoolHolder.set(keyedObjectPool);
			}
			//
			result = keyedObjectPool.borrowObject(key);
		} catch (Exception ex) {
			throw new KeyedCacheException("Could not open Cache", ex);
		}
		return result;
	}

	public void closeCache(K key, V obj) {
		try {
			KeyedObjectPool<K, V> keyedObjectPool = keyedObjectPoolHolder.get();
			keyedObjectPoolHolder.set(null);
			if (keyedObjectPool != null) {
				//
				keyedObjectPool.returnObject(key, obj);
			}
		} catch (Exception ex) {
			throw new KeyedCacheException("Could not close Cache", ex);
		}
	}

	public Object execute(K key, KeyedCacheCallback<K, V> action)
			throws KeyedCacheException {
		return doExecute(key, action);
	}

	protected Object doExecute(K key, KeyedCacheCallback<K, V> action)
			throws KeyedCacheException {
		Object result = null;
		//
		AssertHelper.notNull(action, "Callback object must not be null");
		//
		V obj = null;
		try {
			obj = openCache(key);
			result = action.doInAction(key, obj);
			return result;
		} catch (Exception ex) {
			throw new KeyedCacheException(ex);
		} finally {
			if (obj != null) {
				closeCache(key, obj);
			}
		}
	}

	// public synchronized void close() throws KeyedCacheException {
	// if (isClosed()) {
	// throw new KeyedCacheException("CacheFactory was already closed");
	// }
	// //
	// try {
	// // TODO cacheHolder 未清
	// this.closed = true;
	// ObjectPool<T> oldpool = this.objectPool;
	// this.objectPool = null;
	// if (oldpool != null)
	// oldpool.close();
	// } catch (Exception ex) {
	// throw new KeyedCacheException("Cannot close pool", ex);
	// }
	// }
	//
	// public synchronized boolean isClosed() {
	// return this.closed;
	// }
	//
	// public synchronized void clear() throws KeyedCacheException {
	// try {
	// // TODO cacheHolder 未清
	// objectPool.clear();
	// } catch (Exception ex) {
	// throw new KeyedCacheException("Cannot clear pool", ex);
	// }
	// }
	//
	// public synchronized int getNumIdle() {
	// return objectPool.getNumIdle();
	// }
	//
	// public synchronized int getNumActive() {
	// return objectPool.getNumActive();
	// }

}
