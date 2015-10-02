package org.openyu.commons.lang;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.lang.event.EventHelper;

public class EventHelperTest
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
	//1000000 times: 86 mills. 
	//1000000 times: 82 mills. 
	//1000000 times: 81 mills. 
	public void eventAttach()
	{
		EventAttach<String, String> result = null;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = EventHelper.eventAttach("123", "456");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		//
		String oldValue = result.getOldValue();
		String newValue = result.getNewValue();
		Double diffValue = result.getDiffValue();
		//
		System.out.println("code: " + oldValue + " " + newValue + ", " + diffValue);
	}

}
