package org.openyu.commons.util;

import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.processor.BaseProcessor;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 壓縮處理器
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface CompressProcessor extends BaseProcessor {

	String KEY = CompressProcessor.class.getName();

	/**
	 * 是否壓縮
	 * 
	 * @return
	 */
	boolean isCompress();

	void setCompress(boolean compress);

	/**
	 * 壓縮類別
	 * 
	 * @return
	 */
	CompressType getCompressType();

	void setCompressType(CompressType compressType);

	/**
	 * 壓縮類別列舉
	 * 
	 * @return
	 */
	Set<CompressType> getCompressTypes();

	void setCompressTypes(Set<CompressType> compressTypes);

	/**
	 * 壓縮
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	byte[] compress(String compressTypeValue, byte[] value);

	/**
	 * 壓縮
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	byte[] compress(int compressTypeValue, byte[] value);

	/**
	 * 壓縮
	 * 
	 * @param value
	 * @return
	 */
	byte[] compress(byte[] value);

	/**
	 * 解壓
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	byte[] uncompress(String compressTypeValue, byte[] value);

	/**
	 * 解壓
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	byte[] uncompress(int compressTypeValue, byte[] value);

	/**
	 * 解壓
	 * 
	 * @param value
	 * @return
	 */
	byte[] uncompress(byte[] value);

	/**
	 * 重置
	 */
	void reset();
}
