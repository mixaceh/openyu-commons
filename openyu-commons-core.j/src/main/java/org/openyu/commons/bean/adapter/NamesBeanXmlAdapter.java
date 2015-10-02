package org.openyu.commons.bean.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.jaxb.adapter.LocaleXmlAdapter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.CollectionHelper;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<entry key="zh_TW">測試使用者</entry>
//<entry key="en_US">Test user</entry>
//--------------------------------------------------
public class NamesBeanXmlAdapter extends
		BaseXmlAdapterSupporter<NamesBeanXmlAdapter.NamesBeanList, NamesBean>
{
	public NamesBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class NamesBeanList
	{
		public List<NamesBeanEntry> entry = new LinkedList<NamesBeanEntry>();
	}

	public static class NamesBeanEntry
	{
		@XmlAttribute
		@XmlJavaTypeAdapter(LocaleXmlAdapter.class)
		public Locale key;

		@XmlValue
		public String value;

		public NamesBeanEntry(Locale key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public NamesBeanEntry()
		{}
	}

	// --------------------------------------------------
	public NamesBean unmarshal(NamesBeanList value) throws Exception
	{
		NamesBean result = new NamesBeanSupporter();
		//
		if (value != null)
		{
			for (NamesBeanEntry entry : value.entry)
			{
				result.addName(entry.key, entry.value);
			}
		}
		return result;
	}

	public NamesBeanList marshal(NamesBean value) throws Exception
	{
		NamesBeanList result = new NamesBeanList();
		//
		if (value != null && CollectionHelper.notEmpty(value.getNames()))
		{
			for (LocaleNameBean entry : value.getNames())
			{
				result.entry.add(new NamesBeanEntry(entry.getLocale(), entry.getName()));
			}
		}
		return result;
	}
}
