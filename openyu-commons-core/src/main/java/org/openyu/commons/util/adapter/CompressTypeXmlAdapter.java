package org.openyu.commons.util.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.CompressType;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<compressTypes key="LZMA">1</compressTypes>
//--------------------------------------------------
public class CompressTypeXmlAdapter
		extends
		BaseXmlAdapterSupporter<CompressTypeXmlAdapter.CompressTypeEntry, CompressType> {

	public CompressTypeXmlAdapter() {
	}

	// --------------------------------------------------
	public static class CompressTypeEntry {
		@XmlAttribute
		public String key;

		@XmlValue
		public int value;

		public CompressTypeEntry(String key, int value) {
			this.key = key;
			this.value = value;
		}

		public CompressTypeEntry() {
		}
	}

	// --------------------------------------------------
	public CompressType unmarshal(CompressTypeEntry value) throws Exception {
		CompressType result = null;
		//
		if (value != null) {
			result = CompressType.valueOf(value.key);
		}
		return result;
	}

	public CompressTypeEntry marshal(CompressType value) throws Exception {
		CompressTypeEntry result = null;
		//
		if (value != null) {
			result = new CompressTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
