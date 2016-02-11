package org.openyu.commons.security;

import java.util.Set;

import org.openyu.commons.processor.BaseProcessor;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 安全性處理器
 * 
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SecurityProcessor extends BaseProcessor {

	String KEY = SecurityProcessor.class.getName();

	/**
	 * 是否安全性
	 * 
	 * @return
	 */
	boolean isSecurity();

	void setSecurity(boolean security);

	/**
	 * 安全性類別
	 * 
	 * @return
	 */
	SecurityType getSecurityType();

	void setSecurityType(SecurityType securityType);

	/**
	 * 安全性key
	 * 
	 * @return
	 */
	String getSecurityKey();

	void setSecurityKey(String securityKey);

	/**
	 * 安全性類別列舉
	 * 
	 * @return
	 */
	Set<SecurityType> getSecurityTypes();

	void setSecurityTypes(Set<SecurityType> securityTypes);

	/**
	 * 加密
	 * 
	 * @param securityTypeValue
	 *            安全性類別
	 * @see SecurityType
	 * @param value
	 * @return
	 */
	byte[] encrypt(String securityTypeValue, byte[] value);

	/**
	 * 加密
	 * 
	 * @param value
	 * @return
	 */
	byte[] encrypt(byte[] value);

	/**
	 * 解密
	 * 
	 * @param securityTypeValue
	 *            安全性類別
	 * @see SecurityType
	 * @param value
	 * @return
	 */
	byte[] decrypt(String securityTypeValue, byte[] value);

	/**
	 * 解密
	 * 
	 * @param value
	 * @return
	 */
	byte[] decrypt(byte[] value);

	/**
	 * 重置
	 */
	void reset();
}
