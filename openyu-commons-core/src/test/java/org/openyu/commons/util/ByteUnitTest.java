package org.openyu.commons.util;

import org.junit.Test;

public class ByteUnitTest {

	@Test
	public void toKB() {
		double bytes = 1024;// 1k
		// 10進位 1.024
		System.out.println(ByteUnit.BYTE.toKB(bytes));
		// 2進位 1
		System.out.println(ByteUnit.BYTE.toKiB(bytes));
	}

	@Test
	public void toMB() {
		double bytes = 1024 * 1024;// 1mb
		// 10進位 1.048576
		System.out.println(ByteUnit.BYTE.toMB(bytes));
		// 2進位 1.0
		System.out.println(ByteUnit.BYTE.toMiB(bytes));
	}
}
