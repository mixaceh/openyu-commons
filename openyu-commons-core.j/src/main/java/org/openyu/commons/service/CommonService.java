package org.openyu.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.util.concurrent.MapCache;

/**
 * ORM+JDBC共用服務
 */
public interface CommonService extends JdbcService, OrmService {
	/**
	 * 存放共用的beanCache,當多緒時,物件需注意資料同步
	 * 
	 * @return
	 */
	MapCache<String, Object> getBeanCache();

	/**
	 * 清除
	 */
	void clear();

	/**
	 * 重置
	 */
	void reset();

	/**
	 * 設定監聽器
	 * 
	 * @param beanListener
	 */
	void setBeanListener(BeanListener beanListener);

	/**
	 * 加入監聽器
	 * 
	 * @param beanListener
	 */
	void addBeanListener(BeanListener beanListener);

	/**
	 * 移除監聽器
	 * 
	 * @param beanListener
	 */
	void removeBeanListener(BeanListener beanListener);

	/**
	 * 取得所有監聽器
	 * 
	 * @return
	 */
	BeanListener[] getBeanListeners();

	// ------------------------------------
	// 資料庫佇列, 2014/09/22
	// ------------------------------------
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
}
