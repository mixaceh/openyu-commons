package org.openyu.commons.bean.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.supporter.LocaleNameBeanSupporter;
import org.openyu.commons.jaxb.adapter.LocaleXmlAdapter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<entry key="zh_TW">測試使用者</entry>
//<entry key="en_US">Test user</entry>
//--------------------------------------------------
public class LocaleNameBeanListXmlAdapter
		extends
		BaseXmlAdapterSupporter<LocaleNameBeanListXmlAdapter.LocaleNameBeanList, List<LocaleNameBean>>
{

	public LocaleNameBeanListXmlAdapter()
	{}

	// --------------------------------------------------
	public static class LocaleNameBeanList
	{
		public List<LocaleNameBeanEntry> entry = new LinkedList<LocaleNameBeanEntry>();
	}

	public static class LocaleNameBeanEntry
	{
		@XmlAttribute
		@XmlJavaTypeAdapter(LocaleXmlAdapter.class)
		public Locale key;

		@XmlValue
		public String value;

		public LocaleNameBeanEntry(Locale key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public LocaleNameBeanEntry()
		{}
	}

	// --------------------------------------------------
	public List<LocaleNameBean> unmarshal(LocaleNameBeanList value) throws Exception
	{
		List<LocaleNameBean> result = new LinkedList<LocaleNameBean>();
		//
		if (value != null)
		{
			for (LocaleNameBeanEntry entry : value.entry)
			{
				LocaleNameBean localeNameBean = new LocaleNameBeanSupporter();
				localeNameBean.setLocale(entry.key);
				localeNameBean.setName(entry.value);
				result.add(localeNameBean);
			}
		}
		return result;
	}

	public LocaleNameBeanList marshal(List<LocaleNameBean> value) throws Exception
	{
		LocaleNameBeanList result = new LocaleNameBeanList();
		//
		if (value != null)
		{
			for (LocaleNameBean entry : value)
			{
				result.entry.add(new LocaleNameBeanEntry(entry.getLocale(), entry.getName()));
			}
		}
		return result;
	}
}
