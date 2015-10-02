package org.openyu.commons.entity.supporter;

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
import org.openyu.commons.entity.SeqIdAuditNamesEntity;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NamesEntity;
import org.openyu.commons.entity.bridge.NamesEntityBridge;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqIdAuditNamesEntitySupporter extends SeqIdAuditEntitySupporter implements
		SeqIdAuditNamesEntity, Supporter
{

	private static final long serialVersionUID = 1802508357375128968L;

	private NamesEntity names = new NamesEntitySupporter();

	public SeqIdAuditNamesEntitySupporter()
	{}

	@Type(type = "org.openyu.commons.entity.userType.NamesEntityUserType")
	@Column(name = "names", length = 2048)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = NamesEntityBridge.class)
	public Set<LocaleNameEntity> getNames()
	{
		return names.getNames();
	}

	public void setNames(Set<LocaleNameEntity> names)
	{
		this.names.setNames(names);
	}

	/**
	 * 
	 * 用locale作為equlas判斷條件,若有相同locale則不加入
	 * 
	 * @param locale
	 * @param name
	 * @return
	 */
	public boolean addName(Locale locale, String name)
	{
		return names.addName(locale, name);
	}

	public LocaleNameEntity getNameEntry(Locale locale)
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

	@Transient
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
		SeqIdAuditNamesEntitySupporter copy = null;
		copy = (SeqIdAuditNamesEntitySupporter) super.clone();
		copy.names = clone(names);
		return copy;
	}
}
