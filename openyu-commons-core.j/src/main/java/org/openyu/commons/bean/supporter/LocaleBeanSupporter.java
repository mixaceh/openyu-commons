package org.openyu.commons.bean.supporter;

import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.LocaleBean;
import org.openyu.commons.jaxb.adapter.LocaleXmlAdapter;

@XmlRootElement(name = "localeBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocaleBeanSupporter extends BaseBeanSupporter implements LocaleBean {

	private static final long serialVersionUID = 4434742135049229931L;

	@XmlJavaTypeAdapter(LocaleXmlAdapter.class)
	private Locale locale;

	public LocaleBeanSupporter() {
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("locale", locale);
		return builder.toString();
	}

	public Object clone() {
		LocaleBeanSupporter copy = null;
		copy = (LocaleBeanSupporter) super.clone();
		copy.locale = clone(locale);
		return copy;
	}
}
