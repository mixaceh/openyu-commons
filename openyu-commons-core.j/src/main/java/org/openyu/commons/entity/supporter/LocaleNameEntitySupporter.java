package org.openyu.commons.entity.supporter;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.openyu.commons.entity.LocaleEntity;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NameEntity;
import org.openyu.commons.hibernate.search.bridge.LocaleStringBridge;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class LocaleNameEntitySupporter extends BaseEntitySupporter implements LocaleNameEntity, Supporter {

	private static final long serialVersionUID = 892543413733138133L;

	private LocaleEntity locale = new LocaleEntitySupporter();

	private NameEntity name = new NameEntitySupporter();

	public LocaleNameEntitySupporter() {
	}

	@Column(name = "locale", length = 20)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = LocaleStringBridge.class)
	public Locale getLocale() {
		return locale.getLocale();
	}

	public void setLocale(Locale locale) {
		this.locale.setLocale(locale);
	}

	@Column(name = "name", length = 50)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getName() {
		return name.getName();
	}

	public void setName(String name) {
		this.name.setName(name);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("locale", getLocale());
		builder.append("name", getName());
		return builder.toString();
	}

	public boolean equals(Object object) {
		if (!(object instanceof LocaleNameEntitySupporter)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		LocaleNameEntitySupporter other = (LocaleNameEntitySupporter) object;
		if (getLocale() == null || other.getLocale() == null) {
			return false;
		}
		return new EqualsBuilder().append(getLocale(), other.getLocale()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getLocale()).toHashCode();
	}

	public Object clone() {
		LocaleNameEntitySupporter copy = null;
		copy = (LocaleNameEntitySupporter) super.clone();
		copy.locale = clone(locale);
		copy.name = clone(name);
		return copy;
	}
}
