package org.openyu.commons.bean.supporter;

import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.LocaleBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NameBean;
import org.openyu.commons.bean.adapter.LocaleBeanXmlAdapter;
import org.openyu.commons.bean.adapter.NameBeanXmlAdapter;

@XmlRootElement(name = "localeNameBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocaleNameBeanSupporter extends BaseBeanSupporter implements LocaleNameBean {
	private static final long serialVersionUID = -4234172988428042058L;

	@XmlJavaTypeAdapter(LocaleBeanXmlAdapter.class)
	private LocaleBean locale = new LocaleBeanSupporter();

	@XmlJavaTypeAdapter(NameBeanXmlAdapter.class)
	private NameBean name = new NameBeanSupporter();

	public LocaleNameBeanSupporter() {
	}

	public Locale getLocale() {
		return locale.getLocale();
	}

	public void setLocale(Locale locale) {
		this.locale.setLocale(locale);
	}

	public String getName() {
		return name.getName();
	}

	public void setName(String name) {
		this.name.setName(name);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("locale", (locale != null ? getLocale() : null));
		builder.append("name", (name != null ? getName() : null));
		return builder.toString();
	}

	public boolean equals(Object object) {
		if (!(object instanceof LocaleNameBeanSupporter)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		LocaleNameBeanSupporter other = (LocaleNameBeanSupporter) object;
		if (getLocale() == null || other.getLocale() == null) {
			return false;
		}
		return new EqualsBuilder().append(getLocale(), other.getLocale()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getLocale()).toHashCode();
	}

	public Object clone() {
		LocaleNameBeanSupporter copy = null;
		copy = (LocaleNameBeanSupporter) super.clone();
		copy.locale = clone(locale);
		copy.name = clone(name);
		return copy;
	}
}
