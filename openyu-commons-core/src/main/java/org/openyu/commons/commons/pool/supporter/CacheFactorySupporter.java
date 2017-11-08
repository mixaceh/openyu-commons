package org.openyu.commons.commons.pool.supporter;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.ObjectPoolFactory;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.CacheFactory;
import org.openyu.commons.commons.pool.CacheableObjectFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CacheFactorySupporter<T> extends BaseServiceSupporter implements CacheFactory<T> {

	private static final long serialVersionUID = 34344492443615151L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(CacheFactorySupporter.class);

	protected ObjectPoolFactory<T> objectPoolFactory;

	protected ObjectPool<T> objectPool;

	protected CacheableObjectFactory<T> cacheableObjectFactory;

	protected transient ThreadLocal<T> cacheHolder = new ThreadLocal<T>();

	/**
	 * 是否已關閉
	 */
	private boolean closed;

	/**
	 * 是否已清除
	 */
	private boolean cleared;

	public CacheFactorySupporter(CacheableObjectFactory<T> cacheableObjectFactory) {
		this.cacheableObjectFactory = cacheableObjectFactory;
	}

	public CacheFactorySupporter() {
		this(null);
	}

	/**
	 * 關閉
	 * 
	 * @return
	 */
	public static <T> CacheFactory<T> shutdownInstance(CacheFactory<T> cacheFactory) {
		try {
			if (cacheFactory instanceof CacheFactory) {
				CacheFactory<T> oldInstance = cacheFactory;
				//
				oldInstance.shutdown();
				cacheFactory = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder().append("Exception encountered during shutdownInstance(CacheFactory)")
					.toString(), e);
		}
		return cacheFactory;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(cacheableObjectFactory, "The CacheableObjectFactory is required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		close();
	}

	public T openCache() throws CacheException {
		T result = null;
		try {
			result = cacheHolder.get();
			if (result == null) {
				result = objectPool.borrowObject();
				//
				cacheHolder.set(result);
			}
		} catch (NullPointerException ex) {
			if (objectPool == null) {
				throw new CacheException("ObjectPool is required.", ex);
			}
			throw new CacheException("Could not open Cache", ex);
		} catch (Exception ex) {
			throw new CacheException("Could not open Cache", ex);
		}
		return result;
	}

	public void closeCache() throws CacheException {
		try {
			T cache = cacheHolder.get();
			cacheHolder.set(null);
			if (cache != null) {
				//
				objectPool.returnObject(cache);
			}
		} catch (Exception ex) {
			throw new CacheException("Could not close Cache", ex);
		}
	}

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
			throw e;
		} catch (Throwable e) {
			throw new CacheException(e);
		} finally {
			if (cache != null) {
				closeCache();
			}
		}
	}

	/**
	 * 關閉
	 * 
	 * @throws CacheException
	 */
	public synchronized void close() throws CacheException {
		if (this.closed) {
			throw new CacheException("CacheFactory was already closed");
		}
		//
		try {
			// TODO cacheHolder 未清, 需用反射清除
			this.closed = true;
			ObjectPool<T> oldPool = this.objectPool;
			this.objectPool = null;
			if (oldPool != null) {
				oldPool.close();
			}
		} catch (Exception ex) {
			throw new CacheException("Cannot close pool", ex);
		}
	}

	public synchronized void clear() throws CacheException {
		if (isCleared()) {
			throw new CacheException("CacheFactory was already cleared");
		}
		//
		try {
			// TODO cacheHolder 未清
			this.cleared = true;
			objectPool.clear();
		} catch (Exception ex) {
			throw new CacheException("Cannot clear pool", ex);
		}
	}

	public synchronized boolean isCleared() {
		return cleared;
	}

	public synchronized int getNumIdle() {
		return objectPool.getNumIdle();
	}

	public synchronized int getNumActive() {
		return objectPool.getNumActive();
	}

}

// private void cleanThreadLocals(Thread thread) throws NoSuchFieldException,
// ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
//
// Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
// threadLocalsField.setAccessible(true);
//
// Class threadLocalMapKlazz =
// Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
// Field tableField = threadLocalMapKlazz.getDeclaredField("table");
// tableField.setAccessible(true);
//
// Object fieldLocal = threadLocalsField.get(thread);
// if (fieldLocal == null) {
// return;
// }
// Object table = tableField.get(fieldLocal);
//
// int threadLocalCount = Array.getLength(table);
//
// for (int i = 0; i < threadLocalCount; i++) {
// Object entry = Array.get(table, i);
// if (entry != null) {
// Field valueField = entry.getClass().getDeclaredField("value");
// valueField.setAccessible(true);
// Object value = valueField.get(entry);
// if (value != null) {
// if
// (value.getClass().getName().equals("com.sun.enterprise.security.authorize.HandlerData"))
// {
// valueField.set(entry, null);
// }
// }
//
// }
// }
//
//
// }

// private void cleanThreadLocals() {
// try {
// // Get a reference to the thread locals table of the current thread
// Thread thread = Thread.currentThread();
// Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
// threadLocalsField.setAccessible(true);
// Object threadLocalTable = threadLocalsField.get(thread);
//
// // Get a reference to the array holding the thread local variables inside the
// // ThreadLocalMap of the current thread
// Class threadLocalMapClass =
// Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
// Field tableField = threadLocalMapClass.getDeclaredField("table");
// tableField.setAccessible(true);
// Object table = tableField.get(threadLocalTable);
//
// // The key to the ThreadLocalMap is a WeakReference object. The referent
// field of this object
// // is a reference to the actual ThreadLocal variable
// Field referentField = Reference.class.getDeclaredField("referent");
// referentField.setAccessible(true);
//
// for (int i=0; i < Array.getLength(table); i++) {
// // Each entry in the table array of ThreadLocalMap is an Entry object
// // representing the thread local reference and its value
// Object entry = Array.get(table, i);
// if (entry != null) {
// // Get a reference to the thread local object and remove it from the table
// ThreadLocal threadLocal = (ThreadLocal)referentField.get(entry);
// threadLocal.remove();
// }
// }
// } catch(Exception e) {
// // We will tolerate an exception here and just log it
// throw new IllegalStateException(e);
// }
// }