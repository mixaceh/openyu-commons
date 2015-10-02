package org.openyu.commons.util;

import org.openyu.commons.enumz.IntEnum;

/**
 * 壓縮類別
 */
public enum CompressType implements IntEnum {

	/**
	 * lzma
	 */
	LZMA(1),

	/**
	 * gzip
	 */
	GZIP(2),

	/**
	 * deflate
	 */
	DEFLATE(3),

	/**
	 * deflater
	 */
	DEFLATER(4),

	/**
	 * snappy
	 */
	SNAPPY(5),

	/**
	 * lzf
	 */
	LZF(6),

	/**
	 * lz4
	 */
	LZ4(7),

	;

	private final int intValue;

	private CompressType(int intValue) {
		this.intValue = intValue;
	}

	public int getValue() {
		return intValue;
	}
}
