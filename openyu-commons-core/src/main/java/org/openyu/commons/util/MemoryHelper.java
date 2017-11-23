package org.openyu.commons.util;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.util.memory.MemoryCounter;

public final class MemoryHelper extends BaseHelperSupporter {

	private MemoryHelper() {
		throw new HelperException(
				new StringBuilder().append(MemoryHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * 計算占記憶體空間大小, bytes
	 * 
	 * @param value
	 * @return
	 */
	public static long sizeOf(Object value) {
		long result = 0L;
		if (value == null) {
			return result;
		}
		//
		MemoryCounter counter = new MemoryCounter();
		result = counter.estimate(value);
		return result;
	}
}
