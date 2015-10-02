package org.openyu.commons.bean.supporter;

import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.bean.SeqIdAuditNamesBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.adapter.NamesBeanXmlAdapter;

@XmlRootElement(name = "seqIdAuditNamesBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeqIdAuditNamesBeanSupporter extends SeqIdAuditBeanSupporter implements
		SeqIdAuditNamesBean
{

	private static final long serialVersionUID = 5050470445038582503L;

	@XmlJavaTypeAdapter(NamesBeanXmlAdapter.class)
	private NamesBean names = new NamesBeanSupporter();

	public SeqIdAuditNamesBeanSupporter()
	{}

	public Set<LocaleNameBean> getNames()
	{
		return names.getNames();
	}

	public void setNames(Set<LocaleNameBean> names)
	{
		this.names.setNames(names);
	}

	public boolean addName(Locale locale, String name)
	{
		return names.addName(locale, name);
	}

	public LocaleNameBean getNameEntry(Locale locale)
	{
		return names.getNameEntry(locale);
	}

	public String getName(Locale locale)
	{
		return names.getName(locale);
	}

	public void setName(Locale locale, String name)
	{
		names.setName(locale, name);
	}

	public boolean removeName(Locale locale)
	{
		return names.removeName(locale);
	}

	public String getName()
	{
		return names.getName();
	}

	public void setName(String name)
	{
		names.setName(name);
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
		SeqIdAuditNamesBeanSupporter copy = null;
		copy = (SeqIdAuditNamesBeanSupporter) super.clone();
		copy.names = clone(names);
		return copy;
	}
}
