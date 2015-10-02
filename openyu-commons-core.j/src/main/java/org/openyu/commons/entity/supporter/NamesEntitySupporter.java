package org.openyu.commons.entity.supporter;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NamesEntity;
import org.openyu.commons.entity.bridge.NamesEntityBridge;
import org.openyu.commons.entity.supporter.BaseEntitySupporter;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.LocaleHelper;

@MappedSuperclass
public class NamesEntitySupporter extends BaseEntitySupporter implements NamesEntity, Supporter
{

	private static final long serialVersionUID = -5132648217140984849L;

	private Set<LocaleNameEntity> names = new LinkedHashSet<LocaleNameEntity>();

	public NamesEntitySupporter()
	{}

	@Type(type = "org.openyu.commons.entity.userType.NamesEntityUserType")
	@Column(name = "names", length = 2048)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = NamesEntityBridge.class)
	public Set<LocaleNameEntity> getNames()
	{
		return names;
	}

	public void setNames(Set<LocaleNameEntity> names)
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
			LocaleNameEntity localeNameEntity = new LocaleNameEntitySupporter();
			localeNameEntity.setLocale(locale);
			localeNameEntity.setName(name);
			result = names.add(localeNameEntity);
		}
		return result;
	}

	public LocaleNameEntity getNameEntry(Locale locale)
	{
		LocaleNameEntity result = null;
		if (names != null)
		{
			for (LocaleNameEntity localeNameEntity : names)
			{
				if (localeNameEntity.getLocale().equals(locale))
				{
					result = localeNameEntity;
					break;
				}
			}
		}
		return result;
	}

	public String getName(Locale locale)
	{
		String result = null;
		LocaleNameEntity localeNameEntity = getNameEntry(locale);
		if (localeNameEntity != null)
		{
			result = localeNameEntity.getName();
		}
		return result;
	}

	public void setName(Locale locale, String name)
	{
		LocaleNameEntity localeNameEntity = getNameEntry(locale);
		if (localeNameEntity != null)
		{
			localeNameEntity.setName(name);
		}
		else
		{
			addName(locale, name);
		}
	}

	public boolean removeName(Locale locale)
	{
		boolean result = false;
		if (names != null)
		{
			LocaleNameEntity localeNameEntity = getNameEntry(locale);
			if (localeNameEntity != null)
			{
				result = names.remove(localeNameEntity);
			}
		}
		return result;
	}

	@Transient
	public String getName()
	{
		//return getName(LocaleHelper.getLocale());
		//
		String result = getName(LocaleHelper.getLocale());
		if (result == null)
		{
			if (CollectionHelper.notEmpty(names))
			{
				for (LocaleNameEntity entry : names)
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
		NamesEntitySupporter copy = null;
		copy = (NamesEntitySupporter) super.clone();
		copy.names = clone(names);
		return copy;
	}
}
