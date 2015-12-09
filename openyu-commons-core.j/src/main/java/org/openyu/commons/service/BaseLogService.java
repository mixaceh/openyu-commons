package org.openyu.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 日誌服務
 */
public interface BaseLogService extends BaseService {

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
