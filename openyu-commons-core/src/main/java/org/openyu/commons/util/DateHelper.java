package org.openyu.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//兩者區別:yyyy-MM-dd HH:mm:ss ;  yyyy-MM-dd hh:mm:ss
//如下:
//字母	日期或時間元素	表示	示例
//G	Era 標誌符	Text	AD
//y	年	Year	1996; 96
//M	年中的月份	Month	July; Jul; 07
//w	年中的周數	Number	27
//W	月份中的周數	Number	2
//D	年中的天數	Number	189
//d	月份中的天數	Number	10
//F	月份中的星期	Number	2
//E	星期中的天數	Text	Tuesday; Tue
//a	Am/pm 標記	Text	PM
//H	一天中的小時數（0-23）	Number	0
//k	一天中的小時數（1-24）	Number	24
//K	am/pm 中的小時數（0-11）	Number	0
//h	am/pm 中的小時數（1-12）	Number	12
//m	小時中的分鐘數	Number	30
//s	分鐘中的秒數	Number	55
//S	毫秒數	Number	978
//z	時區	General time zone	Pacific Standard Time; PST; GMT-08:00
//Z	時區	RFC 822 time zone	-0800

public final class DateHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(DateHelper.class);

	public static final String ORIGINAL_PATTERN = "yyyy/M/d a h:mm";

	// 24時制,精確到秒
	public static final String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

	// 24時制,精確到毫秒
	public static final String DATE_TIME_MILLS_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";

	public static final String DATE_PATTERN = "yyyy/MM/dd";

	public static final String TIME_PATTERN = "HH:mm:ss";

	public static final String TIME_PATTERN_MILLS = "HH:mm:ss.SSS";

	// use by DateFormat.getDateTimeInstance(dateStyle,timeStyle,locale)
	public static final int FULL_PATTERN = DateFormat.FULL;

	public static final int LONG_PATTERN = DateFormat.LONG;

	// DEFAULT=MEDIUM
	public static final int MEDIUM_PATTERN = DateFormat.MEDIUM;

	public static final int SHORT_PATTERN = DateFormat.SHORT;

	// --------------------------------------------------------
	public static final int AD_YEAR_LENGTH = 4;

	public static final int RC_YEAR_LENGTH = 3;

	/**
	 * 當日
	 */
	public static final String PRESENT_DAY_STRING = "today";

	/**
	 * 三天內
	 */
	public static final String TRI_DAYS_STRING = "tri_days";

	/**
	 * 近三天（從當前時間開始，含當天，第二天，第三天）
	 */
	public static final String TRI_DAYS_LATER_STRING = "tri_days_later";

	/**
	 * 近日內
	 */
	public static final String REC_DAYS_STRING = "rec_days";

	/**
	 * 前一周
	 */
	public static final String PRE_WEEK_STRING = "p_week";

	/**
	 * 本周
	 */
	public static final String THIS_WEEK_STRING = "t_week";

	/**
	 * 下一周
	 */
	public static final String NEXT_WEEK_STRING = "n_week";

	/**
	 * 前一月
	 */
	public static final String PRE_MONTH_STRING = "p_month";

	/**
	 * 本月
	 */
	public static final String THIS_MONTH_STRING = "t_month";

	/**
	 * 下一月
	 */
	public static final String NEXT_MONTH_STRING = "n_month";

	/**
	 * 前一季度
	 */
	public static final String PRE_SEASON_STRING = "p_season";

	/**
	 * 本季度
	 */
	public static final String THIS_SEASON_STRING = "t_season";

	/**
	 * 下季度
	 */
	public static final String NEXT_SEASON_STRING = "n_season";

	public static final String START_HALF_YEAR_STRING = "s_half_year";

	public static final String END_HALF_YEAR_STRING = "e_half_year";

	public static final String THIS_YEAR_STRING = "t_year";

	public static final String NEXT_YEAR_STRING = "n_year";

	/**
	 * 一周內,指從當前時間之前的一段時間之內
	 */
	public static final String WITH_A_WEEK = "w_week";

	/**
	 * 一月內,指從當前時間之前的一段時間之內
	 */
	public static final String WITH_A_MONTH = "w_month";

	/**
	 * 三月內,指從當前時間之前的一段時間之內
	 */
	public static final String WITH_A_SEASON = "w_season";

	/**
	 * 一年內,指從當前時間之前的一段時間之內
	 */
	public static final String WITH_A_YEAR = "w_year";

	/**
	 * 最近三日左右
	 */
	public static final String AROUND_THREE_DAYS = "around_three_days";

	/**
	 * 最近一周左右
	 */
	public static final String AROUND_ONE_WEEK = "around_one_week";

	/**
	 * 最近一月左右
	 */
	public static final String AROUND_ONE_MONTH = "around_one_month";

	/**
	 * 最近三月左右
	 */
	public static final String AROUND_THREE_MONTH = "around_three_month";

	/**
	 * 最近一年左右
	 */
	public static final String AROUND_ONE_YEAR = "around_one_year";

	private DateHelper() {
		throw new HelperException(
				new StringBuilder().append(DateHelper.class.getName()).append(" can not construct").toString());
	}

	public static Date today() {
		return CalendarHelper.today().getTime();
	}

	public static Date today(int hour, int minute, int second) {
		Date result = CalendarHelper.today(hour, minute, second).getTime();
		return result;
	}

	public static Date yesterday() {
		Date result = CalendarHelper.yesterday().getTime();
		return result;
	}

	public static Date yesterday(int hour, int minute, int second) {
		Date result = CalendarHelper.yesterday(hour, minute, second).getTime();
		return result;
	}

	public static Date tomorrow() {
		Date result = CalendarHelper.tomorrow().getTime();
		return result;
	}

	public static Date tomorrow(int hour, int minute, int second) {
		Date result = CalendarHelper.tomorrow(hour, minute, second).getTime();
		return result;
	}

	public static Calendar toCalendar(Date value) {
		Calendar result = null;
		if (value != null) {
			result = CalendarHelper.today();
			result.setTime(value);
		}
		return result;
	}

	public static Date toDate(String value) {
		return toDate(value, null, null, null);
	}

	public static Date toDate(String value, String pattern) {
		return toDate(value, pattern, null, null);
	}

	public static Date toDate(String value, String pattern, TimeZone timeZone) {
		return toDate(value, pattern, timeZone, null);
	}

	/**
	 * 預設 DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss", 只精確到秒
	 * 
	 * 若要精確到毫秒,用 DATE_TIME_MILLS_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS"
	 * 
	 * @param value
	 * @param pattern
	 * @param timeZone
	 * @param locale
	 * @return
	 */
	public static Date toDate(String value, String pattern, TimeZone timeZone, Locale locale) {
		Date result = null;
		try {
			if (value != null) {
				SimpleDateFormat sdf = createSimpleDateFormat(pattern, timeZone, locale);
				result = sdf.parse(value);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static String toString(Date value) {
		return toString(value, null, null, null);
	}

	public static String toString(Date value, String pattern) {
		return toString(value, pattern, null, null);
	}

	public static String toString(Date value, String pattern, TimeZone timeZone) {
		return toString(value, pattern, timeZone, null);
	}

	/**
	 * 預設 DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss", 只精確到秒
	 * 
	 * 若要精確到毫秒,用 DATE_TIME_MILLS_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS"
	 * 
	 * @param value
	 * @param pattern
	 * @param timeZone
	 * @param locale
	 * @return
	 */
	public static String toString(Date value, String pattern, TimeZone timeZone, Locale locale) {
		String result = null;
		try {
			if (value != null) {
				SimpleDateFormat sdf = createSimpleDateFormat(pattern, timeZone, locale);
				result = sdf.format(value);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static String toString(long value) {
		return toString(value, null, null, null);
	}

	public static String toString(long value, String pattern, TimeZone timeZone, Locale locale) {
		String result = null;
		try {
			SimpleDateFormat sdf = createSimpleDateFormat(pattern, timeZone, locale);
			result = sdf.format(value);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------
	public static DateFormat createDateFormat() {
		return createDateFormat(MEDIUM_PATTERN, MEDIUM_PATTERN, null);
	}

	public static DateFormat createDateFormat(int dateStyle) {
		return createDateFormat(dateStyle, MEDIUM_PATTERN, null);
	}

	public static DateFormat createDateFormat(int dateStyle, int timeStyle) {
		return createDateFormat(dateStyle, timeStyle, null);
	}

	/**
	 * 
	 * @param dateStyle
	 * @param timeStyle
	 * @param locale
	 * @return
	 */
	public static DateFormat createDateFormat(int dateStyle, int timeStyle, Locale locale) {
		DateFormat result = null;
		//
		try {
			Locale newLocale = (locale != null ? locale : LocaleHelper.getLocale());
			result = DateFormat.getDateTimeInstance(dateStyle, timeStyle, newLocale);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// --------------------------------------------------------

	public static SimpleDateFormat createSimpleDateFormat() {
		return createSimpleDateFormat(null, null, null);
	}

	public static SimpleDateFormat createSimpleDateFormat(String pattern) {
		return createSimpleDateFormat(pattern, null, null);
	}

	public static SimpleDateFormat createSimpleDateFormat(String pattern, TimeZone timeZone) {
		return createSimpleDateFormat(pattern, timeZone, null);
	}

	public static SimpleDateFormat createSimpleDateFormat(String pattern, TimeZone timeZone, Locale locale) {
		SimpleDateFormat result = null;
		//
		try {
			String newPattern = (pattern != null ? pattern : DEFAULT_PATTERN);
			TimeZone newTimeZone = (timeZone != null ? timeZone : TimeZone.getDefault());
			Locale newLocale = (locale != null ? locale : LocaleHelper.getLocale());
			result = new SimpleDateFormat(newPattern, newLocale);
			result.setTimeZone(newTimeZone);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	//
	public static long timeInMillis(Date value) {
		return timeInMillis(value, null, null);
	}

	public static long timeInMillis(Date value, TimeZone timeZone) {
		return timeInMillis(value, timeZone, null);
	}

	public static long timeInMillis(Date value, TimeZone timeZone, Locale locale) {
		Calendar calendar = CalendarHelper.createCalendar(timeZone, locale);
		calendar.setTime(value);
		return calendar.getTimeInMillis();
	}

	public static Date excludeYear(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeYear(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeMonth(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeMonth(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeDate(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeDate(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeDay(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeDay(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	//
	public static Date excludeTime(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeTime(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeHour(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeHour(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeMinute(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeMinute(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeSecond(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeSecond(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	public static Date excludeMills(Date value) {
		Date date = null;
		if (value != null) {
			Calendar calendar = CalendarHelper.toCalendar(value);
			calendar = CalendarHelper.excludeMills(calendar);
			date = calendar.getTime();
		}
		return date;
	}

	//
	public static Date joinDateTime(Date date, Date time) {
		if (date == null) {
			return null;
		}
		if (time == null) {
			return null;
		}
		Calendar dateCalendar = CalendarHelper.toCalendar(date);
		Calendar timeCalendar = CalendarHelper.toCalendar(time);

		Calendar calendar = CalendarHelper.mergeDateTime(dateCalendar, timeCalendar);
		return calendar.getTime();
	}

	// -----------------------------------------------------

	/**
	 * 依據日期得到年度
	 * 
	 * @param aDate
	 * @return
	 */
	public static Integer getYear(Date aDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		return new Integer(cal.get(Calendar.YEAR));
	}

	/**
	 * 得到下一年度
	 * 
	 * @param aDate
	 * @return
	 */
	public static Date getNextYear(Date aDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 依據日期得到月份
	 * 
	 * @param aDate
	 * @return
	 */
	public static Integer getMonth(Date aDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		return new Integer(cal.get(Calendar.MONTH) + 1);
	}

	public static Integer getDate(Date aDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		return new Integer(cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 由民國年得到公元年，比如96->2006
	 * 
	 * @param year
	 * @return
	 */
	public static int getYear(int rcYear) {
		return rcYear + 1911;
	}

	/**
	 * 由民國年得到公元年
	 * 
	 * @param year
	 * @return
	 */
	public static Integer getYear(Integer rcYear) {
		return new Integer(getYear(rcYear.intValue()));
	}

	// /**
	// * 由民國年轉換為公元年字符串，比如96->"2006"
	// *
	// * @param rcYear
	// * @return
	// */
	// public static String getYearString(Integer rcYear)
	// {
	// return NumberHelper.toString(getYear(rcYear), AD_YEAR_LENGTH);
	// }

	// /**
	// * 由民國年字串轉換為公元年字符串，比如"096"->"2006"
	// *
	// * @param rcYear
	// * @return
	// */
	// public static String getYearString(String rcYear)
	// {
	// return getYearString(new Integer(rcYear));
	// }

	/**
	 * 由公元年得到民國年，比如2006-> 96
	 * 
	 * @param year
	 * @return
	 */
	public static int getRCYear(int year) {
		return year - 1911;
	}

	/**
	 * 由公元年得到民國年
	 * 
	 * @param year
	 * @return
	 */
	public static Integer getRCYear(Integer year) {
		return new Integer(getRCYear(year.intValue()));
	}

	/**
	 * 依日期型得到民國年
	 * 
	 * @param aDate
	 * @return
	 */
	public static Integer getRCYear(Date aDate) {
		return getRCYear(getYear(aDate));
	}

	// /**
	// * 得到公元年度的民國年度的字串表示形式,比如:2006->"095"
	// *
	// * @param year
	// * @return
	// */
	// public static String getRCYearString(Integer year)
	// {
	// return NumberHelper.toString(getRCYear(year), RC_YEAR_LENGTH);
	// }

	// public static String getRCYearString(String year)
	// {
	// return getRCYearString(new Integer(year));
	// }
	//
	// public static String getRCYearString(Date date)
	// {
	// return getRCYearString(getYear(date));
	// }

	public static String getRCDateString(Date date) {
		return TaiwanDateHelper.getDateTime("yyy/MM/dd", date);
	}

	// /**
	// * 得到月份（兩位），如：12、01。
	// *
	// * @return
	// */
	// public static String getMonthString(Date date)
	// {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(date);
	// return NumberHelper.toString((cal.get(Calendar.MONTH) + 1), 2);
	// }

	// /**
	// * 得到月份中的日期（兩位），如：23、01。
	// *
	// * @return
	// */
	// public static String getDayOfMonthString(Date date)
	// {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(date);
	// return NumberHelper.toString(cal.get(Calendar.DAY_OF_MONTH), 2);
	// }

	// /**
	// * 得到月份與月份中的日期（四位）MM/dd，如：0123,1201。
	// *
	// * @return
	// */
	// public static String getMonthDayString(Date date)
	// {
	// if (date == null)
	// return null;
	// return getMonthString(date) + getDayOfMonthString(date);
	// }

	/**
	 * 處理帶時分的日期字符串，如"yyy/MM/dd HH:mm"
	 * 
	 * @return Date
	 */
	public static Date getDateFromRCTimeString(String dateTime) {
		if (StringHelper.isEmpty(dateTime))
			return null;
		try {
			return TaiwanDateHelper.convertStringToDateTime(dateTime);
		} catch (ParseException pe) {
			return null;
		}
	}

	/**
	 * 處理不帶時分的日期字符串，如"yyy/MM/dd"
	 * 
	 * @return Date
	 */
	public static Date getDateFromRCDayString(String dateTime) {
		if (StringHelper.isEmpty(dateTime))
			return null;
		try {
			return TaiwanDateHelper.convertStringToDate("yyy/MM/dd", dateTime);
		} catch (ParseException pe) {
			return null;
		}
	}

	/**
	 * 得到指定年度內，指定月份到年底的時間範圍<br>
	 * 比如 year=2007, month=10。返回結果從 2007/10/1 到 2007/12/31
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月份
	 * @return 返回Date[2]，其中Date[0]表示開始時間，Date[1]表示結束時間
	 */
	public static Date[] getDateScopeAfterMonth(int year, int month) {
		Date[] date = new Date[2];

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		date[0] = cal.getTime();

		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		date[1] = cal.getTime();

		return date;
	}

	/**
	 * 處理Date對像為帶時分的日期字符串，如"yyy/MM/dd HH:mm"
	 * 
	 * @return String
	 */
	public static String getStringFromRCDate(Date dateTime) {
		if (dateTime == null)
			return "";
		return TaiwanDateHelper.getDateTime(dateTime);
	}

	/**
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */

	/**
	 * 處理帶時分的日期字符串，如"yyy/MM/dd HH:mm"或公元年
	 * 
	 * @return Date
	 */
	public static Date getDateByRelativeDayNumber(Date dateTime, int relativeDayNumber) {
		if (relativeDayNumber == 0)
			return dateTime;
		Long oldDate = dateTime.getTime();
		Long newDate = oldDate + relativeDayNumber * 24 * 3600 * 1000;
		return new Date(newDate);
	}

	/**
	 * 比較第1個Date與第2個Date的相差天數(取整,去掉餘數)
	 * 
	 * @return Long
	 */
	public static int getAfterDayNumber(Date endDate, Date startDate) {
		Calendar cal = new GregorianCalendar();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		Long date1 = cal.getTimeInMillis();

		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		Long date2 = cal.getTimeInMillis();

		return (int) Math.floor((date1 - date2) / (24 * 3600 * 1000));
	}

	public static boolean before(Date sDate, Date eDate) {
		Calendar sCal = new GregorianCalendar();
		sCal.setTime(sDate);
		Calendar eCal = new GregorianCalendar();
		eCal.setTime(eDate);
		return sCal.before(eCal);
	}

	/**
	 * 查看兩個日期是否在同一年度
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public static boolean inTheSameYear(Date date1, Date date2) {
		Calendar sCal1 = new GregorianCalendar();
		sCal1.setTime(date1);
		Calendar sCal2 = new GregorianCalendar();
		sCal2.setTime(date2);

		return (sCal1.get(Calendar.YEAR) == sCal2.get(Calendar.YEAR));
	}

	/**
	 * 查看兩個日期是否在同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public static boolean inTheSameDay(Date date1, Date date2) {
		Calendar sCal1 = new GregorianCalendar();
		sCal1.setTime(date1);
		Calendar sCal2 = new GregorianCalendar();
		sCal2.setTime(date2);

		return (sCal1.get(Calendar.YEAR) == sCal2.get(Calendar.YEAR))
				&& (sCal1.get(Calendar.MONTH) == sCal2.get(Calendar.MONTH))
				&& (sCal1.get(Calendar.DAY_OF_MONTH) == sCal2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 根據輸入的時間,得到輸入時間當天的起始時間,即當天時間的0:0:0
	 * 
	 * @param date
	 * @return
	 */
	public static Date begTimeOfDate(Date value) {
		return CalendarHelper.toDate(CalendarHelper.begTimeOfDate(toCalendar(value)));
	}

	/**
	 * 根據輸入的時間,得到輸入時間當天的最後時間,即當天時間的23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date endTimeOfDate(Date value) {
		return CalendarHelper.toDate(CalendarHelper.endTimeOfDate(toCalendar(value)));
	}

	/**
	 * 得到指定開始日期的開始時間(20070101 00:00:00)與結束日期的結束時間(20070102 23:59:59)
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Date[] getBetweenDate(Date start, Date end) {
		Date[] dates = new Date[2];
		dates[0] = begTimeOfDate(start);
		dates[1] = endTimeOfDate(end);
		return dates;
	}

	/**
	 * 得到beforeDays之前的時間
	 * 
	 * @param date
	 * @param beforeDays
	 * @return
	 */
	public static Date getBeforeDate(Date date, int beforeDays) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - beforeDays);
		return cal.getTime();
	}

	/**
	 * 
	 * 不晚於（早於或等於）
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static boolean isNotAfter(Date sDate, Date eDate) {
		return (sDate.compareTo(eDate) == 0 || before(sDate, eDate));
	}

	/**
	 * 得到本周第一天(週一)(以星期一作為每週的開始,星期日作為每週的結束)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return begTimeOfDate(cal.getTime());
	}

	/**
	 * 得到本周最後一天(週日)(以星期一作為每週的開始,星期日作為每週的結束)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return endTimeOfDate(cal.getTime());
	}

	/**
	 * 得到本月第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);
		return begTimeOfDate(cal.getTime());
	}

	/**
	 * 得到本月最後一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.DATE, -1);
		return endTimeOfDate(cal.getTime());
	}

	/**
	 * 得到本季第一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getFirstDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = (quarter - 1) * 3 + 1;
		}
		return getFirstDayOfMonth(year, month);
	}

	/**
	 * 得到本季最後一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getLastDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = quarter * 3;
		}
		return getLastDayOfMonth(year, month);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getQuarter(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return (cal.get(Calendar.MONTH) - 1) / 3 + 1;
	}

	/**
	 * 得到本年第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getFirstDayOfYear(int year) {
		return getFirstDayOfMonth(year, 1);
	}

	/**
	 * 得到本年最後一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getLastDayOfYear(int year) {
		return getLastDayOfMonth(year, 12);
	}

	/**
	 * 得到指定日期後的近幾日的時間
	 * 
	 * @param date
	 *            指定的日期
	 * @param dateRange
	 *            指定的日期範圍，即：近dateRange日
	 * @return
	 */
	public static Date getRecDay(Date date, int dateRange) {
		return getRecDayCal(date, dateRange).getTime();
	}

	/**
	 * 得到指定日期後的近幾日的時間
	 * 
	 * @param date
	 *            指定的日期
	 * @param dateRange
	 *            指定的日期範圍，即：近dateRange日
	 * @return Calendar
	 */
	public static Calendar getRecDayCal(Date date, int dateRange) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + dateRange);
		return cal;
	}

	/**
	 * 得到指定日期後的近幾小時
	 * 
	 * @param date
	 *            指定日期
	 * @param addHours
	 *            指定的後幾小時
	 * @return
	 */
	public static Date getRecHour(Date date, int addHours) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + addHours);
		return cal.getTime();
	}

	/**
	 * 在getDatesByPattern方法基礎上加入了獲取當天時間範圍、3天前的時間範圍
	 * 
	 * @param pattern
	 * @return
	 */
	public static Object[] getDatesByPatternExt(String pattern) {
		Object[] dateArray = new Date[2];
		Calendar cal = Calendar.getInstance();
		if (pattern.equals(PRESENT_DAY_STRING)) {
			dateArray[0] = begTimeOfDate(cal.getTime());
			dateArray[1] = endTimeOfDate(cal.getTime());
			return dateArray;
		}
		if (pattern.equalsIgnoreCase(TRI_DAYS_STRING)) {
			dateArray[0] = begTimeOfDate(getBeforeDate(cal.getTime(), 3));
			dateArray[1] = endTimeOfDate(cal.getTime());
			return dateArray;
		}

		if (AROUND_THREE_DAYS.equals(pattern)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			dateArray[0] = begTimeOfDate(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 2);
			dateArray[1] = endTimeOfDate(cal.getTime());

			return dateArray;
		}
		if (AROUND_ONE_WEEK.equals(pattern)) {
			cal.add(Calendar.DAY_OF_MONTH, -3);
			dateArray[0] = begTimeOfDate(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 6);
			dateArray[1] = endTimeOfDate(cal.getTime());

			return dateArray;
		}
		if (AROUND_ONE_MONTH.equals(pattern)) {
			cal.add(Calendar.DAY_OF_MONTH, -15);
			dateArray[0] = begTimeOfDate(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 30);
			dateArray[1] = endTimeOfDate(cal.getTime());

			return dateArray;
		}
		if (AROUND_THREE_MONTH.equals(pattern)) {
			cal.add(Calendar.DAY_OF_MONTH, -45);
			dateArray[0] = begTimeOfDate(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 90);
			dateArray[1] = endTimeOfDate(cal.getTime());

			return dateArray;
		}
		if (AROUND_ONE_YEAR.equals(pattern)) {
			cal.add(Calendar.DAY_OF_MONTH, -182);
			dateArray[0] = begTimeOfDate(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 364);
			dateArray[1] = endTimeOfDate(cal.getTime());

			return dateArray;
		}
		if (TRI_DAYS_LATER_STRING.equals(pattern)) {
			dateArray[0] = begTimeOfDate(cal.getTime());
			dateArray[1] = endTimeOfDate(getRecDay(cal.getTime(), 3));
			return dateArray;
		}

		return getDatesByPattern(pattern);
	}

	/**
	 * 得到近dateRange內的起始日及結束日，如：當前日期為2007-5-14,近10日的開始日期為:2007-5-14,結束日期為：2007-5-
	 * 24
	 * 
	 * @param pattern
	 * @param dateRange
	 * @return
	 */
	public static Object[] getDatesByPattern(String pattern, String dateRange) {
		Object[] dateArray = new Date[2];
		Calendar cal = Calendar.getInstance();
		if (pattern.equalsIgnoreCase(REC_DAYS_STRING)) {
			dateArray[0] = cal.getTime();
			dateArray[1] = getRecDay(cal.getTime(), new Integer(dateRange).intValue());
		}
		return dateArray;
	}

	/**
	 * 
	 * 得到指定方式的起始日
	 * 
	 * @param pattern
	 * @return
	 */
	public static Object[] getDatesByPattern(String pattern) {
		Object[] dateArray = new Date[2];
		Calendar cal = Calendar.getInstance();
		cal.setTime(begTimeOfDate(cal.getTime()));
		if (pattern.equals(PRE_WEEK_STRING)) {
			Calendar nweek = new GregorianCalendar();
			nweek.setTime(cal.getTime());
			nweek.add(Calendar.WEEK_OF_MONTH, -1);
			dateArray[0] = getFirstDayOfWeek(nweek.getTime());
			dateArray[1] = getLastDayOfWeek(nweek.getTime());
		} else if (pattern.equals(THIS_WEEK_STRING)) {
			dateArray[0] = getFirstDayOfWeek(cal.getTime());
			dateArray[1] = getLastDayOfWeek(cal.getTime());
		} else if (pattern.equals(NEXT_WEEK_STRING)) {
			Calendar nweek = new GregorianCalendar();
			nweek.setTime(cal.getTime());
			nweek.add(Calendar.WEEK_OF_MONTH, 1);
			dateArray[0] = getFirstDayOfWeek(nweek.getTime());
			dateArray[1] = getLastDayOfWeek(nweek.getTime());
		} else if (pattern.endsWith(PRE_MONTH_STRING)) {
			dateArray[0] = getFirstDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
			dateArray[1] = getLastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		} else if (pattern.equals(THIS_MONTH_STRING)) {
			dateArray[0] = getFirstDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
			dateArray[1] = getLastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
		} else if (pattern.equals(NEXT_MONTH_STRING)) {
			dateArray[0] = getFirstDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 2);
			dateArray[1] = getLastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 2);
		}

		else if (pattern.equals(PRE_SEASON_STRING)) {
			int nquarter = getQuarter(cal.getTime()) - 1;
			if (nquarter < 1)
				nquarter = nquarter + 1;
			dateArray[0] = getFirstDayOfQuarter(cal.get(Calendar.YEAR), nquarter);
			dateArray[1] = getLastDayOfQuarter(cal.get(Calendar.YEAR), nquarter);
		} else if (pattern.equals(THIS_SEASON_STRING)) {
			dateArray[0] = getFirstDayOfQuarter(cal.get(Calendar.YEAR), getQuarter(cal.getTime()));
			dateArray[1] = getLastDayOfQuarter(cal.get(Calendar.YEAR), getQuarter(cal.getTime()));
		} else if (pattern.equals(NEXT_SEASON_STRING)) {
			int nquarter = getQuarter(cal.getTime()) + 1;
			if (nquarter > 4)
				nquarter = nquarter - 4;
			dateArray[0] = getFirstDayOfQuarter(cal.get(Calendar.YEAR), nquarter);
			dateArray[1] = getLastDayOfQuarter(cal.get(Calendar.YEAR), nquarter);
		} else if (pattern.equals(START_HALF_YEAR_STRING)) {
			dateArray[0] = getFirstDayOfMonth(cal.get(Calendar.YEAR), 1);
			dateArray[1] = getLastDayOfMonth(cal.get(Calendar.YEAR), 6);
		} else if (pattern.equals(END_HALF_YEAR_STRING)) {
			dateArray[0] = getFirstDayOfMonth(cal.get(Calendar.YEAR), 7);
			dateArray[1] = getLastDayOfMonth(cal.get(Calendar.YEAR), 12);
		} else if (pattern.equals(THIS_YEAR_STRING)) {
			dateArray[0] = getFirstDayOfYear(cal.get(Calendar.YEAR));
			dateArray[1] = getLastDayOfYear(cal.get(Calendar.YEAR));
		} else if (pattern.equals(NEXT_YEAR_STRING)) {
			dateArray[0] = getFirstDayOfYear(cal.get(Calendar.YEAR) + 1);
			dateArray[1] = getLastDayOfYear(cal.get(Calendar.YEAR) + 1);
		} else if (pattern.equals(WITH_A_WEEK)) {
			dateArray[1] = endTimeOfDate(cal.getTime());
			// cal.roll(Calendar.WEEK_OF_YEAR, false);
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			dateArray[0] = cal.getTime();
		} else if (pattern.equals(WITH_A_MONTH)) {
			dateArray[1] = endTimeOfDate(cal.getTime());
			// cal.roll(Calendar.MONTH, false);
			cal.add(Calendar.MONTH, -1);
			dateArray[0] = cal.getTime();
		} else if (pattern.equals(WITH_A_SEASON)) {
			dateArray[1] = endTimeOfDate(cal.getTime());
			// cal.roll(Calendar.MONTH, -3);
			cal.add(Calendar.MONTH, -3);
			dateArray[0] = cal.getTime();
		} else if (pattern.equals(WITH_A_YEAR)) {
			dateArray[1] = endTimeOfDate(cal.getTime());
			// cal.roll(Calendar.YEAR, false);
			cal.add(Calendar.YEAR, -1);
			dateArray[0] = cal.getTime();
		}
		return dateArray;
	}

	/**
	 * 根據給定的Date對像返回其對應的年月的整數
	 * 
	 * @param date
	 *            給定的時間對像
	 * @return 返回轉換後的整型對像
	 * @see com.linkeysoft.hyoa.common.DateTool#getYearMonthInt(String, String)
	 */
	public static Integer getYearMonthInt(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return getYearMonthInt(String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(cal.get(Calendar.MONTH) + 1));
	}

	/**
	 * 根據給定的年月字符串得到整數<br>
	 * 轉換規則：<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;整數 = 年 * 12 + (月 - 1)。<br>
	 * 例如:2000年2月 --> 24001
	 * 
	 * @param year
	 *            表示年份的字符串，比如：「2007」
	 * @param month
	 *            表示月份的字符串，範圍從 「1」 到 「12」 (或「01」到「12」)
	 * @return 返回轉換後的整型對像
	 * @see com.linkeysoft.hyoa.common.DateTool#getYearMonthArray(Integer)
	 */
	public static Integer getYearMonthInt(String year, String month) {
		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		return yearInt * 12 + (monthInt - 1);
	}

	/**
	 * 根據給定的整數得到年月<br>
	 * 轉換規則：<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;年 = 整數 / 12<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;月 = 整數 % 12 + 1<br>
	 * 例如：24001-->2000年2月
	 * 
	 * @param yearMonth
	 *            根據年月合成的整數
	 * @return 返回表示年月的字符串數組，<br>
	 *         數組第一個元素為「年」，第二個字符串為「月」，月份範圍從 「1」 到 「12」
	 * @see com.linkeysoft.hyoa.common.DateTool#getYearMonthInt(String, String)
	 */
	public static String[] getYearMonthArray(Integer yearMonth) {
		String[] rv = new String[2];
		rv[0] = String.valueOf(yearMonth / 12);
		rv[1] = String.format("%02d", yearMonth % 12 + 1);
		return rv;
	}

	/**
	 * 根據給定的整數得到月,參見getYearMonthArray(Integer yearMonth)
	 */
	public static int getMonthByIntegerYearMonth(Integer yearMonth) {
		return yearMonth % 12 + 1;
	}

	/**
	 * 得到指定年度的時間範圍
	 * 
	 * @param theYear
	 * @return 返回Date[]：<br>
	 *         &nbsp;&nbsp;&nbsp;&nbsp;Date[0] 表示該年度開始的日期<br>
	 *         &nbsp;&nbsp;&nbsp;&nbsp;Date[1] 表示該年度結束的日期<br>
	 */
	public static Date[] getDateScopeOfYear(int theYear) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, theYear);
		Date[] ret = new Date[2];

		cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		ret[0] = new Date(cal.getTimeInMillis());

		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		ret[1] = new Date(cal.getTimeInMillis());

		return ret;
	}

	/**
	 * 根據給定的日期和時間的字串,返回給定日期的指定時間.
	 * 
	 * @param date
	 *            給定的日期
	 * @param timeStr
	 *            時間字串,格式為:HH:mm 如:getTimeFromDateAndTimeStr(new
	 *            Date(),"08:30"),則返回的Date為當前日期的8:30分
	 */
	public static Date getTimeFromDateAndTimeStr(Date date, String timeStr) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		setHourAndMinute(calendar, timeStr);
		return calendar.getTime();
	}

	/**
	 * 根據字符串，設置cal的小時與分鐘
	 * 
	 * @param cal
	 * @param hourAndMinute
	 *            如："18:30"
	 */
	public static void setHourAndMinute(Calendar cal, String timeStr) {
		if (StringHelper.isEmpty(timeStr))
			return;
		String[] strs = timeStr.split(":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(strs[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(strs[1]));
	}

	/**
	 * 從給定的時間段內返回是星期*的日期
	 * 
	 * @param startDate
	 *            開始時間(含)
	 * @param endDate
	 *            結束時間(含)
	 * @param weekDays
	 *            星期*的數組，由1--7分別代表週日，週一....週六 其順序為：1,2,3,4,5,6,7
	 * @return
	 */
	public static Date[] getDateOfWeekByTime(Date startDate, Date endDate, String[] weekDays) {
		List<Date> dates = new ArrayList<Date>();
		Date[] rv = new Date[0];

		/* 順序找出第一組滿足條件的日期 */
		getFirstTeamWeekDays(startDate, endDate, weekDays, dates);

		int plusDates = getAfterDayNumber(begTimeOfDate(endDate), begTimeOfDate(startDate)) + 1;
		int weeks = (int) Math.ceil(plusDates / 7.0) - 2;

		/* 加上中間的幾個星期 */
		if (weeks > 0) {
			Date[] firstDates = new Date[dates.size()];
			dates.toArray(firstDates);
			for (int j = 1; j <= weeks; j++) {
				for (int i = 0; i < firstDates.length; i++) {
					dates.add(getRecDay(firstDates[i], j * 7));
				}
			}
		}

		/* 處理最後一個星期 */
		if (dates.size() > 0) {
			startDate = getRecDay(dates.get(dates.size() - 1), 1);
			getFirstTeamWeekDays(startDate, endDate, weekDays, dates);
		}

		return dates.toArray(rv);
	}

	/**
	 * 返回給定時間內符合給定條件（星期幾）的天數
	 * 
	 * @param startDate
	 *            開始時間(包含)
	 * @param endDate
	 *            結束時間(包含)
	 * @param weekDays
	 *            星期*的數組，由1--7分別代表週日，週一....週六 其順序為：1,2,3,4,5,6,7
	 * @return
	 */
	public static int getDateSumOfWeekByTime(Date startDate, Date endDate, String[] weekDays) {
		int datesTotal = 0;

		/* 順序找出第一組滿足條件的日期 */
		List<Date> dates = new ArrayList<Date>();
		getFirstTeamWeekDays(startDate, endDate, weekDays, dates);

		int plusDates = getAfterDayNumber(begTimeOfDate(endDate), begTimeOfDate(startDate)) + 1;
		int weeks = (int) Math.ceil(plusDates / 7.0) - 2;

		/* 加上中間的幾個星期 */
		if (weeks > 0) {
			datesTotal = datesTotal + weeks * dates.size();
		}

		/* 處理最後一個星期 */
		if (dates.size() > 0) {
			startDate = getRecDay(dates.get(dates.size() - 1), weeks * 7 + 1);
			getFirstTeamWeekDays(startDate, endDate, weekDays, dates);
		}

		/* 加上第一個和最後一個星期的符合條件的天數 */
		datesTotal += dates.size();
		return datesTotal;
	}

	/**
	 * 在給定的時間段內查找符合"星期*"的第一組數據
	 * 
	 * @param startDate
	 *            開始時間
	 * @param endDate
	 *            結束時間
	 * @param weekDays
	 *            星期*的數組，由1--7分別代表週日，週一....週六 其順序為：1,2,3,4,5,6,7
	 * @param dates
	 *            將符合條件的日期放入此集合內
	 * 
	 *            eg1:startDate=2007/12/01 endDate=2007/12/31
	 *            若:weekDays={"1","3","7"},
	 *            則dates將放入如下數據:2007/12/01,2007/12/02,2007/12/04
	 *            若:weekDays={"3","5","7"},
	 *            則dates將放入如下數據:2007/12/04,2007/12/06,2007/12/08
	 *            eg2:startDate=2007/12/04 endDate=2007/12/09
	 *            若:weekDays={"1","2"}, 則dates將放入如下數據:2007/12/09
	 */
	private static void getFirstTeamWeekDays(Date startDate, Date endDate, String[] weekDays, List<Date> dates) {
		Calendar calStart = new GregorianCalendar();
		calStart.setTime(startDate);
		Calendar calEnd = new GregorianCalendar();
		calEnd.setTime(endDate);
		int startDayNum = calStart.get(Calendar.DAY_OF_WEEK);
		int afterStart = 0;
		for (int i = 0; i < weekDays.length; i++) {
			// 如果給定的星期*小於開始時間，則先找後面的星期
			if (new Integer(weekDays[i]).intValue() < startDayNum)
				continue;
			afterStart = i;
			Calendar cal = new GregorianCalendar();
			cal = getRecDayCal(startDate, new Integer(weekDays[i]).intValue() - startDayNum);
			if (cal.getTimeInMillis() > calEnd.getTimeInMillis()) {
				return;
			}
			dates.add(cal.getTime());
		}

		// 再找小於開始時間的星期*
		afterStart = (afterStart == 0 ? weekDays.length : afterStart);
		for (int i = 0; i < afterStart; i++) {
			if (new Integer(weekDays[i]).intValue() >= startDayNum)
				continue;
			Calendar cal = new GregorianCalendar();
			cal = getRecDayCal(startDate, 7 - (startDayNum - new Integer(weekDays[i]).intValue()));
			if (cal.getTimeInMillis() > calEnd.getTimeInMillis()) {
				return;
			}
			dates.add(cal.getTime());
		}
	}

	/**
	 * 根據給定的日期返回其星期數 週一：1;週二：2......週日：7
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int rv = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (rv == 0) // 週日
			rv = 7;
		return rv;

	}

	/**
	 * 在給定的年月裡返回指定的第weekNum(從1開始)周的第一天和最後一天(只考慮本月).
	 * 第一周的第一天為本月第一天,最後一周的最後一天為本月的最後一天
	 */
	public static Date[] getFirstAndLastDateOfWeek(String year, String month, String weekNum) {
		int maxWeek = getMaxWeekOfMonth(year, month, Calendar.MONDAY);

		// 如果指定周超過最大周數或小於1則返回null
		if (Integer.valueOf(weekNum) > maxWeek || Integer.valueOf(weekNum) <= 0)
			return null;

		// 取得當月第一天及最後一天
		Date firstDayOfFirstWeek = getFirstDayOfMonth(Integer.valueOf(year), Integer.valueOf(month));
		Date lastDayOfMonth = getLastDayOfMonth(Integer.valueOf(year), Integer.valueOf(month));

		// 取得本月第一個星期的最後一天
		Date lastDayOfFirstWeek = getLastDayOfWeek(firstDayOfFirstWeek);

		/*
		 * 得出指定周的第一天及最後一天 表達式:第一天 =本月開始日期向後延長[(7-本月第一天的星期)+1+(7*(指定周數-2))]天
		 * 最後一天=第一周的最後一天往後延長[7*(指定周數-1)]
		 */
		Date firstDayOfNowWeek = getRecDay(firstDayOfFirstWeek,
				(7 - getDayOfWeek(firstDayOfFirstWeek) + 1) + (7 * (Integer.valueOf(weekNum) - 2)));
		Date lastDayOfNowWeek = getRecDay(lastDayOfFirstWeek, 7 * (Integer.valueOf(weekNum) - 1));

		/*
		 * 如果指定周的最後一天大於本月最後一天,則本月最後一天為該周在本月的最後一天
		 * 如果指定周的第一天小於本月第一天,則本月第一天為該周在本月的第一天
		 */
		if (lastDayOfNowWeek.after(lastDayOfMonth))
			lastDayOfNowWeek = lastDayOfMonth;
		if (firstDayOfNowWeek.before(firstDayOfFirstWeek))
			firstDayOfNowWeek = firstDayOfFirstWeek;

		return new Date[] { firstDayOfNowWeek, lastDayOfNowWeek };
	}

	/**
	 * 查看某日處於其所在月的第幾周
	 */
	public static String getWeekOfMonthByDate(Date date) {
		int dateWeekDay = getDayOfWeek(date);
		Date firstDayOfFirstWeek = getFirstDayOfWeek(
				getFirstDayOfMonth(getYear(date).intValue(), getMonth(date).intValue()));
		/*
		 * 取得日期所在的周 表達式:[(指定日期與本月第一周星期一相差的時間+1)+(7-給定日期的星期數)]/7
		 */
		int weekNum = (getAfterDayNumber(endTimeOfDate(date), endTimeOfDate(firstDayOfFirstWeek)) + 1
				+ (7 - dateWeekDay)) / 7;
		return String.valueOf(weekNum);
	}

	/**
	 * 獲得一個指定年月的每個周的開始日期和結束日期
	 * 
	 * @param year
	 * @param month
	 * @return 以第幾為key,周開始和結束日期為一個Date[]的Map集合
	 */
	public static Map<Integer, Date[]> getDateFromMonthByWeek(String year, String month) {
		// 生成Calendar並設置當前日期時間
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(getFirstDayOfMonth(Integer.valueOf(year), Integer.valueOf(month)));
		// 取得當前月份的總周數
		int maxWeek = getMaxWeekOfMonth(year, month, Calendar.MONDAY);
		// 設置變量周從第一周開始
		int weekNow = 1;

		// 用於存放每週和其對應啟止日期(數組)的Map
		Map<Integer, Date[]> weekDate = new LinkedHashMap<Integer, Date[]>();

		/*
		 * 本月第一周開始日期一定為本月第一天,so以此類推日期進行疊代
		 */
		while (true) {
			// 取得當前日曆中的日期為星期幾
			int nowDay = getDayOfWeek(calendar.getTime());
			// 取得當前日期(周開始日期)
			Date nowFristDate = calendar.getTime();
			Date nowLastDate = null;

			// 如果目前周是最後一周那麼月的最後一天就是該周的最後一天,避免剩餘這周不夠7天而延伸到下月
			if (weekNow == maxWeek) {
				nowLastDate = getLastDayOfMonth(Integer.valueOf(year), Integer.valueOf(month));
			} else {
				/*
				 * 根據當前的nowFristDate是星期幾的不同來增加不同的天數到週日,從而把calendar調整到本周最後一天
				 * 比如:開始星期 增加天數 星期一 6 星期四 3 所以 增加天數 = (7 - 開始星期)
				 */
				calendar.setTime(getRecDay(nowFristDate, (7 - nowDay)));
				// 取得本周最後一天的日期,其實通過getRecDay方法已經可以取得,但為了循環必須set下calendar日期,
				// 所以直接從calendar取了
				nowLastDate = calendar.getTime();
			}

			// 每次循環都將以該周為key,該周開始結束日期(數組)為value,put到集合中
			weekDate.put(weekNow, new Date[] { nowFristDate, nowLastDate });

			// 遍歷本月所有周後返回集合
			if (weekNow == maxWeek)
				return weekDate;
			weekNow++;
			// 在取得nowLastDate的時候calendar已經到該周週末,所以將日曆往後一天既下周週一開始新一周計算
			calendar.setTime(getRecDay(nowLastDate, 1));
		}

	}

	/**
	 * 得到指定年月含有的周數
	 * 
	 * @param fristDayOfWeek
	 *            :設定每週從周幾開始.1-->週日，2-->週一....7--->週六
	 */
	public static int getMaxWeekOfMonth(String year, String month, int fristDayOfWeek) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(getLastDayOfMonth(Integer.valueOf(year), Integer.valueOf(month)));
		calendar.setFirstDayOfWeek(fristDayOfWeek);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 得到上一周或下一周中的一天
	 * 
	 * @param adjust
	 *            "1"-->下一周;"-1"--->上一周
	 * @param year
	 *            當前年
	 * @param month
	 *            當前月
	 * @param endDate
	 *            當前周的最後一天
	 * @param startDate
	 *            當前周的第一天
	 * @return 適用於工時卡：如果當前周跨月，則跳轉時返回的日期為不同的但同一周的日期（我的工時卡界面中的前後一周）
	 */
	public static Date getDayOfWeekAdjust(String adjust, String year, String month, int startDate, int endDate) {
		Date oldStartDate = begTimeOfDate(toDate(year + "/" + month + "/" + startDate));
		Date oldEndDate = endTimeOfDate(toDate(year + "/" + month + "/" + endDate));
		Date newDate = null;
		if (adjust.equalsIgnoreCase("1")) { // 下一周
											// 是本月最後一天,則往後加一天；否則加七天
			Date lastDate = getLastDayOfMonth(new Integer(year), new Integer(month));
			if (getAfterDayNumber(lastDate, oldEndDate) == 0)
				newDate = getRecDay(oldEndDate, 1);
			else if (getAfterDayNumber(lastDate, oldEndDate) >= 7)
				newDate = getRecDay(oldEndDate, 7);
			else
				newDate = lastDate;
		} else if (adjust.equalsIgnoreCase("-1")) { // 上一周
													// 是本月第一天,則往前減一天；否則減七天
			Date firstDate = getFirstDayOfMonth(new Integer(year), new Integer(month));
			if (getAfterDayNumber(oldStartDate, firstDate) == 0)
				newDate = getBeforeDate(oldStartDate, 1);
			else if (getAfterDayNumber(oldStartDate, firstDate) >= 7)
				newDate = getBeforeDate(oldStartDate, 7);
			else
				newDate = firstDate;

		}
		return newDate;
	}

	/**
	 * 在給定的年月裡返回指定的第xunNum(從1開始,其最大值為3)旬的第一天和最後一天(只考慮本月). 第一旬為:本月1日至10日
	 * 第二旬為:本月11日至20日 第三旬為:本月21日至本月最後一天
	 */
	public static Date[] getFirstAndLastDateOfXun(String year, String month, String xunNum) {
		if (StringHelper.isEmpty(xunNum))
			return null;
		int index = Integer.parseInt(xunNum);
		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		Date[] rv = new Date[2];
		rv[0] = getFirstDayOfMonth(yearInt, monthInt);
		rv[1] = getLastDayOfMonth(yearInt, monthInt);
		switch (index) {
		case 1:
			rv[1] = getRecDay(rv[0], 9);
			break;
		case 2:
			rv[0] = getRecDay(rv[0], (index - 1) * 10);
			rv[1] = getRecDay(rv[0], 9);
			break;
		case 3:
			rv[0] = getRecDay(rv[0], (index - 1) * 10);
			break;
		default:
			rv = null;
			break;
		}
		return rv;
	}

	/**
	 * 查看某日處於其所在月的第幾旬
	 */
	public static String getXunOfMonthByDate(Date date) {
		int daysAfterFirstDayOfMonthInt = getAfterDayNumber(begTimeOfDate(date),
				getFirstDayOfMonth(getYear(date), getMonth(date))) + 1;

		/* 取得日期所在的旬 */
		int xunNum = (int) Math.ceil(daysAfterFirstDayOfMonthInt / 10.0);
		if (xunNum > 3)
			xunNum = 3;
		return String.valueOf(xunNum);
	}

	/**
	 * 得到上一旬或下一旬中的一天
	 * 
	 * @param adjust
	 *            "1"-->下一旬;"-1"--->上一旬
	 * @param year
	 *            當前年
	 * @param month
	 *            當前月
	 * @param xunIndex
	 *            當前旬
	 */
	public static Date getDayOfXunAdjust(String adjust, String year, String month, String xunIndex) {
		int xunNum = Integer.parseInt(xunIndex);
		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		Date newDate = null;
		if (adjust.equalsIgnoreCase("1")) { // 下一旬
			switch (xunNum) {
			case 1:
				newDate = getRecDay(getFirstDayOfMonth(yearInt, monthInt), 10);
				break;
			case 2:
				newDate = getLastDayOfMonth(yearInt, monthInt);
				break;
			case 3:
				newDate = getRecDay(getLastDayOfMonth(yearInt, monthInt), 1);
				break;
			default:
				break;
			}
		} else if (adjust.equalsIgnoreCase("-1")) { // 上一旬
			switch (xunNum) {
			case 1:
				newDate = getRecDay(getFirstDayOfMonth(yearInt, monthInt), -1);
				break;
			case 2:
				newDate = getFirstDayOfMonth(yearInt, monthInt);
				break;
			case 3:
				newDate = getRecDay(getFirstDayOfMonth(yearInt, monthInt), 10);
				break;
			default:
				break;
			}

		}
		return newDate;
	}

	public static boolean compareToCal(Date sDate, Date eDate) {

		Calendar sCal = new GregorianCalendar();
		sCal.setTime(sDate);
		Calendar eCal = new GregorianCalendar();
		eCal.setTime(eDate);

		return (sCal.getTimeInMillis() == eCal.getTimeInMillis() || before(sDate, eDate));
	}

	public static boolean isOverTime(Date lastTime, Date currentTime, Date begTime, Date endTime) {
		return isOverTime(lastTime.getTime(), currentTime.getTime(), begTime.getTime(), endTime.getTime());
	}

	public static boolean isOverTime(Calendar lastTime, Calendar currentTime, Calendar begTime, Calendar endTime) {
		return isOverTime(lastTime.getTimeInMillis(), currentTime.getTimeInMillis(), begTime.getTimeInMillis(),
				endTime.getTimeInMillis());
	}

	/**
	 * 是否超過beg-end之間的區間
	 * 
	 * ------beg(今日凌晨3點)|-----------------|end(明日凌晨3點)
	 * 
	 * ------------------------*last(今日凌晨4點)
	 * 
	 * 1.系統時間--------------------*(今日凌晨5點) =>false
	 * 
	 * 2.系統時間--------------------------*(現在) =>false
	 * 
	 * 3.系統時間-------------------------------*(明日凌晨3點) =>true
	 * 
	 * @param lastTime
	 *            最後一次時間
	 * @param currentTime
	 *            系統時間
	 * @param begTime
	 *            起始時間
	 * @param endTime
	 *            截止時間
	 * @return
	 */
	public static boolean isOverTime(long lastTime, long currentTime, long begTime, long endTime) {
		boolean result = false;
		if ((lastTime != 0 && lastTime < begTime && currentTime >= begTime)
				|| (lastTime >= begTime && currentTime >= endTime)) {
			result = true;
		}
		return result;
	}

	/**
	 * 隨機日期,今日+一天之內的隨機毫秒
	 * 
	 * @return
	 */
	public static Date randomDate() {
		Date result = new Date();
		result.setTime(today().getTime() + NumberHelper.randomInt(24 * 60 * 60 * 1 * 1000));
		return result;
	}

	public static long randomDateLong() {
		return randomDate().getTime();
	}

	// copy from apache commons lang 2.6
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		//
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	// copy from apache commons lang 2.6
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			return false;
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

}
