package org.openyu.commons.util;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public final class TimeZoneHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(TimeZoneHelper.class);

	public final static String TIME_ZONE = "timeZoneHelper.timeZone";

	private static TimeZone timeZone = null;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				timeZone = toTimeZone(ConfigHelper.getString(TIME_ZONE));
				if (timeZone == null) {
					timeZone = TimeZone.getDefault();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private TimeZoneHelper() {
		throw new HelperException(
				new StringBuilder().append(TimeZoneHelper.class.getName()).append(" can not construct").toString());
	}

	public static TimeZone getTimeZone() {
		return timeZone;
	}

	public static void setTimeZone(TimeZone timeZone) {
		TimeZoneHelper.timeZone = timeZone;
	}

	/**
	 * @param value
	 *            Asia/Taipei,GMT+8,GMT
	 */
	public static TimeZone toTimeZone(String value) {
		TimeZone result = null;
		if (value != null) {
			result = TimeZone.getTimeZone(value);
		}
		return result;
	}

}
