package org.openyu.commons.cat.service.event.impl;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.cat.vo.impl.CatImpl;

public class CatFieldAdapterTest
{

	private static ConcurrentMap<String, CatImpl> beans = new ConcurrentHashMap<String, CatImpl>();

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

	/**
	 * 建構beans模擬資料
	 * 
	 * @return
	 */
	private ConcurrentMap<String, CatImpl> createMockCats()
	{
		ConcurrentMap<String, CatImpl> cats = new ConcurrentHashMap<String, CatImpl>();

		CatImpl cat = new CatImpl();
		cat.setId("aaa");
		cats.put(cat.getId(), cat);
		//
		cat = new CatImpl();
		cat.setId("bbb");
		cats.put(cat.getId(), cat);
		//
		cat = new CatImpl();
		cat.setId("ccc");
		cats.put(cat.getId(), cat);
		return cats;
	}

	@Test
	/**
	 * 單例
	 */
	//1000000 times: 1065 mills. 
	//1000000 times: 973 mills. 
	//1000000 times: 968 mills. 
	public void beanChangedSingleton()
	{
		final String CODE = "aaa";

		//模擬資料
		beans = createMockCats();
		CatImpl cat = beans.get(CODE);

		//連線時,addBeanChangeListener
		//connect(CODE);
		cat.addBeanChangeListener(CatFieldAdapter.getInstance());

		//多加2次單例
		cat.addBeanChangeListener(CatFieldAdapter.getInstance());
		cat.addBeanChangeListener(CatFieldAdapter.getInstance());
		//
		System.out.println(cat.getBeanChangeListeners().length);
		assertEquals(1, cat.getBeanChangeListeners().length);

		//------------------------------------------

		cat.setAge(10);
		//
		int count = 1;//100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			cat.setAge(9);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		//------------------------------------------

		//斷線時,removeBeanChangeListener
		//disconnect(CODE);
		cat.removeBeanChangeListener(CatFieldAdapter.getInstance());

		System.out.println(cat.getBeanChangeListeners());
		assertNull(cat.getBeanChangeListeners());
		//
		cat = beans.get("bbb");
		cat.setAge(10);
		//

	}
}
