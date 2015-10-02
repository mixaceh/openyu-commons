package org.openyu.commons.util;

import org.junit.Test;

public class ByteUnitTest {

	@Test
	public void kb() {
		double bytes = 1024;// 1k
		// 10進位
		System.out.println(ByteUnit.BYTE.toKB(bytes));
		// 2進位
		System.out.println(ByteUnit.BYTE.toKiB(bytes));
	}

	@Test
	public void mb() {
		double bytes = 1024 * 1024;// 1mb
		// 10進位
		System.out.println(ByteUnit.BYTE.toMB(bytes));
		// 2進位
		System.out.println(ByteUnit.BYTE.toMiB(bytes));
	}
}
