package org.openyu.commons.security;

import org.openyu.commons.model.BaseModel;

public interface AuthKey extends BaseModel {
	/**
	 * key uuid,長度 36
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

	/**
	 * 時間戳
	 * 
	 * @return
	 */
	long getTimeStamp();

	void setTimeStamp(long timeStamp);

	/**
	 * key有效期間,毫秒
	 * 
	 * @return
	 */
	long getAliveMills();

	void setAliveMills(long aliveMills);

	/**
	 * 是否到期
	 * 
	 * @return
	 */
	boolean isExpired();

}
