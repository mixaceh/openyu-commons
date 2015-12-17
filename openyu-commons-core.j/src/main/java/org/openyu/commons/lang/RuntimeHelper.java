package org.openyu.commons.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public final class RuntimeHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(RuntimeHelper.class);

	private RuntimeHelper() {
		throw new HelperException(
				new StringBuilder().append(RuntimeHelper.class.getName()).append(" can not construct").toString());
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
