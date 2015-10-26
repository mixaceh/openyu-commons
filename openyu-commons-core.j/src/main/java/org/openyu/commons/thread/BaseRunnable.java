package org.openyu.commons.thread;

public interface BaseRunnable extends Runnable {

	/**
	 * 啟動
	 */
	void start() throws Exception;

	/**
	 * 是否啟動
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * 關閉
	 */
	void shutdown() throws Exception;

	/**
	 * 是否關閉
	 * 
	 * @return
	 */
	boolean isShutdown();
}
