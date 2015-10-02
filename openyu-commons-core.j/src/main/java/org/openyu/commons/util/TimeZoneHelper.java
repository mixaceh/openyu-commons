package org.openyu.commons.util;

import java.util.TimeZone;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public class TimeZoneHelper extends BaseHelperSupporter
{

	private static transient final Logger log = LogManager.getLogger(TimeZoneHelper.class);

	private static TimeZoneHelper instance;

	public final static String TIME_ZONE = "timeZoneHelper.timeZone";

	private static TimeZone timeZone = null;
	static
	{
		new Static();
	}

	protected static class Static
	{
		public Static()
		{
			try
			{
				timeZone = toTimeZone(ConfigHelper.getString(TIME_ZONE));
				if (timeZone == null)
				{
					timeZone = TimeZone.getDefault();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public TimeZoneHelper()
	{}

	public static synchronized TimeZoneHelper getInstance()
	{
		if (instance == null)
		{
			instance = new TimeZoneHelper();
		}
		return instance;
	}

	public static TimeZone getTimeZone()
	{
		return timeZone;
	}

	public static void setTimeZone(TimeZone timeZone)
	{
		TimeZoneHelper.timeZone = timeZone;
	}

	/**
	 * @param value Asia/Taipei,GMT+8,GMT
	 */
	public static TimeZone toTimeZone(String value)
	{
		TimeZone result = null;
		if (value != null)
		{
			result = TimeZone.getTimeZone(value);
		}
		return result;
	}

}
