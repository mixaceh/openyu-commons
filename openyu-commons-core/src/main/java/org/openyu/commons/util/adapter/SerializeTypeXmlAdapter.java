package org.openyu.commons.util.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.SerializeType;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<serializeTypes key="JDK">2</serializeTypes>
//--------------------------------------------------
public class SerializeTypeXmlAdapter
		extends
		BaseXmlAdapterSupporter<SerializeTypeXmlAdapter.SerializeTypeEntry, SerializeType> {

	public SerializeTypeXmlAdapter() {
	}

	// --------------------------------------------------
	public static class SerializeTypeEntry {
		@XmlAttribute
		public String key;

		@XmlValue
		public int value;

		public SerializeTypeEntry(String key, int value) {
			this.key = key;
			this.value = value;
		}

		public SerializeTypeEntry() {
		}
	}

	// --------------------------------------------------
	public SerializeType unmarshal(SerializeTypeEntry value) throws Exception {
		SerializeType result = null;
		//
		if (value != null) {
			result = SerializeType.valueOf(value.key);
		}
		return result;
	}

	public SerializeTypeEntry marshal(SerializeType value) throws Exception {
		SerializeTypeEntry result = null;
		//
		if (value != null) {
			result = new SerializeTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
