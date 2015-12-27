package org.openyu.commons.cat.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

import org.openyu.commons.cat.dao.CatLogDao;
import org.openyu.commons.cat.log.CatInsertLog;
import org.openyu.commons.cat.log.impl.CatInsertLogImpl;
import org.openyu.commons.cat.service.CatLogService;
import org.openyu.commons.dao.anno.LogTx;
import org.openyu.commons.service.supporter.LogServiceSupporter;

/**
 * 貓日誌服務
 */
public class CatLogServiceImpl extends LogServiceSupporter implements CatLogService {

	private static final long serialVersionUID = 3659244335150140179L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CatLogServiceImpl.class);

	@Autowired
	@Qualifier("catLogDao")
	private transient CatLogDao catLogDao;

	public CatLogServiceImpl() {
	}

	// --------------------------------------------------
	// db
	// --------------------------------------------------

	// --------------------------------------------------
	// CatInsertLog
	// --------------------------------------------------
	@LogTx
	public int deleteCatInsertLog(String catId) {
		return catLogDao.deleteCatInsertLog(catId);
	}

	// --------------------------------------------------
	// biz
	// --------------------------------------------------
	/**
	 * 紀錄貓新增
	 * 
	 * @param catId
	 */
	@LogTx
	public void recordInsert(String catId) {
		CatInsertLog log = new CatInsertLogImpl();
		log.setSeq(71L);
		log.setCatId(catId);
		log.setLogDate(new Date());
		log.setVersion(1);
		//
		offerUpdate(log);
	}
}
