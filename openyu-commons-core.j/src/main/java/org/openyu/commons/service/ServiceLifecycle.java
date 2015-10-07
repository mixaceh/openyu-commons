package org.openyu.commons.service;

/**
 * 服務生命週期
 */
public interface ServiceLifecycle {

	/**
	 * 啟動
	 */
	void start() throws Exception;

	/**
	 * 關閉
	 */
	void shutdown() throws Exception;

	/**
	 * 重啟
	 */
	void restart() throws Exception;
}
