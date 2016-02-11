package org.openyu.commons.jaxb.adapter;

import java.util.Locale;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.LocaleHelper;

/**
 * 區域
 * 
 * object <-> xml
 */
public class LocaleXmlAdapter extends BaseXmlAdapterSupporter<String, Locale>
{

	public LocaleXmlAdapter()
	{}

	public Locale unmarshal(String value) throws Exception
	{
		return LocaleHelper.toLocale(value);
	}

	public String marshal(Locale value) throws Exception
	{
		return LocaleHelper.toString(value);
	}
}
