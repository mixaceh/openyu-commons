package org.openyu.commons.dao.supporter;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openyu.commons.dao.XmlDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class XmlDaoSupporter extends BaseDaoSupporter implements XmlDao {

	private static final long serialVersionUID = -7661235306121443371L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CommonDaoSupporter.class);

	// xml data list
	private List<Object> list;

	public XmlDaoSupporter() {
		this.list = Collections.synchronizedList(read(null));
	}

	public synchronized List<Object> getList() {
		return list;
	}

	public synchronized void setList(List<Object> list) {
		this.list = list;
	}

	public synchronized List<Object> find(Class entityClass) {
		return list;
	}

	public Object find(Class entityClass, Serializable id) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized int save(Object entity) {
		List deleteList = new LinkedList();
		for (Object element : getList()) {
			if (element.equals(entity)) {
				deleteList.add(entity);
			}
		}
		//
		if (!deleteList.isEmpty()) {
			list.removeAll(deleteList);
		}
		list.add(entity);
		return 1;
	}

	public synchronized int delete(Object entity) {
		list.remove(entity);
		return 1;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T> T delete(Class<?> entityClass, Serializable seq) {
		T result = null;
		Object entity = find(entityClass, seq);
		int del = delete(entity);
		if (del > 0) {
			result = (T) entity;
		}
		return result;
	}

	public <E> InputStream write(Collection<E> list) {
		throw new UnsupportedOperationException();
	}

	public <E> List<E> read(InputStream inputStream) {
		throw new UnsupportedOperationException();
	}

	public long rowCount(Class<?> entityClass) {
		return list.size();
	}

	public int delete(Object entity, String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	public <T> T delete(Class<?> entityClass, Serializable seq, String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	public boolean reindex(Class<?> entityClass) {
		throw new UnsupportedOperationException();
	}

	public <T> boolean reindex(T entity) {
		throw new UnsupportedOperationException();
	}

	public Serializable insert(Object entity) {
		throw new UnsupportedOperationException();
	}

	public Serializable insert(Object entity, String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	public int update(Object entity) {
		throw new UnsupportedOperationException();
	}

	public int update(Object entity, String modifiedUser) {
		throw new UnsupportedOperationException();
	}

}
