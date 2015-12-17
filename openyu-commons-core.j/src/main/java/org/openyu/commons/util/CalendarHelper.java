package org.openyu.commons.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 萬年曆輔助類
 */
public final class CalendarHelper extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CalendarHelper.class);

	public final static int MILLIS_IN_SECOND = 1000;

	public final static int MILLIS_IN_THREE_SECOND = MILLIS_IN_SECOND * 3;

	public final static int MILLIS_IN_FIVE_SECOND = MILLIS_IN_SECOND * 5;

	public final static int MILLIS_IN_TEN_SECOND = MILLIS_IN_SECOND * 10;

	//
	public final static int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

	public final static int MILLIS_IN_THREE_MINUTE = MILLIS_IN_MINUTE * 3;

	public final static int MILLIS_IN_FIVE_MINUTE = MILLIS_IN_MINUTE * 5;

	public final static int MILLIS_IN_TEN_MINUTE = MILLIS_IN_MINUTE * 10;

	public final static int MILLIS_IN_FIFTEEN_MINUTE = MILLIS_IN_MINUTE * 15;

	public final static int MILLIS_IN_THIRTY_MINUTE = MILLIS_IN_MINUTE * 30;

	public final static int MILLIS_IN_FORTY_FIVE_MINUTE = MILLIS_IN_MINUTE * 45;

	//
	public final static int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

	public final static int MILLIS_IN_THREE_HOUR = MILLIS_IN_HOUR * 3;

	public final static int MILLIS_IN_FIVE_HOUR = MILLIS_IN_HOUR * 5;

	//
	public final static int MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

	public final static int MILLIS_IN_THREE_DAY = MILLIS_IN_DAY * 3;

	public final static int MILLIS_IN_FIVE_DAY = MILLIS_IN_DAY * 5;

	public final static int MILLIS_IN_ONE_WEEK = MILLIS_IN_DAY * 7;

	//
	public static final int INTERVAL_SCALE = 10;

	private CalendarHelper() {
		throw new HelperException(
				new StringBuilder().append(CalendarHelper.class.getName()).append(" can not construct").toString());
	}

	public static Calendar createCalendar() {
		return createCalendar((TimeZone) null, (Locale) null);
	}

	public static Calendar createCalendar(TimeZone timeZone) {
		return createCalendar(timeZone, null);
	}

	public static Calendar createCalendar(TimeZone timeZone, Locale locale) {
		Calendar calendar = null;
		TimeZone newTimeZone = (timeZone != null ? timeZone : TimeZone.getDefault());
		Locale newLocale = (locale != null ? locale : LocaleHelper.getLocale());
		calendar = Calendar.getInstance(newTimeZone, newLocale);
		return calendar;
	}

	// -----------------------------------------------------
	public static Calendar toCalendar(Date date) {
		return toCalendar(date, (TimeZone) null, (Locale) null);
	}

	public static Calendar toCalendar(Date date, TimeZone timeZone) {
		return toCalendar(date, timeZone, null);
	}

	public static Calendar toCalendar(Date date, TimeZone timeZone, Locale locale) {
		Calendar calendar = createCalendar(timeZone, locale);
		calendar.setTime(date);
		return calendar;
	}

	public static Calendar toCalendar(String value) {
		return toCalendar(value, null, null, null);
	}

	public static Calendar toCalendar(String value, String pattern) {
		return toCalendar(value, pattern, null, null);
	}

	public static Calendar toCalendar(String value, String pattern, TimeZone timeZone) {
		return toCalendar(value, pattern, timeZone, null);
	}

	public static Calendar toCalendar(String value, String pattern, TimeZone timeZone, Locale locale) {
		Date date = DateHelper.toDate(value, pattern, timeZone, locale);
		return toCalendar(date, timeZone, locale);
	}

	// --------------------------------------------------------
	public static String toString(Calendar value) {
		return toString(value, null, null, null);
	}

	public static String toString(Calendar value, String pattern) {
		return toString(value, pattern, null, null);
	}

	public static String toString(Calendar value, String pattern, TimeZone timeZone) {
		return toString(value, pattern, timeZone, null);
	}

	public static String toString(Calendar value, String pattern, TimeZone timeZone, Locale locale) {
		return DateHelper.toString(value.getTime(), pattern, timeZone, locale);
	}

	// -----------------------------------------------------
	public static long diffDays(Calendar value1, Calendar value2) {
		long result = diff(value1, value2, MILLIS_IN_DAY);
		return result;
	}

	public static long diffHours(Calendar value1, Calendar value2) {
		long result = diff(value1, value2, MILLIS_IN_HOUR);
		return result;
	}

	public static long diffMinutes(Calendar value1, Calendar value2) {
		long result = diff(value1, value2, MILLIS_IN_MINUTE);
		return result;
	}

	public static long diffSeconds(Calendar value1, Calendar value2) {
		long result = diff(value1, value2, MILLIS_IN_SECOND);
		return result;
	}

	public static long diff(Calendar value1, Calendar value2, int millisInCondition) {
		long result = 0;
		long v1 = (value1.getTimeInMillis() + value1.get(Calendar.ZONE_OFFSET)) / millisInCondition;
		long v2 = (value2.getTimeInMillis() + value2.get(Calendar.ZONE_OFFSET)) / millisInCondition;
		result = v2 - v1;
		return result;
	}

	public static Calendar excludeDate(Calendar calendar) {
		excludeYear(calendar);
		excludeMonth(calendar);
		excludeDay(calendar);
		return calendar;
	}

	public static Calendar excludeYear(Calendar calendar) {

		if (calendar != null) {
			// YEAR
			calendar.clear(Calendar.YEAR);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeMonth(Calendar calendar) {
		if (calendar != null) {
			// MONTH
			calendar.clear(Calendar.MONTH);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeDay(Calendar calendar) {
		if (calendar != null) {
			// DAY_OF_MONTH
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeTime(Calendar calendar) {
		excludeHour(calendar);
		excludeMinute(calendar);
		excludeSecond(calendar);
		excludeMills(calendar);
		return calendar;
	}

	public static Calendar excludeHour(Calendar calendar) {
		if (calendar != null) {
			// HOUR
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeMinute(Calendar calendar) {
		if (calendar != null) {
			// MINUTE
			calendar.clear(Calendar.MINUTE);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeSecond(Calendar calendar) {
		if (calendar != null) {
			// SECOND
			calendar.clear(Calendar.SECOND);
			return calendar;
		}
		return null;
	}

	public static Calendar excludeMills(Calendar calendar) {
		if (calendar != null) {
			// MILLISECOND
			calendar.clear(Calendar.MILLISECOND);
			return calendar;
		}
		return null;
	}

	public static Calendar mergeDateTime(Calendar date, Calendar time) {
		Calendar clendar = null;
		if (date != null && time != null) {
			clendar = Calendar.getInstance();
			clendar.set(Calendar.YEAR, date.get(Calendar.YEAR));
			clendar.set(Calendar.MONTH, date.get(Calendar.MONTH));
			clendar.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
			//
			clendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
			clendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
			clendar.set(Calendar.SECOND, time.get(Calendar.SECOND));
			clendar.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));
		}
		return clendar;
	}

	public static Calendar today() {
		return Calendar.getInstance();
	}

	/**
	 * 今天日期
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Calendar today(int hour, int minute, int second) {
		Calendar result = today();
		result.set(Calendar.HOUR_OF_DAY, hour);
		result.set(Calendar.MINUTE, minute);
		result.set(Calendar.SECOND, second);
		return result;
	}

	public static Calendar yesterday() {
		Calendar result = today();
		result.add(Calendar.DATE, -1);
		return result;
	}

	/**
	 * 昨天日期
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Calendar yesterday(int hour, int minute, int second) {
		Calendar result = yesterday();
		result.set(Calendar.HOUR_OF_DAY, hour);
		result.set(Calendar.MINUTE, minute);
		result.set(Calendar.SECOND, second);
		return result;
	}

	public static Calendar tomorrow() {
		Calendar result = today();
		result.add(Calendar.DATE, 1);
		return result;
	}

	/**
	 * 明天日期
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Calendar tomorrow(int hour, int minute, int second) {
		Calendar result = tomorrow();
		result.set(Calendar.HOUR_OF_DAY, hour);
		result.set(Calendar.MINUTE, minute);
		result.set(Calendar.SECOND, second);
		return result;
	}

	public static Date toDate(Calendar value) {
		Date date = null;
		if (value != null) {
			date = value.getTime();
		}
		return date;
	}

	/**
	 * 根据输入的时间,得到输入时间当天的起始时间,即当天时间的0:0:0
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar begTimeOfDate(Calendar value) {
		Calendar calendar = null;
		if (value != null) {
			calendar = (Calendar) value.clone();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}
		return calendar;
	}

	/**
	 * 根据输入的时间,得到输入时间当天的最后时间,即当天时间的23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar endTimeOfDate(Calendar value) {
		Calendar calendar = null;
		if (value != null) {
			calendar = (Calendar) value.clone();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
		}
		return calendar;
	}

	public static Calendar randomCalendar() {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(today().getTimeInMillis() + NumberHelper.randomInt());
		return result;
	}

	public static long randomCalendarLong() {
		return randomCalendar().getTimeInMillis();
	}
}
