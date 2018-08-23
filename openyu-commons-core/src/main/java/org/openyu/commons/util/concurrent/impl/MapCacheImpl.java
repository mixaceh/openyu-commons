package org.openyu.commons.util.concurrent.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.concurrent.MapCache;

public class MapCacheImpl<K, V> extends BaseModelSupporter implements MapCache<K, V> {

	private static final long serialVersionUID = -2410205759813960541L;

	private Map<K, V> cache = new ConcurrentHashMap<K, V>();

	/**
	 * 存放value=null的key
	 */
	private Set<K> nullValueKeys = new CopyOnWriteArraySet<K>();

	/**
	 * 讀寫鎖
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 讀鎖
	 */
	private Lock readLock = lock.readLock();

	/**
	 * 寫鎖
	 */
	private Lock writeLock = lock.writeLock();

	/**
	 * 停用
	 */
	private AtomicBoolean disable = new AtomicBoolean(false);

	public MapCacheImpl() {
	}

	@Override
	public V get(K key) {
		V result = null;
		if (!disable.get()) {
			try {
				// #issue must lock
				// #fix 讀鎖
				// read.lock();
				readLock.lockInterruptibly();
				try {

					result = cache.get(key);
				} finally {
					readLock.unlock();
				}
			} catch (InterruptedException ex) {
				// System.out.println("get thread[" +
				// Thread.currentThread().getId() + "] InterruptedException");
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public V put(K key, V value) {
		V result = null;
		if (!disable.get()) {
			// #issue must lock
			// #fix 寫鎖
			try {
				writeLock.lockInterruptibly();
				try {
					if (value != null) {
						result = cache.put(key, value);
					} else {
						// 存放value=null的key
						nullValueKeys.add(key);
					}
				} finally {
					writeLock.unlock();
				}
			} catch (InterruptedException ex) {
				// System.out.println("put thread[" +
				// Thread.currentThread().getId() + "] InterruptedException");
				ex.printStackTrace();
			}
		}
		return result;

	}

	@Override
	public void lock() {
		if (!disable.get()) {
			writeLock.lock();
		}
	}

	@Override
	public void unlock() {
		if (!disable.get()) {
			writeLock.unlock();
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		if (!disable.get()) {
			writeLock.lockInterruptibly();
		}
	}

	protected boolean isNullValue(K key) {
		boolean result = false;
		try {
			readLock.lockInterruptibly();
			try {
				if (!disable.get()) {
					result = nullValueKeys.contains(key);
				}
			} finally {
				readLock.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 是否為value!=null的key
	 */
	@Override
	public boolean isNotNullValue(K key) {
		return !isNullValue(key);
	}

	@Override
	public void clear() {
		try {
			writeLock.lockInterruptibly();
			try {
				cache.clear();
				nullValueKeys.clear();
			} finally {
				writeLock.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public V remove(K k) {
		V result = null;
		if (!disable.get()) {
			// #issue must lock
			// #fix 寫鎖
			try {
				writeLock.lockInterruptibly();
				try {
					if (k != null) {
						if (cache.containsKey(k)) {
							result = cache.remove(k);
						} else if (nullValueKeys.contains(k)) {
							nullValueKeys.remove(k);
						}
					}
				} finally {
					writeLock.unlock();
				}
			} catch (InterruptedException ex) {
				// System.out.println("put thread[" +
				// Thread.currentThread().getId() + "] InterruptedException");
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public boolean contains(K k) {
		return cache.containsKey(k);
	}

	public Map<K, V> getCache() {
		return cache;
	}

	public void setCache(Map<K, V> cache) {
		this.cache = cache;
	}

	@Override
	public List<K> getKeys() {
		List<K> result = new LinkedList<K>();
		for (K key : cache.keySet()) {
			result.add(key);
		}
		return result;
	}

	@Override
	public List<V> getValues() {
		List<V> result = new LinkedList<V>();
		for (V value : cache.values()) {
			result.add(value);
		}
		return result;
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		// builder.append("readLock", readLock);
		// builder.append("writeLock", writeLock);
		builder.append("disable", disable);
		builder.append("cache", cache);
		builder.append("nullValueKeys", nullValueKeys);
		return builder.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		MapCacheImpl<K, V> copy = null;
		copy = (MapCacheImpl<K, V>) super.clone();
		copy.lock = new ReentrantReadWriteLock();
		copy.readLock = lock.readLock();
		copy.writeLock = lock.readLock();
		copy.cache = clone(cache);
		copy.nullValueKeys = clone(nullValueKeys);
		return copy;
	}

}
