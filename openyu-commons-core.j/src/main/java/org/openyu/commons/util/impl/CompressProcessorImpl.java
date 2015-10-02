package org.openyu.commons.util.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CompressType;
import org.openyu.commons.util.CompressProcessor;
import org.openyu.commons.util.CompressHelper;
import org.openyu.commons.util.adapter.CompressTypeXmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "compressProcessor")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompressProcessorImpl extends BaseModelSupporter implements
		CompressProcessor {

	private static final long serialVersionUID = 7763775982495411425L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CompressProcessorImpl.class);

	// --------------------------------------------------
	// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	/**
	 * 壓縮類別
	 */
	@XmlJavaTypeAdapter(CompressTypeXmlAdapter.class)
	private static Set<CompressType> compressTypes = new LinkedHashSet<CompressType>();

	// --------------------------------------------------

	/** 是否壓縮 */
	private boolean compress;

	/** 壓縮類別 */
	private CompressType compressType;

	static {
		// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
		compressTypes = EnumHelper.valuesSet(CompressType.class);
	}

	public CompressProcessorImpl() {
	}

	/**
	 * 是否壓縮
	 * 
	 * @return
	 */
	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	/**
	 * 壓縮類別
	 * 
	 * @return
	 */
	public CompressType getCompressType() {
		return compressType;
	}

	public void setCompressType(CompressType compressType) {
		this.compressType = compressType;
	}

	/**
	 * 壓縮類別列舉
	 * 
	 * @return
	 */
	public Set<CompressType> getCompressTypes() {
		if (compressTypes == null) {
			compressTypes = new LinkedHashSet<CompressType>();
		}
		return compressTypes;
	}

	public void setCompressTypes(Set<CompressType> compressTypes) {
		CompressProcessorImpl.compressTypes = compressTypes;
	}

	/**
	 * 壓縮
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	public byte[] compress(String compressTypeValue, byte[] value) {
		CompressType compressType = EnumHelper.valueOf(CompressType.class,
				compressTypeValue);
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		this.compressType = compressType;
		return compress(value);
	}

	/**
	 * 壓縮
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	public byte[] compress(int compressTypeValue, byte[] value) {
		CompressType compressType = EnumHelper.valueOf(CompressType.class,
				compressTypeValue);
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		this.compressType = compressType;
		return compress(value);
	}

	/**
	 * 壓縮
	 * 
	 * @param value
	 * @return
	 */
	public byte[] compress(byte[] value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		if (!compress) {
			return value;
		}
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		//
		switch (compressType) {
		case LZMA: {
			result = CompressHelper.lzma(value);
			break;
		}
		case GZIP: {
			result = CompressHelper.gzip(value);
			break;
		}
		case DEFLATE: {
			result = CompressHelper.deflate(value);
			break;
		}
		case DEFLATER: {
			result = CompressHelper.deflater(value);
			break;
		}
		case SNAPPY: {
			result = CompressHelper.snappy(value);
			break;
		}
		case LZF: {
			result = CompressHelper.lzf(value);
			break;
		}
		case LZ4: {
			result = CompressHelper.lz4(value);
			break;
		}
		default: {
			AssertHelper.unsupported("The CompressType [" + compressType
					+ "] is unsupported");
		}
		}
		// LOGGER.info("Compressed the value with " + compressType);
		return result;
	}

	/**
	 * 解壓
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	public byte[] uncompress(String compressTypeValue, byte[] value) {
		CompressType compressType = EnumHelper.valueOf(CompressType.class,
				compressTypeValue);
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		this.compressType = compressType;
		return uncompress(value);
	}

	/**
	 * 解壓
	 * 
	 * @param compressTypeValue
	 *            壓縮類別
	 * @see CompressType
	 * @param value
	 * @return
	 */
	public byte[] uncompress(int compressTypeValue, byte[] value) {
		CompressType compressType = EnumHelper.valueOf(CompressType.class,
				compressTypeValue);
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		this.compressType = compressType;
		return uncompress(value);
	}

	/**
	 * 解壓
	 * 
	 * @param value
	 * @return
	 */
	public byte[] uncompress(byte[] value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		if (!compress) {
			return value;
		}
		AssertHelper.notNull(compressType, "The CompressType must not be null");
		//
		switch (compressType) {
		case LZMA: {
			result = CompressHelper.unlzma(value);
			break;

		}
		case GZIP: {
			result = CompressHelper.ungzip(value);
			break;
		}
		case DEFLATE: {
			result = CompressHelper.inflate(value);
			break;
		}
		case DEFLATER: {
			result = CompressHelper.inflater(value);
			break;
		}
		case SNAPPY: {
			result = CompressHelper.unsnappy(value);
			break;
		}
		case LZF: {
			result = CompressHelper.unlzf(value);
			break;
		}
		case LZ4: {
			result = CompressHelper.unlz4(value);
			break;
		}
		default: {
			AssertHelper.unsupported("The CompressType [" + compressType
					+ "] is unsupported");
		}
		}
		// LOGGER.info("Uncompressed the value with " + compressType);
		return result;
	}

	/**
	 * 重置
	 */
	public void reset() {
		// nothing to do
	}

	public Object clone() {
		CompressProcessorImpl copy = null;
		copy = (CompressProcessorImpl) super.clone();
		//
		CompressProcessorImpl.compressTypes = clone(compressTypes);
		return copy;
	}
}
