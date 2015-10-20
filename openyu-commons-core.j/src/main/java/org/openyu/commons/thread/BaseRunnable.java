package org.openyu.commons.thread;

public interface BaseRunnable extends Runnable {

	/**
	 * 啟動
	 */
	void start();

	/**
	 * 關閉
	 */
	void shutdown();

	/**
	 * 是否關閉
	 * 
	 * @return
	 */
	boolean isShutdown();
}
