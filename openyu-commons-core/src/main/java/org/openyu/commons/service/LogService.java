package org.openyu.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 日誌服務
 */
public interface LogService extends BaseService {

	// ------------------------------------
	// 同步操作
	// ------------------------------------
	/**
	 * hwl查詢所有資料, = findAll
	 * 
	 * @param entityClass
	 */
	<E> List<E> find(Class<?> entityClass);

	/**
	 * hql查詢單筆pk資料
	 * 
	 * @param entityClass
	 * @param seq
	 */
	<T> T find(Class<?> entityClass, Serializable seq);

	/**
	 * hql查詢多筆pk資料
	 * 
	 * @param entityClass
	 * @param seqs
	 */
	<E> List<E> find(Class<?> entityClass, Collection<Serializable> seqs);

	/**
	 * orm新增
	 * 
	 * =save()
	 * 
	 * @param entity
	 * @return pk
	 */
	<T> Serializable insert(T entity);

	/**
	 * orm修改
	 * 
	 * =update()
	 * 
	 * @param entity
	 * @return
	 */
	<T> int update(T entity);

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @return
	 */
	<T> int delete(T entity);

	// ------------------------------------
	// 異步操作
	// ------------------------------------
	/**
	 * 加到佇列新增
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerInsert(T entity);

	/**
	 * 加到佇列儲存
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerUpdate(T entity);

	/**
	 * 加到佇列刪除
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerDelete(T entity);

	/**
	 * 加到佇列刪除,會先用find找entity
	 *
	 * @param entityClass
	 *            the entity class
	 * @param seq
	 *            the seq
	 * @return true, if successful
	 */
	boolean offerDelete(Class<?> entityClass, Serializable seq);

	/**
	 * 多筆加到佇列刪除, 會先用find找entity
	 *
	 * @param entityClass
	 *            the entity class
	 * @param seqs
	 *            the seqs
	 * @return the list
	 */
	List<Boolean> offerDelete(Class<?> entityClass, Collection<Serializable> seqs);

}
