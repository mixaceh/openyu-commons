package org.openyu.commons.util;

import org.openyu.commons.enumz.IntEnum;

/**
 * 檢查碼類別
 */
public enum ChecksumType implements IntEnum {

	/**
	 * crc32
	 */
	CRC32(1),

	/**
	 * adler32
	 */
	ADLER32(2),

	;

	private final int intValue;

	private ChecksumType(int intValue) {
		this.intValue = intValue;
	}

	public int getValue() {
		return intValue;
	}
}
