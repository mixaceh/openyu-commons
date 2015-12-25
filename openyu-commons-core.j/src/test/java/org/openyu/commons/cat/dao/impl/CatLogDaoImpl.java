package org.openyu.commons.cat.dao.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openyu.commons.cat.dao.CatLogDao;
import org.openyu.commons.cat.log.impl.CatInsertLogImpl;
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatLogDaoImpl extends CommonDaoSupporter implements CatLogDao {

	private static final long serialVersionUID = -8140396918843969392L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CatLogDaoImpl.class);

	/**
	 * 貓新增log
	 */
	private static final String CAT_INSERT_LOG_PO_NAME = CatInsertLogImpl.class.getName();

	public CatLogDaoImpl() {
	}

	/**
	 * 刪除貓新增log
	 * 
	 * @param accountId
	 * @return
	 */
	public int deleteCatInsertLog(String catId) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		//
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ");
		hql.append(CAT_INSERT_LOG_PO_NAME + " ");
		hql.append("where 1=1 ");

		// catId
		hql.append("and catId = :catId ");
		params.put("catId", catId);
		//
		return executeByHql(hql, params);
	}

}
