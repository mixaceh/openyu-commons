package org.openyu.commons.entity;

import java.util.Locale;
import java.util.Set;

public interface NamesEntity
{
	String KEY = NamesEntity.class.getName();

	Set<LocaleNameEntity> getNames();

	void setNames(Set<LocaleNameEntity> names);

	//
	boolean addName(Locale locale, String name);

	LocaleNameEntity getNameEntry(Locale locale);

	String getName(Locale locale);

	void setName(Locale locale, String name);

	boolean removeName(Locale locale);

	//
	/**
	 * 名稱,locale採預設,LocaleHelper.getLocale()
	 * 
	 * @return
	 */
	String getName();

	void setName(String name);
}
