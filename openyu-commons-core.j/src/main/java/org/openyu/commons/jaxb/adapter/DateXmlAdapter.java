package org.openyu.commons.jaxb.adapter;

import java.util.Date;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.DateHelper;

/**
 * 日期
 * 
 * object <-> xml
 */
public class DateXmlAdapter extends BaseXmlAdapterSupporter<String, Date>
{

	public DateXmlAdapter()
	{}

	public Date unmarshal(String value) throws Exception
	{
		return DateHelper.toDate(value, DateHelper.DATE_TIME_MILLS_PATTERN);
	}

	public String marshal(Date value) throws Exception
	{
		return DateHelper.toString(value, DateHelper.DATE_TIME_MILLS_PATTERN);
	}
}
