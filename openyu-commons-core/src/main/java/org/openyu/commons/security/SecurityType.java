package org.openyu.commons.security;

import org.openyu.commons.enumz.StringEnum;

/**
 * 安全性類別
 */
public enum SecurityType implements StringEnum {

	// ---------------------------------------------------
	// 非對稱加密 RSA, DSA, ECC
	// ---------------------------------------------------
	// /**
	// * RSA
	// */
	// RSA(1),
	//
	// /**
	// * DSA
	// */
	// DSA(2),
	//
	// /**
	// * ECC
	// */
	// ECC(3),

	// ---------------------------------------------------
	// 對稱加密
	// ---------------------------------------------------

	/**
	 * DES (DES/ECB/PKCS5Padding)
	 */
	DES("DES"),

	// /**
	// * DES/CBC/NoPadding JCE error
	// */
	// DES_CBC_NoPadding("DES/CBC/NoPadding"),

	/**
	 * DES/CBC/PKCS5Padding 每次結果會不同
	 */
	DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),

	// /**
	// * DES/ECB/NoPadding JCE error
	// */
	// DES_ECB_NoPadding("DES/ECB/NoPadding"),

	/**
	 * DES/ECB/PKCS5Padding
	 */
	DES_ECB_PKCS5Padding("DES/ECB/PKCS5Padding"),

	/**
	 * DES (DESede/ECB/PKCS5Padding)
	 */
	DESede("DESede"),

	// /**
	// * DESede/CBC/NoPadding JCE error
	// */
	// DESede_CBC_NoPadding("DESede/CBC/NoPadding"),

	/**
	 * DESede/CBC/PKCS5Padding 每次結果會不同
	 */
	DESede_CBC_PKCS5Padding("DESede/CBC/PKCS5Padding"),

	// /**
	// * DESede/ECB/NoPadding JCE error
	// */
	// DESede_ECB_NoPadding("DESede/ECB/NoPadding"),

	/**
	 * DESede/ECB/PKCS5Padding
	 */
	DESede_ECB_PKCS5Padding("DESede/ECB/PKCS5Padding"),

	/**
	 * AES (AES/ECB/PKCS5Padding)
	 */
	AES("AES"),

	// /**
	// * AES/CBC/NoPadding JCE error
	// */
	// AES_CBC_NoPadding("AES/CBC/NoPadding"),

	/**
	 * AES/CBC/PKCS5Padding 每次結果會不同
	 */
	AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),

	// /**
	// * AES/ECB/NoPadding JCE error
	// */
	// AES_ECB_NoPadding("AES/ECB/NoPadding"),

	/**
	 * AES/ECB/PKCS5Padding
	 */
	AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),

	// ---------------------------------------------------
	// MD
	// ---------------------------------------------------
	/**
	 * MD5
	 */
	MD5("MD5"),

	/**
	 * SHA-1
	 */
	SHA_1("SHA-1"),

	/**
	 * SHA-256
	 */
	SHA_256("SHA-256"),

	// ---------------------------------------------------
	// Hmac
	// ---------------------------------------------------
	/**
	 * HmacMD5
	 */
	HmacMD5("HmacMD5"),

	/**
	 * HmacSHA1
	 */
	HmacSHA1("HmacSHA1"),

	/**
	 * HmacSHA256
	 */
	HmacSHA256("HmacSHA256"),

	;
	private final String value;

	private SecurityType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
