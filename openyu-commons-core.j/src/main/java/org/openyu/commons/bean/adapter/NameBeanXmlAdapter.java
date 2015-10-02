package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.NameBean;
import org.openyu.commons.bean.supporter.NameBeanSupporter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<name>TEST_NAME</name>
//--------------------------------------------------
public class NameBeanXmlAdapter extends
		BaseXmlAdapterSupporter<NameBeanXmlAdapter.NameBeanEntry, NameBean>
{

	public NameBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class NameBeanEntry
	{
		@XmlValue
		public String value;

		public NameBeanEntry(String value)
		{
			this.value = value;
		}

		public NameBeanEntry()
		{}
	}

	// --------------------------------------------------
	public NameBean unmarshal(NameBeanEntry value) throws Exception
	{
		NameBean result = null;
		//
		if (value != null)
		{
			result = new NameBeanSupporter();
			result.setName(value.value);
		}
		return result;
	}

	public NameBeanEntry marshal(NameBean value) throws Exception
	{
		NameBeanEntry result = null;
		//
		if (value != null)
		{
			result = new NameBeanEntry(value.getName());
		}
		return result;
	}
}
