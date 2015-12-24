package org.openyu.commons.cat.vo.impl;

import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.supporter.AuditBeanSupporter;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.bean.supporter.SeqBeanSupporter;
import org.openyu.commons.lang.event.EventAttach;

/**
 * vo -> po CatPoImpl
 * 
 * isVoClass=true
 */
@XmlRootElement(name = "catImpl")
@XmlAccessorType(XmlAccessType.FIELD)
public class CatImpl extends SeqBeanSupporter implements NamesBean {

	private static final long serialVersionUID = -1693170176219907274L;

	private String id;

	private boolean valid;

	private int age;

	private NamesBean names = new NamesBeanSupporter();

	private AuditBean audit = new AuditBeanSupporter();

	public CatImpl() {
	}

	public String getId() {
		return id;
	}

	/**
	 * 有觸發事件
	 * 
	 * @param id
	 */
	public void setId(String id) {
		EventAttach<String, String> eventAttach = eventAttach(this.id, id);
		//
		fireBeanChanging(this, "id", eventAttach);
		fireBeanChanging(this, CatField.ID, eventAttach);
		this.id = id;
		fireBeanChanged(this, "id", eventAttach);
		fireBeanChanged(this, CatField.ID, eventAttach);
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getAge() {
		return age;
	}

	/**
	 * 有事件觸發
	 * 
	 * @param age
	 */
	public void setAge(int age) {
		EventAttach<Integer, Integer> eventAttach = eventAttach(this.age, age);
		//
		fireBeanChanging(this, "age", eventAttach);
		fireBeanChanging(this, CatField.AGE, eventAttach);
		this.age = age;
		fireBeanChanged(this, "age", eventAttach);
		fireBeanChanged(this, CatField.AGE, eventAttach);
	}

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

	public AuditBean getAudit() {
		return audit;
	}

	public void setAudit(AuditBean audit) {
		this.audit = audit;
	}

	public void barking() {
		System.out.println("喵喵!!!");
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
