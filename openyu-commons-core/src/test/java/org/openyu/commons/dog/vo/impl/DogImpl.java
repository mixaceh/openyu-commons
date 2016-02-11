package org.openyu.commons.dog.vo.impl;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.adapter.NamesBeanXmlAdapter;
import org.openyu.commons.bean.supporter.AuditBeanSupporter;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;

/**
 * vo -> po DogPoImpl
 * 
 * isVoClass=false
 */
@XmlRootElement(name = "dogImpl")
@XmlAccessorType(XmlAccessType.FIELD)
public class DogImpl implements NamesBean, Serializable {

	private static final long serialVersionUID = -2594686174664528860L;

	private Long seq;

	private Integer version;

	private String id;

	private Boolean valid;

	@XmlJavaTypeAdapter(NamesBeanXmlAdapter.class)
	private NamesBean names = new NamesBeanSupporter();// 新的方式

	// @XmlElement(type = LocaleNameBeanSetXmlAdapter.class)
	// private Set<LocaleNameBean> names = new
	// LinkedHashSet<LocaleNameBean>();// 原來的方式

	@XmlElement(type = AuditBeanSupporter.class)
	private AuditBean audit = new AuditBeanSupporter();

	/**
	 * 描述
	 */
	@XmlJavaTypeAdapter(NamesBeanXmlAdapter.class)
	private NamesBean descriptions = new NamesBeanSupporter();

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	// public Set<LocaleNameBean> getNames() {
	// return names;
	// }
	//
	// public void setNames(Set<LocaleNameBean> names) {
	// this.names = names;
	// }

	// /**
	// *
	// * 用locale作為equlas判斷條件,若有相同locale則不加入
	// *
	// * @param locale
	// * @param name
	// * @return
	// */
	// public boolean addName(Locale locale, String name) {
	// boolean result = false;
	// if (names != null) {
	// LocaleNameBean localeNameBean = new LocaleNameBeanSupporter();
	// localeNameBean.setLocale(locale);
	// localeNameBean.setName(name);
	// result = names.add(localeNameBean);
	// }
	// return result;
	// }
	//
	// public LocaleNameBean getNameEntry(Locale locale) {
	// LocaleNameBean result = null;
	// if (names != null) {
	// for (LocaleNameBean localeNameBean : names) {
	// if (localeNameBean.getLocale().equals(locale)) {
	// result = localeNameBean;
	// break;
	// }
	// }
	// }
	// return result;
	// }
	//
	// public String getName(Locale locale) {
	// String result = null;
	// LocaleNameBean localeNameBean = getNameEntry(locale);
	// if (localeNameBean != null) {
	// result = localeNameBean.getName();
	// }
	// return result;
	// }
	//
	// public void setName(Locale locale, String name) {
	// LocaleNameBean localeNameBean = getNameEntry(locale);
	// if (localeNameBean != null) {
	// localeNameBean.setName(name);
	// } else {
	// addName(locale, name);
	// }
	// }
	//
	// public String getName() {
	// return getName(Locale.getDefault());
	// }
	//
	// public void setName(String name) {
	// setName(Locale.getDefault(), name);
	// }
	//
	// public boolean removeName(Locale locale) {
	// boolean result = false;
	// if (names != null) {
	// LocaleNameBean localeNameBean = getNameEntry(locale);
	// if (localeNameBean != null) {
	// result = names.remove(localeNameBean);
	// }
	// }
	// return result;
	// }

	public Set<LocaleNameBean> getNames() {
		return names.getNames();
	}

	public void setNames(Set<LocaleNameBean> names) {
		this.names.setNames(names);
	}

	public boolean addName(Locale locale, String name) {
		return names.addName(locale, name);
	}

	public LocaleNameBean getNameEntry(Locale locale) {
		return names.getNameEntry(locale);
	}

	public String getName(Locale locale) {
		return names.getName(locale);
	}

	public void setName(Locale locale, String name) {
		names.setName(locale, name);
	}

	public boolean removeName(Locale locale) {
		return names.removeName(locale);
	}

	public String getName() {
		return names.getName();
	}

	public void setName(String name) {
		names.setName(name);
	}

	public String getDescription() {
		return descriptions.getName();
	}

	public void setDescription(String description) {
		descriptions.setName(description);
	}

	public String getDescription(Locale locale) {
		return descriptions.getName(locale);
	}

	public void setDescription(Locale locale, String description) {
		descriptions.setName(locale, description);
	}

	public Set<LocaleNameBean> getDescriptions() {
		return descriptions.getNames();
	}

	public void setDescriptions(Set<LocaleNameBean> descriptions) {
		this.descriptions.setNames(descriptions);
	}

	public void barking() {
		System.out.println("汪汪!!!");
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
