package org.openyu.commons.lang;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloneHelperTest
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
	//no cache
	//1000000 times: 515 mills.
	//1000000 times: 537 mills.
	//1000000 times: 509 mills.
	//
	//cache
	//1000000 times: 533 mills.
	//1000000 times: 531 mills.
	//1000000 times: 530 mills.
	public void genericClone()
	{
		Date value = new Date();
		System.out.println("value: " + value);
		//
		Date cloneValue = null;

		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			cloneValue = CloneHelper.genericClone(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		cloneValue.setYear(50);//1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	//1000000 times: 1771 mills.
	//1000000 times: 1778 mills.
	//1000000 times: 1818 mills.
	//
	//#fix
	//1000000 times: 1311 mills.
	//1000000 times: 1316 mills.
	//1000000 times: 1327 mills.
	public void deepCloneByArray()
	{
		Object[] values = new Object[] { new Date(), "aaa", 123 };
		SystemHelper.println(values);
		//
		Object[] cloneValues = null;
		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//cloneValue = CloneHelper.genericClone(value);
			cloneValues = CloneHelper.deepClone(values);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Date date = (Date) cloneValues[0];
		date.setYear(50);//1950
		SystemHelper.println(cloneValues);//1950
		SystemHelper.println(values);//今天日期
	}

	@Test
	//1000000 times: 1288 mills.
	//1000000 times: 1292 mills.
	//1000000 times: 1297 mills.
	//
	//#fix
	//1000000 times: 1012 mills. 
	//1000000 times: 1016 mills. 
	//1000000 times: 1015 mills. 
	public void deepCloneByList()
	{
		List value = new LinkedList();
		value.add(new Date());
		value.add(Locale.TRADITIONAL_CHINESE);
		System.out.println("value: " + value);
		//
		List cloneValue = null;
		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//cloneValue = CloneHelper.genericClone(value);
			cloneValue = CloneHelper.deepClone(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Date date = (Date) cloneValue.get(0);
		date.setYear(50);//1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	//1000000 times: 1771 mills.
	//1000000 times: 1778 mills.
	//1000000 times: 1818 mills.
	//
	//#fix
	//1000000 times: 1311 mills.
	//1000000 times: 1316 mills.
	//1000000 times: 1327 mills.
	public void deepCloneByMap()
	{
		Map value = new LinkedHashMap();
		value.put(1, new Date());
		value.put(2, Locale.TRADITIONAL_CHINESE);
		System.out.println("value: " + value);
		//
		Map cloneValue = null;
		int count = 1000000;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			//cloneValue = CloneHelper.genericClone(value);
			cloneValue = CloneHelper.deepClone(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Date date = (Date) cloneValue.get(1);
		date.setYear(50);//1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	public void cloneByList()
	{
		List<String> value = new LinkedList<String>();
		value.add("aaa");
		value.add("bbb");
		value.add("ccc");
		System.out.println("value: " + value);
		//
		List<String> result = null;
		int count = 1;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = CloneHelper.clone(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}
}
