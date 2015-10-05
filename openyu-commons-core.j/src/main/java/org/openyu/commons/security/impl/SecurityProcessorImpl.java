package org.openyu.commons.security.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.processor.supporter.BaseProcessorSupporter;
import org.openyu.commons.security.SecurityType;
import org.openyu.commons.security.SecurityProcessor;
import org.openyu.commons.security.SecurityHelper;
import org.openyu.commons.security.adapter.SecurityTypeXmlAdapter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "securityProcessor")
@XmlAccessorType(XmlAccessType.FIELD)
public class SecurityProcessorImpl extends BaseProcessorSupporter implements SecurityProcessor {

	private static final long serialVersionUID = 8889147664445576044L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(SecurityProcessorImpl.class);

	// --------------------------------------------------
	// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	/**
	 * 安全性類別
	 */
	@XmlJavaTypeAdapter(SecurityTypeXmlAdapter.class)
	private static Set<SecurityType> securityTypes = new LinkedHashSet<SecurityType>();

	// --------------------------------------------------

	/**
	 * 預設是否加密
	 */
	public static final boolean DEFAULT_SECURITY = true;

	/** 是否加密 */
	private boolean security = DEFAULT_SECURITY;;

	/**
	 * 預設加密類型
	 */
	public static final SecurityType DEFAULT_SECURITY_TYPE = SecurityType.HmacSHA1;

	/**
	 * 加密類別
	 */
	private SecurityType securityType = DEFAULT_SECURITY_TYPE;

	/**
	 * 預設加密key
	 */
	public static final String DEFAULT_SECURITY_KEY = "encrypt";

	/**
	 * 加密key
	 */
	private String securityKey = DEFAULT_SECURITY_KEY;

	static {
		// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
		securityTypes = EnumHelper.valuesSet(SecurityType.class);
	}

	public SecurityProcessorImpl() {
	}

	/**
	 * 是否安全性
	 * 
	 * @return
	 */
	public boolean isSecurity() {
		return security;
	}

	public void setSecurity(boolean security) {
		this.security = security;
	}

	/**
	 * 安全性類別
	 * 
	 * @return
	 */
	public SecurityType getSecurityType() {
		return securityType;
	}

	public void setSecurityType(SecurityType securityType) {
		this.securityType = securityType;
	}

	/**
	 * 安全性key
	 * 
	 * @return
	 */
	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	/**
	 * 安全性類別列舉
	 * 
	 * @return
	 */
	public Set<SecurityType> getSecurityTypes() {
		if (securityTypes == null) {
			securityTypes = new LinkedHashSet<SecurityType>();
		}
		return securityTypes;
	}

	public void setSecurityTypes(Set<SecurityType> securityTypes) {
		SecurityProcessorImpl.securityTypes = securityTypes;
	}

	/**
	 * 加密
	 * 
	 * @param securityTypeValue
	 *            安全性類別
	 * @see SecurityType
	 * @param value
	 * @return
	 */
	public byte[] encrypt(String securityTypeValue, byte[] value) {
		SecurityType securityType = EnumHelper.valueOf(SecurityType.class, securityTypeValue);
		AssertHelper.notNull(securityType, "The SecurityType must not be null");
		this.securityType = securityType;
		return encrypt(value);
	}

	/**
	 * 加密
	 * 
	 * @param value
	 * @return
	 */
	public byte[] encrypt(byte[] value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		if (!security) {
			return value;
		}
		AssertHelper.notNull(securityType, "The SecurityType must not be null");
		AssertHelper.notNull(securityKey, "The SecurityKey must not be null");
		//
		switch (securityType) {
		// DES
		case DES:
			// case DES_CBC_NoPadding:
		case DES_CBC_PKCS5Padding:
			// case DES_ECB_NoPadding:
		case DES_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 8, "The SecurityKey length must be greater than 8");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.DES.getValue());
			result = SecurityHelper.encrypt(value, secretKey, securityType.getValue());
			break;
		}

			// DESede
		case DESede:
			// case DES_CBC_NoPadding:
		case DESede_CBC_PKCS5Padding:
			// case DES_ECB_NoPadding:
		case DESede_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 24, "The SecurityKey length must be greater than 24");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.DESede.getValue());
			result = SecurityHelper.encrypt(value, secretKey, securityType.getValue());
			break;
		}

			// AES
		case AES:
			// case AES_CBC_NoPadding:
		case AES_CBC_PKCS5Padding:
			// case AES_ECB_NoPadding:
		case AES_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 16, "The SecurityKey length must be greater than 16");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.AES.getValue());
			result = SecurityHelper.encrypt(value, secretKey, securityType.getValue());
			break;
		}

			// MD
		case MD5:
		case SHA_1:
		case SHA_256: {
			result = SecurityHelper.md(value, securityType.getValue());
			break;
		}

			// Hmac
		case HmacMD5:
		case HmacSHA1:
		case HmacSHA256: {
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, securityType.getValue());
			result = SecurityHelper.mac(value, secretKey, securityType.getValue());
			break;
		}
		default: {
			AssertHelper.unsupported("The SecurityType [" + securityType + "] is unsupported");
			break;
		}
		}
		return result;
	}

	/**
	 * 解密
	 * 
	 * @param securityTypeValue
	 *            安全性類別
	 * @see SecurityType
	 * @param value
	 * @return
	 */
	public byte[] decrypt(String securityTypeValue, byte[] value) {
		SecurityType securityType = EnumHelper.valueOf(SecurityType.class, securityTypeValue);
		AssertHelper.notNull(securityType, "The SecurityType must not be null");
		this.securityType = securityType;
		return decrypt(value);
	}

	/**
	 * 解密
	 * 
	 * @param value
	 * @return
	 */
	public byte[] decrypt(byte[] value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		if (!security) {
			return value;
		}
		AssertHelper.notNull(securityType, "The SecurityType must not be null");
		AssertHelper.notNull(securityKey, "The SecurityKey must not be null");
		//
		switch (securityType) {
		// DES
		case DES:
			// case DES_CBC_NoPadding:
		case DES_CBC_PKCS5Padding:
			// case DES_ECB_NoPadding:
		case DES_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 8, "The SecurityKey length must be greater than 8");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.DES.getValue());
			result = SecurityHelper.decrypt(value, secretKey, securityType.getValue());
			break;
		}

			// DESede
		case DESede:
			// case DES_CBC_NoPadding:
		case DESede_CBC_PKCS5Padding:
			// case DES_ECB_NoPadding:
		case DESede_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 24, "The SecurityKey length must be greater than 24");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.DESede.getValue());
			result = SecurityHelper.decrypt(value, secretKey, securityType.getValue());
			break;
		}

			// AES
		case AES:
			// case AES_CBC_NoPadding:
		case AES_CBC_PKCS5Padding:
			// case AES_ECB_NoPadding:
		case AES_ECB_PKCS5Padding: {
			AssertHelper.isTrue(securityKey.length() >= 16, "The SecurityKey length must be greater than 16");
			//
			SecretKey secretKey = SecurityHelper.createSecretKey(securityKey, SecurityType.AES.getValue());
			result = SecurityHelper.decrypt(value, secretKey, securityType.getValue());
			break;
		}
		default: {
			AssertHelper.unsupported("The SecurityType [" + securityType + "] is unsupported");
			break;
		}
		}
		return result;
	}

	/**
	 * 重置
	 */
	public void reset() {
		// nothing to do
	}

	public Object clone() {
		SecurityProcessorImpl copy = null;
		copy = (SecurityProcessorImpl) super.clone();
		//
		SecurityProcessorImpl.securityTypes = clone(securityTypes);
		return copy;
	}
}
