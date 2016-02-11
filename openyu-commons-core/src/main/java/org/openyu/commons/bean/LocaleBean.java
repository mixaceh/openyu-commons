package org.openyu.commons.bean;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * LocaleBean => LocaleEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface LocaleBean extends BaseBean
{

	String KEY = LocaleBean.class.getName();

	void setLocale(Locale locale);

	Locale getLocale();

}
