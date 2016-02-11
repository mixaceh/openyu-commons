package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * LocaleNameBean => LocaleNameEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface LocaleNameBean extends LocaleBean, NameBean
{
	String KEY = LocaleNameBean.class.getName();
}
