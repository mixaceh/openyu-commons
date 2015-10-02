package org.openyu.commons.util;

import org.openyu.commons.enumz.IntEnum;

/**
 * 序列化類別
 */
public enum SerializeType implements IntEnum {

	/**
	 * jgroup
	 */
	JGROUP(1),

	/**
	 * jdk
	 */
	JDK(2),

	/**
	 * fst
	 */
	@Deprecated
	FST(3), // throw OutOfMemoryError, see fst.readme.txt

	/**
	 * kryo
	 */
	KRYO(4),

	/**
	 * json
	 */
	@Deprecated
	//ser, json, xml
	JACKSON(5), // throw JsonMappingException, see fasterxml.readme.txt

	/**
	 * smile
	 */
	@Deprecated
	SMILE(6), // throw JsonMappingException, see fasterxml.readme.txt

	/**
	 * smile jaxrs
	 */
	@Deprecated
	SMILE_JAXRS(7), // throw JsonMappingException, see fasterxml.readme.txt

	//
	;

	private final int intValue;

	private SerializeType(int intValue) {
		this.intValue = intValue;
	}

	public int getValue() {
		return intValue;
	}
}
