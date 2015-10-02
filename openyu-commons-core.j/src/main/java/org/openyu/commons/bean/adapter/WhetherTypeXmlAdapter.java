package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.WhetherOption.WhetherType;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<whetherTypes key="ALL">all</whetherTypes>
//<whetherTypes key="FALSE">false</whetherTypes>
//<whetherTypes key="TRUE">true</whetherTypes>
//--------------------------------------------------
public class WhetherTypeXmlAdapter extends
		BaseXmlAdapterSupporter<WhetherTypeXmlAdapter.WhetherTypeEntry, WhetherType>
{

	public WhetherTypeXmlAdapter()
	{}

	// --------------------------------------------------
	public static class WhetherTypeEntry
	{
		@XmlAttribute
		public String key;

		@XmlValue
		public String value;

		public WhetherTypeEntry(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public WhetherTypeEntry()
		{}
	}

	// --------------------------------------------------
	public WhetherType unmarshal(WhetherTypeEntry value) throws Exception
	{
		WhetherType result = null;
		//
		if (value != null)
		{
			result = WhetherType.valueOf(value.key);
		}
		return result;
	}

	public WhetherTypeEntry marshal(WhetherType value) throws Exception
	{
		WhetherTypeEntry result = null;
		//
		if (value != null)
		{
			result = new WhetherTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
