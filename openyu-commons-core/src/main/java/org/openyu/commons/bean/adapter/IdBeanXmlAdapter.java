package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.IdBean;
import org.openyu.commons.bean.supporter.IdBeanSupporter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<id>WPvQ</id>
//--------------------------------------------------
public class IdBeanXmlAdapter extends
		BaseXmlAdapterSupporter<IdBeanXmlAdapter.IdBeanEntry, IdBean>
{

	public IdBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class IdBeanEntry
	{
		@XmlValue
		public String value;

		public IdBeanEntry(String value)
		{
			this.value = value;
		}

		public IdBeanEntry()
		{}
	}

	// --------------------------------------------------
	public IdBean unmarshal(IdBeanEntry value) throws Exception
	{
		IdBean result = null;
		//
		if (value != null)
		{
			result = new IdBeanSupporter();
			result.setId(value.value);
		}
		return result;
	}

	public IdBeanEntry marshal(IdBean value) throws Exception
	{
		IdBeanEntry result = null;
		//
		if (value != null)
		{
			result = new IdBeanEntry(value.getId());
		}
		return result;
	}
}
