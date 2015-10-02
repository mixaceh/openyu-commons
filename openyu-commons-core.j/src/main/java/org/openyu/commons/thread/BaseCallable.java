package org.openyu.commons.thread;

import java.util.concurrent.Callable;

public interface BaseCallable<V> extends Callable<V> {

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
	// execute()改成 supporter.doCall()
	// */
	// V execute();

	void start();

	void shutdown();

	boolean isShutdown();
}
