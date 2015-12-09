package org.openyu.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 日誌服務
 */
public interface BaseLogService extends BaseService {

	<T> Serializable insert(T entity);

	<T> int update(T entity);

	<T> int delete(T entity);

	<T> T delete(Class<?> entityClass, Serializable seq);

	<E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs);
}
