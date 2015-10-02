package org.openyu.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 資料庫佇列服務.
 */
public interface QueueService extends BaseService {

	/**
	 * 加到佇列新增.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerInsert(T entity);

	/**
	 * 加到佇列儲存.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerUpdate(T entity);

	/**
	 * 加到佇列刪除.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return true, if successful
	 */
	<T> boolean offerDelete(T entity);

	/**
	 * 加到佇列刪除,會先用find找entity.
	 *
	 * @param entityClass
	 *            the entity class
	 * @param seq
	 *            the seq
	 * @return true, if successful
	 */
	boolean offerDelete(Class<?> entityClass, Serializable seq);

	/**
	 * 多筆加到佇列刪除,會先用find找entity.
	 *
	 * @param entityClass
	 *            the entity class
	 * @param seqs
	 *            the seqs
	 * @return the list
	 */
	List<Boolean> offerDelete(Class<?> entityClass,
			Collection<Serializable> seqs);

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

	// ------------------------------------
	// oo delete
	// ------------------------------------

	/**
	 * orm刪除.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @return the int
	 */
	<T> int delete(T entity);

	/**
	 * orm刪除.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entityClass
	 *            the entity class
	 * @param seq
	 *            the seq
	 * @return the t
	 */
	<T> T delete(Class<?> entityClass, Serializable seq);

	/**
	 * orm刪除.
	 *
	 * @param <E>
	 *            the element type
	 * @param entityClass
	 *            the entity class
	 * @param seqs
	 *            the seqs
	 * @return the list
	 */
	<E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs);

}
