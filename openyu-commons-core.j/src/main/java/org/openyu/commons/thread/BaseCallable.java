package org.openyu.commons.thread;

import java.util.concurrent.Callable;

public interface BaseCallable<V> extends Callable<V> {

	// /**
	// * 執行
	// execute()改成 supporter.doCall()
	// */
	// V execute();

	void start();

	void shutdown();

	boolean isShutdown();
}
