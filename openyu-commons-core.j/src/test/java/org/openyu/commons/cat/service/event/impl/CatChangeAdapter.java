package org.openyu.commons.cat.service.event.impl;

import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.service.event.BeanChangeEvent;
import org.openyu.commons.service.event.supporter.BeanChangeAdapter;

/**
 * 1.設計成singleton, 使用getInstance
 * 
 * 2.若用spring,不需使用getInstance,直接在xml上設定即可,但要怎註冊到bean上?
 */
public class CatChangeAdapter extends BeanChangeAdapter
{
	private static CatChangeAdapter instance;

	public CatChangeAdapter()
	{}

	public static synchronized CatChangeAdapter getInstance()
	{
		if (instance == null)
		{
			instance = new CatChangeAdapter();
		}
		return instance;
	}

	@Override
	public void beanChanged(BeanChangeEvent beanChangeEvent)
	{
		CatImpl cat = (CatImpl) beanChangeEvent.getSource();
		if (cat != null)
		{
			String fieldName = beanChangeEvent.getFieldName();
			if ("code".equals(fieldName))
			{
				@SuppressWarnings("unchecked")
				EventAttach<String, String> eventAttach = ((EventAttach<String, String>) beanChangeEvent
						.getAttach());
				//
				String oldValue = eventAttach.getOldValue();
				String newValue = eventAttach.getNewValue();
				Double diffValue = eventAttach.getDiffValue();
				//
				//System.out.println(eventAttach);
				System.out.println("code: " + oldValue + ", " + newValue + ", " + diffValue);
			}
			else if ("age".equals(fieldName))
			{
				@SuppressWarnings("unchecked")
				EventAttach<Integer, Integer> eventAttach = ((EventAttach<Integer, Integer>) beanChangeEvent
						.getAttach());
				//
				Integer oldValue = eventAttach.getOldValue();
				Integer newValue = eventAttach.getNewValue();
				Double diffValue = eventAttach.getDiffValue();
				//
				//System.out.println(eventAttach);
				System.out.println("age: " + oldValue + ", " + newValue + ", " + diffValue);
			}
			else
			{
				//just for pretty
			}
		}

	}
}
