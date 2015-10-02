package org.openyu.commons.entity.supporter;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.SeqNameEntity;
import org.openyu.commons.entity.LocaleEntity;
import org.openyu.commons.entity.NameEntity;
import org.openyu.commons.hibernate.search.bridge.LocaleStringBridge;
import org.openyu.commons.jaxb.adapter.LocaleXmlAdapter;
import org.openyu.commons.mark.Supporter;

@Deprecated
/**
 * 因合併多個欄位成一個欄位,故不用再用此class,處理多個欄位 
 */
@MappedSuperclass
public abstract class SeqNameEntitySupporter extends SeqEntitySupporter implements SeqNameEntity,
		Supporter
{

	private static final long serialVersionUID = 7406642923040326380L;

	private NameEntity name;

	private LocaleEntity locale;

	public SeqNameEntitySupporter()
	{
		this.name = new NameEntitySupporter();
		this.locale = new LocaleEntitySupporter();
	}

	@Column(name = "name", length = 50)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getName()
	{
		return name.getName();
	}

	public void setName(String name)
	{
		this.name.setName(name);
	}

	@XmlJavaTypeAdapter(LocaleXmlAdapter.class)
	@Column(name = "locale", length = 20)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = LocaleStringBridge.class)
	public Locale getLocale()
	{
		return locale.getLocale();
	}

	public void setLocale(Locale locale)
	{
		this.locale.setLocale(locale);
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("locale", getLocale());
		builder.append("name", getName());
		return builder.toString();
	}

	public Object clone()
	{
		SeqNameEntitySupporter copy = null;
		copy = (SeqNameEntitySupporter) super.clone();
		copy.name = clone(name);
		copy.locale = clone(locale);
		return copy;
	}
}
