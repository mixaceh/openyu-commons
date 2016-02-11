package org.openyu.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * 僅限台灣使用
 */
public final class TaiwanDateHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(TaiwanDateHelper.class);

	private static String defaultDatePattern = null;

	private static String timePattern = "HH:mm";

	private TaiwanDateHelper() {
		throw new HelperException(
				new StringBuilder().append(TaiwanDateHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * Return default datePattern (yyy/MM/dd)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static synchronized String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = "yyy/MM/dd";
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyy/MM/dd";
		}

		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return getDatePattern() + " " + timePattern;
	}

	public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;

		df = new SimpleDateFormat(aMask);

		try {
			Calendar calendar = Calendar.getInstance();
			date = df.parse(strDate);
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, 1911);
			date = calendar.getTime();
		} catch (ParseException pe) {
			LOGGER.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	public static Date convertStringToYearMonth(String strDate) throws ParseException {
		return convertStringToDate("yyy/MM", strDate);
	}

	public static Date convertStringToDate(String strDate) throws ParseException {
		return convertStringToDate(getDatePattern(), strDate);
	}

	public static Date convertStringToDateTime(String strDate) throws ParseException {
		return convertStringToDate(getDateTimePattern(), strDate);
	}

	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
			// log.error("aDate is null!");
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(aDate);
			// TODO 先凑合着用
			String rcyear = Integer.toString(calendar.get(Calendar.YEAR) - 1911);
			if (rcyear.length() == 2)
				rcyear = "0".concat(rcyear);
			else if (rcyear.length() == 1)
				rcyear = "00".concat(rcyear);
			aMask = aMask.replaceAll("yyy", rcyear);
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(calendar.getTime());
		}
		return returnValue;
	}

	public static String getDateTime(Date aDate) {
		return getDateTime(getDateTimePattern(), aDate);
	}

	public static String getDate(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * 
	 * @param aDate
	 * @return
	 */
	public static String getYearMonth(Date aDate) {
		return getDateTime("yyy/MM", aDate);
	}

	/**
	 * 
	 * @param aDate
	 * @return
	 */
	public static String getYear(Date aDate) {
		return getDateTime("yyy", aDate);
	}

	/**
	 * 
	 * @return
	 */
	public static String getYear() {
		return getYear(new Date());
	}

}
