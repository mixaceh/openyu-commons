package org.openyu.commons.entity.supporter;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.openyu.commons.entity.LocaleEntity;
import org.openyu.commons.hibernate.search.bridge.LocaleStringBridge;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class LocaleEntitySupporter extends BaseEntitySupporter implements LocaleEntity, Supporter
{

	private static final long serialVersionUID = -4988143993915077794L;

	private Locale locale;

	public LocaleEntitySupporter()
	{}

	@Column(name = "locale", length = 20)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = LocaleStringBridge.class)
	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("locale", locale);
		return builder.toString();
	}

	public Object clone()
	{
		LocaleEntitySupporter copy = null;
		copy = (LocaleEntitySupporter) super.clone();
		copy.locale = clone(locale);
		return copy;
	}
}
