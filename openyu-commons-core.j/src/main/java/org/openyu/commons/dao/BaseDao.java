package org.openyu.commons.dao;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.openyu.commons.service.BaseService;

/**
 * 基底Relational Data Access Object
 */
public interface BaseDao extends BaseService {

	<E> InputStream write(Collection<E> list);

	<E> List<E> read(InputStream inputStream);

}
