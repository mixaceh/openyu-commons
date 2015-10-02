package org.openyu.commons.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateHelperTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void today() {
		Date result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.today();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(DateHelper.toString(result));
		System.out.println(DateHelper.toString(result,
				DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	// #issue: 有點慢
	// 1000000 times: 2921 mills.
	// 1000000 times: 2925 mills.
	// 1000000 times: 2946 mills.
	//
	// #fix
	// 1000000 times: 1990 mills.
	// 1000000 times: 1989 mills.
	// 1000000 times: 1954 mills.
	public void toDate() {
		Date result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 2012/02/09 09:57:23:671
			// 2012/02/09 21:57:49:908
			result = DateHelper.toDate("2012/02/09 21:57:49:908",
					DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(DateHelper.toString(result,
				DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	public void toCalendar() {
		Calendar result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.toCalendar(DateHelper.today());
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(CalendarHelper.toString(result));
		System.out.println(CalendarHelper.toString(result,
				DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	// #issue: 有點慢
	// 1000000 times: 2607 mills.
	// 1000000 times: 2643 mills.
	// 1000000 times: 2618 mills.
	//
	// #fix
	// 1000000 times: 1547 mills.
	// 1000000 times: 1558 mills.
	// 1000000 times: 1536 mills.
	public void toStringz() {
		String result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 2012/02/09 09:57:23.671
			// 2012/02/09 21:57:49.908
			result = DateHelper.toString(DateHelper.today(),
					DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 324 mills.
	// 1000000 times: 312 mills.
	// 1000000 times: 296 mills.
	public void createDateFormat() {
		DateFormat result = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.createDateFormat();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 324 mills.
	// 1000000 times: 333 mills.
	// 1000000 times: 333 mills.
	public void createSimpleDateFormat() {
		SimpleDateFormat result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.createSimpleDateFormat();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 627 mills.
	// 1000000 times: 628 mills.
	// 1000000 times: 631 mills.
	public void begTimeOfDate() {
		Date date = DateHelper.today();
		Date begTime = null;
		Date endTime = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			begTime = DateHelper.begTimeOfDate(date);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(DateHelper.toString(begTime));

		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			endTime = DateHelper.endTimeOfDate(date);
		}
		//
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println(DateHelper.toString(endTime));
	}

	@Test
	// toString
	// 1000000 times: 2072 mills.
	// 1000000 times: 2059 mills.
	// 1000000 times: 2076 mills.
	//
	// toDate
	// 1000000 times: 2674 mills.
	// 1000000 times: 2575 mills.
	// 1000000 times: 2076 mills.
	public void toStringAndToDate() {
		Date today = DateHelper.today();
		String toString = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			toString = DateHelper.toString(today,
					DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(toString);

		//
		Date toDate = null;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			toDate = DateHelper.toDate(toString,
					DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		// System.out.println(toDate);
		//
		System.out.println(DateHelper.toString(toDate,
				DateHelper.DATE_TIME_MILLS_PATTERN));
		//
		System.out.println("today: " + today.getTime());
		System.out.println("toDate: " + toDate.getTime());

		System.out.println("today==toDate: "
				+ (today.getTime() == toDate.getTime()));
	}

	@Test
	// 1000000 times: 104 mills.
	// 1000000 times: 78 mills.
	// 1000000 times: 82 mills.
	public void isOverTime() {
		Calendar begTime = Calendar.getInstance();
		// 今日早上3點
		begTime.set(Calendar.HOUR_OF_DAY, 3);
		begTime.set(Calendar.MINUTE, 0);
		begTime.set(Calendar.SECOND, 0);

		// 明日早上3點
		Calendar endTime = (Calendar) begTime.clone();
		endTime.add(Calendar.DATE, 1);

		// 今日凌晨4點
		Calendar lastTime = Calendar.getInstance();
		lastTime.set(Calendar.HOUR_OF_DAY, 4);
		lastTime.set(Calendar.MINUTE, 0);
		lastTime.set(Calendar.SECOND, 0);

		// 系統時間:今日凌晨5點
		Calendar todaySystem = Calendar.getInstance();
		todaySystem.set(Calendar.HOUR_OF_DAY, 5);
		todaySystem.set(Calendar.MINUTE, 0);
		todaySystem.set(Calendar.SECOND, 0);

		boolean isOverTime = false;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			isOverTime = DateHelper.isOverTime(lastTime, todaySystem, begTime,
					endTime);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(CalendarHelper.toString(todaySystem) + " "
				+ isOverTime);

		// 系統時間:現在
		Calendar currentSystem = Calendar.getInstance();
		isOverTime = DateHelper.isOverTime(lastTime, currentSystem, begTime,
				endTime);
		System.out.println(CalendarHelper.toString(currentSystem) + " "
				+ isOverTime);

		// 系統時間:明日凌晨3點
		Calendar tomorrowSystem = Calendar.getInstance();
		tomorrowSystem.set(Calendar.HOUR_OF_DAY, 3);
		tomorrowSystem.set(Calendar.MINUTE, 0);
		tomorrowSystem.set(Calendar.SECOND, 0);
		tomorrowSystem.add(Calendar.DATE, 1);
		isOverTime = DateHelper.isOverTime(lastTime, tomorrowSystem, begTime,
				endTime);
		System.out.println(CalendarHelper.toString(tomorrowSystem) + " "
				+ isOverTime);
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomDate() {
		Date result = null;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.randomDate();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 95 mills.
	// 1000000 times: 111 mills.
	// 1000000 times: 104 mills.
	//
	// verified
	public void randomDateLong() {
		long result = 0L;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = DateHelper.randomDateLong();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	public void dateToLong() {
		System.out.println(DateHelper.toString(1349627127788L));

		// 1900起算
		Date date = new Date();
		date.setYear(0);
		System.out.println(date);
		//
		System.out.println("today: " + DateHelper.today().getTime());
		System.out.println("yesterday: " + DateHelper.yesterday().getTime());
		//
		System.out.println(new Date(1362248049307L));
		//
		date = new Date();
		date.setMonth(date.getMonth() - 1);
		System.out.println(date + " " + date.getTime());
		//
		date = new Date();
		date.setMonth(date.getMonth() + 1);
		System.out.println(date + " " + date.getTime());
	}

	@Test
	public void parse() {
		try {
			// String value = "Thu Sep 28 20:29:30 JST 2000";
			// DateFormat df = new
			// SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

			String value = "Mon, 13 May 2013 11:28:19 +0800";
			SimpleDateFormat df = new SimpleDateFormat(
					"EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
			Date result = df.parse(value);
			System.out.println(result);
			//
			System.out.println(df.format(result));
			//
			System.out.println(new Date());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void getYearMonth() {
		Date result = DateHelper.today();

		System.out.println(DateHelper.toString(result));
		System.out.println(DateHelper.toString(result, "yyyyMM"));
	}
}
