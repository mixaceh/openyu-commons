package org.openyu.commons.util;


import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class CalendarHelperTest
{

	@Test
	public void today()
	{
		Calendar value = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			value = CalendarHelper.today();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(CalendarHelper.toString(value));
		System.out.println(CalendarHelper.toString(value, DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	//1000000 times: 450 mills.
	//1000000 times: 480 mills.
	//1000000 times: 487 mills.
	public void toDate()
	{
		Date value = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			value = CalendarHelper.toDate(CalendarHelper.today());
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(DateHelper.toString(value));
		System.out.println(DateHelper.toString(value, DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	//1000000 times: 2512 mills.
	//1000000 times: 2789 mills.
	//1000000 times: 2792 mills. 
	public void toCalendar()
	{
		Calendar value = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			value = CalendarHelper.toCalendar("2012/03/02 13:42:17:534",
				DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(CalendarHelper.toString(value));
		System.out.println(CalendarHelper.toString(value, DateHelper.DATE_TIME_MILLS_PATTERN));
	}

	@Test
	//1000000 times: 1760 mills. 
	//1000000 times: 1722 mills. 
	//1000000 times: 1763 mills. 
	public void toStringz()
	{
		Calendar today = CalendarHelper.today();
		String value = null;
		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//2012/02/09 09:57:23:671
			//2012/02/09 21:57:49:908
			value = CalendarHelper.toString(today, DateHelper.DATE_TIME_MILLS_PATTERN);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(value);
	}

	@Test
	//1000000 times: 627 mills. 
	//1000000 times: 628 mills. 
	//1000000 times: 631 mills. 
	public void begTimeOfDate()
	{
		Calendar calendar = CalendarHelper.today();
		Calendar begTime = null;
		Calendar endTime = null;
		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			begTime = CalendarHelper.begTimeOfDate(calendar);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(CalendarHelper.toString(begTime));

		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			endTime = CalendarHelper.endTimeOfDate(calendar);
		}
		//
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println(CalendarHelper.toString(endTime));
	}

	@Test
	//1000000 times: 733 mills. 
	//1000000 times: 770 mills. 
	//1000000 times: 746 mills. 
	//
	//verified
	public void randomCalendar()
	{
		Calendar result = null;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = CalendarHelper.randomCalendar();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	//1000000 times: 736 mills. 
	//1000000 times: 748 mills. 
	//1000000 times: 719 mills. 
	//
	//verified
	public void randomCalendarLong()
	{
		long result = 0L;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = CalendarHelper.randomCalendarLong();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}
}
