package org.openyu.commons.security.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.security.SecurityType;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<securityTypes key="DES_ECB_PKCS5Padding">DES/ECB/PKCS5Padding</securityTypes>
//--------------------------------------------------
public class SecurityTypeXmlAdapter
		extends
		BaseXmlAdapterSupporter<SecurityTypeXmlAdapter.SecurityTypeEntry, SecurityType> {

	public SecurityTypeXmlAdapter() {
	}

	// --------------------------------------------------
	public static class SecurityTypeEntry {
		@XmlAttribute
		public String key;

		@XmlValue
		public String value;

		public SecurityTypeEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public SecurityTypeEntry() {
		}
	}

	// --------------------------------------------------
	public SecurityType unmarshal(SecurityTypeEntry value) throws Exception {
		SecurityType result = null;
		//
		if (value != null) {
			result = SecurityType.valueOf(value.key);
		}
		return result;
	}

	public SecurityTypeEntry marshal(SecurityType value) throws Exception {
		SecurityTypeEntry result = null;
		//
		if (value != null) {
			result = new SecurityTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
