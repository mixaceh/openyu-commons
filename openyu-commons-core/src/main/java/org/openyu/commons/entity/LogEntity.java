package org.openyu.commons.entity;

import java.util.Date;

public interface LogEntity extends BaseEntity
{

	String KEY = LogEntity.class.getName();

	/**
	 * 紀錄日期
	 * @return
	 */
	Date getLogDate();

	void setLogDate(Date logDate);

}
