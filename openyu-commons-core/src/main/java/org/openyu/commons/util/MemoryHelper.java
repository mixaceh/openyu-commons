package org.openyu.commons.util;

import java.text.MessageFormat;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.util.memory.MemoryCounter;

public final class MemoryHelper extends BaseHelperSupporter {

	private final static String MEMORY_PATTERN = "{0} bytes, {1} KB, {2} MB";

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

	/**
	 * PrintlnMemory.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 */
	public static <T> void println(final T value) {
		println("", value);
	}

	/**
	 * PrintlnMemory.
	 *
	 * @param <T>
	 *            the generic type
	 * @param title
	 *            the title
	 * @param value
	 *            the value
	 */
	public static <T> void println(String title, final T value) {
		double bytes = MemoryHelper.sizeOf(value);
		double kb = NumberHelper.round(ByteUnit.BYTE.toKiB(bytes), 1);
		double mb = NumberHelper.round(ByteUnit.BYTE.toMiB(bytes), 1);
		//
		System.out.print(title);
		System.out.println(MessageFormat.format(MEMORY_PATTERN, bytes, kb, mb));
	}

}
