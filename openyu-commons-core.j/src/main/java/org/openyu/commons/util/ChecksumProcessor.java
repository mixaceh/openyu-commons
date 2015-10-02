package org.openyu.commons.util;

import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.processor.BaseProcessor;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 檢查碼處理器
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface ChecksumProcessor extends BaseProcessor {

	String KEY = ChecksumProcessor.class.getName();

	/**
	 * 是否檢查碼
	 * 
	 * @return
	 */
	boolean isChecksum();

	void setChecksum(boolean checksum);

	/**
	 * 檢查碼類別
	 * 
	 * @return
	 */
	ChecksumType getChecksumType();

	void setChecksumType(ChecksumType checksumType);

	/**
	 * 檢查碼key
	 * 
	 * @return
	 */
	String getChecksumKey();

	void setChecksumKey(String checksumKey);

	/**
	 * 檢查碼類別列舉
	 * 
	 * @return
	 */
	Set<ChecksumType> getChecksumTypes();

	void setChecksumTypes(Set<ChecksumType> checksumTypes);

	/**
	 * 檢查碼
	 * 
	 * @param checksumTypeValue
	 *            檢查碼類別
	 * @see ChecksumType
	 * @param value
	 * @return
	 */
	long checksum(String checksumTypeValue, byte[] value);

	/**
	 * 檢查碼
	 * 
	 * @param checksumTypeValue
	 *            檢查碼類別
	 * @see ChecksumType
	 * @param value
	 * @return
	 */
	long checksum(int checksumTypeValue, byte[] value);

	/**
	 * 檢查碼
	 * 
	 * @param value
	 * @return
	 */
	long checksum(byte[] value);

	/**
	 * 重置
	 */
	void reset();
}
