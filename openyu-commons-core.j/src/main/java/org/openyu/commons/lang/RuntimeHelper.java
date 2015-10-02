package org.openyu.commons.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class RuntimeHelper.
 */
public class RuntimeHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(RuntimeHelper.class);

	/**
	 * Instantiates a new helper.
	 */
	public RuntimeHelper() {
		if (InstanceHolder.INSTANCE != null) {
				throw new HelperException(
						new StringBuilder().append(getDisplayName()).append(" can not construct").toString());
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		// private static final RuntimeHelper INSTANCE = new RuntimeHelper();
		private static RuntimeHelper INSTANCE = new RuntimeHelper();
	}

	/**
	 * Gets the single instance of BlankHelper.
	 *
	 * @return single instance of BlankHelper
	 */
	public synchronized static RuntimeHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new RuntimeHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 已使用的記憶體
	 * 
	 * byte
	 * 
	 * @return
	 */
	public static long usedMemory() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	/**
	 * 資源回收
	 */
	@SuppressWarnings("static-access")
	public static void gc() {
		long usedMem1 = usedMemory();
		long usedMem2 = Long.MAX_VALUE;
		//
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); i++) {
			Runtime.getRuntime().runFinalization();
			Runtime.getRuntime().gc();
			Thread.currentThread().yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

}
