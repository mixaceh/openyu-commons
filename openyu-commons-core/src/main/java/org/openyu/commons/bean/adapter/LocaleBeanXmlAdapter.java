package org.openyu.commons.bean.adapter;

import java.util.Locale;

import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openyu.commons.bean.LocaleBean;
import org.openyu.commons.bean.supporter.LocaleBeanSupporter;

import org.openyu.commons.jaxb.adapter.LocaleXmlAdapter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<locale>zh_TW</locale>
//--------------------------------------------------
public class LocaleBeanXmlAdapter extends
		BaseXmlAdapterSupporter<LocaleBeanXmlAdapter.LocaleBeanEntry, LocaleBean>
{

	public LocaleBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class LocaleBeanEntry
	{
		@XmlValue
		@XmlJavaTypeAdapter(LocaleXmlAdapter.class)
		public Locale value;

		public LocaleBeanEntry(Locale value)
		{
			this.value = value;
		}

		public LocaleBeanEntry()
		{}
	}

	// --------------------------------------------------
	public LocaleBean unmarshal(LocaleBeanEntry value) throws Exception
	{
		LocaleBean result = null;
		//
		if (value != null)
		{
			result = new LocaleBeanSupporter();
			result.setLocale(value.value);
		}
		return result;
	}

	public LocaleBeanEntry marshal(LocaleBean value) throws Exception
	{
		LocaleBeanEntry result = null;
		//
		if (value != null)
		{
			result = new LocaleBeanEntry(value.getLocale());
		}
		return result;
	}
}
