package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.TrueFalseOption.TrueFalseType;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<trueFalseTypes key="FALSE">false</trueFalseTypes>
//<trueFalseTypes key="TRUE">true</trueFalseTypes>
//--------------------------------------------------
public class TrueFalseTypeXmlAdapter extends
		BaseXmlAdapterSupporter<TrueFalseTypeXmlAdapter.TrueFalseTypeEntry, TrueFalseType>
{

	public TrueFalseTypeXmlAdapter()
	{}

	// --------------------------------------------------
	public static class TrueFalseTypeEntry
	{
		@XmlAttribute
		public String key;

		@XmlValue
		public boolean value;

		public TrueFalseTypeEntry(String key, boolean value)
		{
			this.key = key;
			this.value = value;
		}

		public TrueFalseTypeEntry()
		{}
	}

	// --------------------------------------------------
	public TrueFalseType unmarshal(TrueFalseTypeEntry value) throws Exception
	{
		TrueFalseType result = null;
		//
		if (value != null)
		{
			result = TrueFalseType.valueOf(value.key);
		}
		return result;
	}

	public TrueFalseTypeEntry marshal(TrueFalseType value) throws Exception
	{
		TrueFalseTypeEntry result = null;
		//
		if (value != null)
		{
			result = new TrueFalseTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
