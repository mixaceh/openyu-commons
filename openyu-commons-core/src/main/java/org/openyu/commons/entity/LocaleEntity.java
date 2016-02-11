package org.openyu.commons.entity;

import java.util.Locale;

public interface LocaleEntity extends BaseEntity
{

	String KEY = LocaleEntity.class.getName();

	void setLocale(Locale locale);

	Locale getLocale();

}
