package org.openyu.commons.bean.supporter;

import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.bean.IdBean;
import org.openyu.commons.bean.IdNamesBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.adapter.NamesBeanXmlAdapter;

@XmlRootElement(name = "idNamesBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdNamesBeanSupporter extends BaseBeanSupporter implements IdNamesBean
{

	private static final long serialVersionUID = -6331913209274884905L;

	@XmlElement(type = IdBeanSupporter.class)
	private IdBean id = new IdBeanSupporter();

	@XmlJavaTypeAdapter(NamesBeanXmlAdapter.class)
	private NamesBean names = new NamesBeanSupporter();

	public IdNamesBeanSupporter()
	{}

	public String getId()
	{
		return id.getId();
	}

	public void setId(String id)
	{
		this.id.setId(id);
	}

	public String getDataId()
	{
		return id.getDataId();
	}

	public void setDataId(String dataId)
	{
		this.id.setDataId(dataId);
	}

	public boolean isOnly()
	{
		return id.isOnly();
	}

	public void setOnly(boolean only)
	{
		this.id.setOnly(only);
	}

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
		builder.append("id", (id != null ? getId() : null));
		append(builder, "names", names);
		return builder.toString();
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof IdNamesBeanSupporter))
		{
			return false;
		}
		if (this == object)
		{
			return true;
		}
		IdNamesBeanSupporter other = (IdNamesBeanSupporter) object;
		if (getId() == null || other.getId() == null)
		{
			return false;
		}
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public Object clone()
	{
		IdNamesBeanSupporter copy = null;
		copy = (IdNamesBeanSupporter) super.clone();
		copy.id = clone(id);
		copy.names = clone(names);
		return copy;
	}
}
