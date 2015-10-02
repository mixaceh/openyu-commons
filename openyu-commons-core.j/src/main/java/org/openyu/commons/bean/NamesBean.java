package org.openyu.commons.bean;

import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * NamesBean => NamesEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface NamesBean
{
	String KEY = NamesBean.class.getName();

	Set<LocaleNameBean> getNames();

	void setNames(Set<LocaleNameBean> names);

	//
	boolean addName(Locale locale, String name);

	LocaleNameBean getNameEntry(Locale locale);

	String getName(Locale locale);

	void setName(Locale locale, String name);

	boolean removeName(Locale locale);

	/**
	 * 名稱,locale採預設 LocaleHelper.getLocale()
	 * 
	 * @return
	 */
	String getName();

	void setName(String name);
}
