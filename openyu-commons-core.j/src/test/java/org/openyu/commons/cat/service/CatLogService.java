package org.openyu.commons.cat.service;

import java.util.List;

import org.openyu.commons.cat.log.CatInsertLog;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.service.LogService;

/**
 * 帳戶日誌服務
 */
public interface CatLogService extends LogService {
	// --------------------------------------------------
	// db
	// --------------------------------------------------

	// --------------------------------------------------
	// CatInsertLog
	// --------------------------------------------------
	/**
	 * 刪除貓新增log
	 * 
	 * @param accountId
	 * @return
	 */
	int deleteCatInsertLog(String catId);

	// --------------------------------------------------
	// biz
	// --------------------------------------------------
	/**
	 * 紀錄貓新增
	 * 
	 * @param catId
	 */
	void recordInsert(String catId);
}
