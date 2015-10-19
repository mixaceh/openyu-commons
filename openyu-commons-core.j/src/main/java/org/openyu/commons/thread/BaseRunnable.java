package org.openyu.commons.thread;

public interface BaseRunnable extends Runnable {

	// /**
	// * 取消
	// *
	// * @return true, if is cancel
	// */
	// boolean isCancel();
	//
	// /**
	// * 設定取消
	// *
	// * @param cancel
	// * the new cancel
	// */
	// void setCancel(boolean cancel);

	// /**
	// * 是否寫log
	// *
	// * @return
	// */
	// boolean isLogEnable();
	//
	// void setLogEnable(boolean logEnable);

	// /**
	// * 執行
	// execute()改成 supporter.doRun()
	// */
	// void execute();

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
