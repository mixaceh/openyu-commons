package org.openyu.commons.util.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.misc.UnsafeHelper;
import org.openyu.commons.processor.supporter.BaseProcessorSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.ChecksumType;
import org.openyu.commons.util.ChecksumProcessor;
import org.openyu.commons.util.ChecksumHelper;
import org.openyu.commons.util.adapter.ChecksumTypeXmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "checksumProcessor")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChecksumProcessorImpl extends BaseProcessorSupporter implements
		ChecksumProcessor {

	private static final long serialVersionUID = 7763775982495411425L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(ChecksumProcessorImpl.class);

	// --------------------------------------------------
	// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	/**
	 * 檢查碼類別
	 */
	@XmlJavaTypeAdapter(ChecksumTypeXmlAdapter.class)
	private static Set<ChecksumType> checksumTypes = new LinkedHashSet<ChecksumType>();

	// --------------------------------------------------

	/** 是否檢查碼 */
	private boolean checksum;

	/** 檢查碼類別 */
	private ChecksumType checksumType;

	/** 檢查碼key */
	private String checksumKey;

	static {
		// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
		checksumTypes = EnumHelper.valuesSet(ChecksumType.class);
	}

	public ChecksumProcessorImpl() {
	}

	/**
	 * 是否檢查碼
	 * 
	 * @return
	 */
	public boolean isChecksum() {
		return checksum;
	}

	public void setChecksum(boolean checksum) {
		this.checksum = checksum;
	}

	/**
	 * 檢查碼類別
	 * 
	 * @return
	 */
	public ChecksumType getChecksumType() {
		return checksumType;
	}

	public void setChecksumType(ChecksumType checksumType) {
		this.checksumType = checksumType;
	}

	/**
	 * 檢查碼key
	 * 
	 * @return
	 */
	public String getChecksumKey() {
		return checksumKey;
	}

	public void setChecksumKey(String checksumKey) {
		this.checksumKey = checksumKey;
	}

	/**
	 * 檢查碼類別列舉
	 * 
	 * @return
	 */
	public Set<ChecksumType> getChecksumTypes() {
		if (checksumTypes == null) {
			checksumTypes = new LinkedHashSet<ChecksumType>();
		}
		return checksumTypes;
	}

	public void setChecksumTypes(Set<ChecksumType> checksumTypes) {
		ChecksumProcessorImpl.checksumTypes = checksumTypes;
	}

	/**
	 * 檢查碼
	 * 
	 * @param checksumTypeValue
	 *            檢查碼類別
	 * @see ChecksumType
	 * @param value
	 * @return
	 */
	public long checksum(String checksumTypeValue, byte[] value) {
		ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
				checksumTypeValue);
		AssertHelper.notNull(checksumType, "The ChecksumType must not be null");
		this.checksumType = checksumType;
		return checksum(value);
	}

	/**
	 * 檢查碼
	 * 
	 * @param checksumTypeValue
	 *            檢查碼類別
	 * @see ChecksumType
	 * @param value
	 * @return
	 */
	public long checksum(int checksumTypeValue, byte[] value) {
		ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
				checksumTypeValue);
		AssertHelper.notNull(checksumType, "The ChecksumType must not be null");
		this.checksumType = checksumType;
		return checksum(value);
	}

	/**
	 * 檢查碼
	 * 
	 * @param value
	 * @return
	 */
	public long checksum(byte[] value) {
		long result = 0;
		//
		if (!checksum){
			return result;
		}
		AssertHelper.notNull(checksumType, "The ChecksumType must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		switch (checksumType) {
		case CRC32: {
			byte[] buf = value;
			if (checksumKey != null) {
				// buf = ArrayHelper.add(buf,
				// ByteHelper.toByteArray(checksumKey));
				byte[] checksumKeyArray = ByteHelper.toByteArray(checksumKey);
				buf = UnsafeHelper
						.putByteArray(buf, buf.length, checksumKeyArray);
			}
			result = ChecksumHelper.crc32(buf);
			break;

		}
		case ADLER32: {
			byte[] buf = value;
			if (checksumKey != null) {
				// buf = ArrayHelper.add(buf,
				// ByteHelper.toByteArray(checksumKey));
				byte[] checksumKeyArray = ByteHelper.toByteArray(checksumKey);
				buf = UnsafeHelper
						.putByteArray(buf, buf.length, checksumKeyArray);
			}
			result = ChecksumHelper.adler32(buf);
			break;
		}
		default: {
			AssertHelper.unsupported("The ChecksumType [" + checksumType
					+ "] is unsupported");
		}
		}
		// LOGGER.info("Checksum the value with " + checksumType);
		return result;
	}

	/**
	 * 重置
	 */
	public void reset() {
		// nothing to do
	}

	// execute方法已經有了, executeAsXxx先不使用, 太多方法了
	// /**
	// * as a byte[]
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public byte[] executeAsBytes(String checksumTypeValue, byte[] value,
	// String checksumKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return ByteHelper.toByteArray(execute(checksumType, value, checksumKey));
	// }
	//
	// /**
	// * as a byte[]
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public byte[] executeAsBytes(int checksumTypeValue, byte[] value,
	// String checksumKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return ByteHelper.toByteArray(execute(checksumType, value, checksumKey));
	// }
	//
	// /**
	// * as a byte[]
	// *
	// * @param checksumType
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public byte[] executeAsBytes(ChecksumType checksumType, byte[] value,
	// String checksumKey) {
	// return ByteHelper.toByteArray(execute(checksumType, value, checksumKey));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public String executeAsHex(String checksumTypeValue, byte[] value,
	// String checksumKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return EncodingHelper.encodeHexString(ByteHelper.toByteArray(execute(
	// checksumType, value, checksumKey)));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public String executeAsHex(int checksumTypeValue, byte[] value,
	// String checksumKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return EncodingHelper.encodeHexString(ByteHelper.toByteArray(execute(
	// checksumType, value, checksumKey)));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumType
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param value
	// * @param checksumKey
	// * @return
	// */
	// public String executeAsHex(ChecksumType checksumType, byte[] value,
	// String checksumKey) {
	// return EncodingHelper.encodeHexString(ByteHelper.toByteArray(execute(
	// checksumType, value, checksumKey)));
	// }

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("checksum", checksum);
		builder.append("checksumType", checksumType);
		builder.append("checksumKey", checksumKey);
		//
		builder.append("checksumTypes", checksumTypes);
		return builder.toString();
	}

	public Object clone() {
		ChecksumProcessorImpl copy = null;
		copy = (ChecksumProcessorImpl) super.clone();
		//
		ChecksumProcessorImpl.checksumTypes = clone(checksumTypes);
		return copy;
	}

}
