package org.openyu.commons.cat.dao;

import org.openyu.commons.dao.CommonDao;

public interface CatLogDao extends CommonDao {

	/**
	 * 刪除貓新增log
	 * 
	 * @param accountId
	 * @return
	 */
	int deleteCatInsertLog(String catId);
}
