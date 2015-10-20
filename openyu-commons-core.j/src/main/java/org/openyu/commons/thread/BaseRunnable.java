package org.openyu.commons.thread;

public interface BaseRunnable extends Runnable {

	/**
	 * 啟動
	 */
	void start() throws Exception;
	
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
