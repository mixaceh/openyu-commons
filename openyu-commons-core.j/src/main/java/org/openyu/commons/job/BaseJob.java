package org.openyu.commons.job;

public interface BaseJob {

	String KEY = BaseJob.class.getName();

	/**
	 * 取消
	 * 
	 * @return
	 */
	boolean isCancel();

	void setCancel(boolean cancel);

	/**
	 * 偵錯
	 * 
	 * @return
	 */
	boolean isLogEnable();

	void setLogEnable(boolean logEnable);

	/**
	 * 執行
	 */
	void execute();
}
