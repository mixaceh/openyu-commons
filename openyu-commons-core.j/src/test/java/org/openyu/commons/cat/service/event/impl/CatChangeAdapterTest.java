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

public class CatChangeAdapterTest
{

	private static ConcurrentMap<String, CatImpl> beans = new ConcurrentHashMap<String, CatImpl>();

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

	private void connect(String code)
	{
		CatImpl cat = beans.get(code);
		cat.addBeanChangeListener(CatChangeAdapter.getInstance());
	}

	private void disconnect(String code)
	{
		CatImpl cat = beans.get(code);
		cat.removeBeanChangeListener(CatChangeAdapter.getInstance());
	}

	@Test
	/**
	 * 多個非單例
	 */
	public void beanChanged()
	{
		CatChangeAdapter catBeanChangeAdapter = new CatChangeAdapter();
		CatChangeAdapter catBeanChangeAdapter2 = new CatChangeAdapter();
		//
		CatImpl cat = new CatImpl();
		cat.addBeanChangeListener(catBeanChangeAdapter);
		cat.addBeanChangeListener(catBeanChangeAdapter2);
		//
		cat.setId("aaa");
		cat.setId("bbb");
		//
		cat.setAge(1);
		cat.setAge(2);
		cat.setAge(3);
		//
		System.out.println(cat.getBeanChangeListeners().length);
		assertEquals(2, cat.getBeanChangeListeners().length);

		//只移除1個
		cat.removeBeanChangeListener(catBeanChangeAdapter);
		System.out.println(cat.getBeanChangeListeners().length);
		assertEquals(1, cat.getBeanChangeListeners().length);
	}

	@Test
	/**
	 * 單例
	 */
	//1000000 times: 327 mills. 
	//1000000 times: 343 mills. 
	//1000000 times: 344 mills. 
	public void beanChangedSingleton()
	{
		final String CODE = "aaa";

		//模擬資料
		beans = createMockCats();
		CatImpl cat = beans.get(CODE);

		//連線時,addBeanChangeListener
		connect(CODE);

		//多加2次單例
		cat.addBeanChangeListener(CatChangeAdapter.getInstance());
		cat.addBeanChangeListener(CatChangeAdapter.getInstance());
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
		disconnect(CODE);

		System.out.println(cat.getBeanChangeListeners());
		assertNull(cat.getBeanChangeListeners());
		//
		cat = beans.get("bbb");
		cat.setAge(10);
		//
	}

}
