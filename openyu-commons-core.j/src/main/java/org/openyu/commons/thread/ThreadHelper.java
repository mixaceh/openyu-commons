package org.openyu.commons.thread;

import java.util.concurrent.TimeUnit;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * 線程輔助類
 */
public final class ThreadHelper extends BaseHelperSupporter {

	private ThreadHelper() {
		throw new HelperException(
				new StringBuilder().append(ThreadHelper.class.getName()).append(" can not construct").toString());
	}

	public static void sleep(long millis) {
		try {
			// 0表無限
			if (millis == 0) {
				Thread.sleep(Long.MAX_VALUE);
			} else {
				// Thread.sleep(millis);
				TimeUnit.MILLISECONDS.sleep(millis);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 迴圈
	 * 
	 * <=0, =>無間隔
	 * 
	 * >0, => 間隔毫秒
	 * 
	 * @param millis
	 */
	public static void loop(long millis) {
		while (true) {
			if (millis > 0) {
				sleep(millis);
			}
		}
	}

}
