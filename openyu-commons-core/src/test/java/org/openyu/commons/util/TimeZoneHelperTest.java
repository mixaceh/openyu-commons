package org.openyu.commons.util;

import java.util.TimeZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeZoneHelperTest
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
	public void getTimeZone()
	{
		TimeZone result = TimeZoneHelper.getTimeZone();
		System.out.println(result);//id="Asia/Taipei",offset=28800000,dstSavings=0,useDaylight=false,transitions=42,lastRule=null
	}

	@Test
	public void toTimeZone()
	{
		TimeZone result = TimeZoneHelper.toTimeZone("GMT+9");
		System.out.println(result);//id="GMT+09:00",offset=32400000,dstSavings=0,useDaylight=false,transitions=0,lastRule=null

	}

}
