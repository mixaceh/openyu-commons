package org.openyu.commons.thread;

import java.util.concurrent.Callable;

public interface BaseCallable<V> extends Callable<V> {

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
