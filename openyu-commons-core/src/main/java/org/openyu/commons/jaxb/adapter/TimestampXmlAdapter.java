package org.openyu.commons.jaxb.adapter;

import java.sql.Timestamp;

import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;
import org.openyu.commons.util.TimestampHelper;

/**
 * 時間戳
 * 
 * object <-> xml
 */
public class TimestampXmlAdapter extends BaseXmlAdapterSupporter<String, Timestamp>
{

	public TimestampXmlAdapter()
	{}

	public Timestamp unmarshal(String value) throws Exception
	{
		return TimestampHelper.toTimestamp(value);
	}

	public String marshal(Timestamp value) throws Exception
	{
		return TimestampHelper.toString(value);
	}
}