package org.openyu.commons.bean.adapter;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
public class LocaleNameBeanSetXmlAdapter
		extends
		BaseXmlAdapterSupporter<LocaleNameBeanSetXmlAdapter.LocaleNameBeanList, Set<LocaleNameBean>>
{

	public LocaleNameBeanSetXmlAdapter()
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
	public Set<LocaleNameBean> unmarshal(LocaleNameBeanList value) throws Exception
	{
		Set<LocaleNameBean> result = new LinkedHashSet<LocaleNameBean>();
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

	public LocaleNameBeanList marshal(Set<LocaleNameBean> value) throws Exception
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
