package org.openyu.commons.cat.log;

import org.openyu.commons.entity.SeqLogEntity;

/**
 * 貓log,不做bean,直接用entity處理掉
 */
public interface CatInsertLog extends SeqLogEntity {

	String KEY = CatInsertLog.class.getName();

	String getCatId();

	void setCatId(String catId);
}
