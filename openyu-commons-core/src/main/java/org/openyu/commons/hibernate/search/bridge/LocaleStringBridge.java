package org.openyu.commons.hibernate.search.bridge;

import java.util.Locale;

import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;
//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
import org.openyu.commons.util.LocaleHelper;

public class LocaleStringBridge extends BaseStringBridgeSupporter
{

	public LocaleStringBridge()
	{}

	public String objectToString(Object value)
	{
		String result = null;
		if (value instanceof Locale)
		{
			Locale locale = (Locale) value;
			result = LocaleHelper.toString(locale);
		}
		return result;
	}
}
