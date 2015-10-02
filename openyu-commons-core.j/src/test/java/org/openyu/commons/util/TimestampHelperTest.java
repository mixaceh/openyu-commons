package org.openyu.commons.util;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimestampHelperTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{}

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{}

	@Test
	//1000000 times: 703 mills. 
	//1000000 times: 718 mills. 
	//1000000 times: 703 mills. 
	public void toTimestamp()
	{
		Timestamp result = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//2012/02/09 09:57:23:671
			//2012/02/09 21:57:49:908
			result = TimestampHelper.toTimestamp("2012-04-12 11:59:19.257");
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	//1000000 times: 937 mills. 
	//1000000 times: 954 mills. 
	//1000000 times: 937 mills. 
	public void toStringz()
	{
		String result = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//2012/02/09 09:57:23:671
			//2012/02/09 21:57:49:908
			result = TimestampHelper.toString(new Timestamp(new Date().getTime()));
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}
}
