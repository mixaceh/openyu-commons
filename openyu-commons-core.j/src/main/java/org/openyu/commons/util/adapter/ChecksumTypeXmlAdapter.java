package org.openyu.commons.util.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.ChecksumType;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<checksumTypes key="CRC32">1</checksumTypes>
//--------------------------------------------------
public class ChecksumTypeXmlAdapter
		extends
		BaseXmlAdapterSupporter<ChecksumTypeXmlAdapter.ChecksumTypeEntry, ChecksumType> {

	public ChecksumTypeXmlAdapter() {
	}

	// --------------------------------------------------
	public static class ChecksumTypeEntry {
		@XmlAttribute
		public String key;

		@XmlValue
		public int value;

		public ChecksumTypeEntry(String key, int value) {
			this.key = key;
			this.value = value;
		}

		public ChecksumTypeEntry() {
		}
	}

	// --------------------------------------------------
	public ChecksumType unmarshal(ChecksumTypeEntry value) throws Exception {
		ChecksumType result = null;
		//
		if (value != null) {
			result = ChecksumType.valueOf(value.key);
		}
		return result;
	}

	public ChecksumTypeEntry marshal(ChecksumType value) throws Exception {
		ChecksumTypeEntry result = null;
		//
		if (value != null) {
			result = new ChecksumTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
