package org.openyu.commons.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Generic Dao
 */
public interface GenericDao extends BaseDao {

	// ------------------------------------
	// oo find(select)
	// ------------------------------------
	// List find(Class entity);
	/**
	 * hql查詢所有資料, = findAll
	 * 
	 * @param entityClass
	 */
	<E> List<E> find(Class<?> entityClass);

	// Object find(Class entity, Serializable id);
	// 使用generic可不用轉型
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

	// ------------------------------------
	// oo save(insert/update)
	// ------------------------------------
	// void save(Object entity);
	// int save(Object entity);
	//
	// int save(Object entity, String modifiedUser);

	// ------------------------------------
	// oo save(insert/update)
	// 2011/11/01 save改insert/update,為了觸發event
	// ------------------------------------
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
	 * orm新增
	 * 
	 * =save()
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return pk
	 */
	<T> Serializable insert(T entity, String modifiedUser);

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
	 * orm修改
	 * 
	 * =update()
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return
	 */
	<T> int update(T entity, String modifiedUser);

	// ------------------------------------
	// oo delete
	// ------------------------------------
	// void delete(Class entityClass);
	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @return
	 */
	<T> int delete(T entity);

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return
	 */
	<T> int delete(T entity, String modifiedUser);

	// oo delete(get first, then delete)
	// void delete(Class entity, Serializable seq);
	/**
	 * orm刪除
	 * 
	 * @param entityClass
	 * @param seq
	 * @return
	 */
	<T> T delete(Class<?> entityClass, Serializable seq);

	/**
	 * orm刪除
	 * 
	 * @param entityClass
	 * @param seq
	 * @param modifiedUser
	 * @return
	 */
	<T> T delete(Class<?> entityClass, Serializable seq, String modifiedUser);

	/**
	 * orm刪除
	 * 
	 * @param entityClass
	 * @param seqs
	 * @return
	 */
	<E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs);

	/**
	 * orm刪除
	 * 
	 * @param entityClass
	 * @param seqs
	 * @param modifiedUser
	 * @return
	 */
	<E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs,
			String modifiedUser);

	/**
	 * orm計算筆數
	 * 
	 * @param entityClass
	 * @return
	 */
	long rowCount(Class<?> entityClass);

	/**
	 * 全文檢索重建索引, 2011/10/12
	 * 
	 * @param entityClass
	 * @return
	 */
	boolean reindex(Class<?> entityClass);

	/**
	 * 全文檢索重建索引, 2011/10/12
	 * 
	 * @param entity
	 * @return
	 */
	<T> boolean reindex(T entity);

}
