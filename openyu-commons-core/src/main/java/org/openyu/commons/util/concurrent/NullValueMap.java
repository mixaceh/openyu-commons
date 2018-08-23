package org.openyu.commons.util.concurrent;

import java.util.List;

import org.openyu.commons.model.BaseModel;

public interface NullValueMap<K, V> extends BaseModel {

	V get(K key);

	V put(K key, V value);

	void lock();

	void unlock();

	void lockInterruptibly() throws InterruptedException;

	boolean isNotNullValue(K key);

	void clear();

	V remove(K k);

	boolean contains(K k);

	/**
	 * 取得所有的key
	 * 
	 * @return
	 */
	List<K> getKeys();

	/**
	 * 取得所有的value
	 * 
	 * @return
	 */
	List<V> getValues();

	int size();

}