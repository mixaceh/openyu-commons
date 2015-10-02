package org.openyu.commons.bean.supporter;

import java.util.LinkedHashSet;

import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.adapter.LocaleNameBeanSetXmlAdapter;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.LocaleHelper;

@XmlRootElement(name = "namesBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class NamesBeanSupporter extends BaseBeanSupporter implements NamesBean
{

	private static final long serialVersionUID = 6621024789861415524L;

	@XmlJavaTypeAdapter(LocaleNameBeanSetXmlAdapter.class)
	private Set<LocaleNameBean> names = new LinkedHashSet<LocaleNameBean>();

	public NamesBeanSupporter()
	{}

	public Set<LocaleNameBean> getNames()
	{
		return names;
	}

	public void setNames(Set<LocaleNameBean> names)
	{
		this.names = names;
	}

	/**
	 * 
	 * 用locale 作為equlas 判斷條件,若有相同locale則不加入
	 * 
	 * @param locale
	 * @param name
	 * @return
	 */
	public boolean addName(Locale locale, String name)
	{
		boolean result = false;
		if (names != null)
		{
			LocaleNameBean localeNameBean = new LocaleNameBeanSupporter();
			localeNameBean.setLocale(locale);
			localeNameBean.setName(name);
			result = names.add(localeNameBean);
		}
		return result;
	}

	public LocaleNameBean getNameEntry(Locale locale)
	{
		LocaleNameBean result = null;
		if (CollectionHelper.notEmpty(names))
		{
			for (LocaleNameBean localeNameBean : names)
			{
				if (localeNameBean.getLocale().equals(locale))
				{
					result = localeNameBean;
					break;
				}
			}
		}
		return result;
	}

	public String getName(Locale locale)
	{
		String result = null;
		LocaleNameBean localeNameBean = getNameEntry(locale);
		if (localeNameBean != null)
		{
			result = localeNameBean.getName();
		}
		return result;
	}

	public void setName(Locale locale, String name)
	{
		LocaleNameBean localeNameBean = getNameEntry(locale);
		if (localeNameBean != null)
		{
			localeNameBean.setName(name);
		}
		else
		{
			addName(locale, name);
		}
	}

	public boolean removeName(Locale locale)
	{
		boolean result = false;
		LocaleNameBean localeNameBean = getNameEntry(locale);
		if (localeNameBean != null)
		{
			result = names.remove(localeNameBean);
		}
		return result;
	}

	public String getName()
	{
		//return getName(LocaleHelper.getLocale());
		//
		String result = getName(LocaleHelper.getLocale());
		if (result == null)
		{
			if (CollectionHelper.notEmpty(names))
			{
				for (LocaleNameBean entry : names)
				{
					//取第一個
					result = entry.getName();
					break;
				}
			}
		}
		return result;
	}

	public void setName(String name)
	{
		setName(LocaleHelper.getLocale(), name);
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		append(builder, "names", names);
		return builder.toString();
	}

	public Object clone()
	{
		NamesBeanSupporter copy = null;
		copy = (NamesBeanSupporter) super.clone();
		copy.names = clone(names);
		return copy;
	}
}
