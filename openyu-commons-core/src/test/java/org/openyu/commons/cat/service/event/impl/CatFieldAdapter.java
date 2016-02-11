package org.openyu.commons.cat.service.event.impl;

import org.openyu.commons.cat.vo.impl.CatField;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.service.event.BeanChangeEvent;
import org.openyu.commons.service.event.supporter.BeanChangeAdapter;

/**
 * 1.設計成singleton, 使用getInstance
 * 
 * 2.若用spring,不需使用getInstance,直接在xml上設定即可,但要怎註冊到bean上?
 */
public class CatFieldAdapter extends BeanChangeAdapter
{
	private static CatFieldAdapter instance;

	public CatFieldAdapter()
	{}

	public static synchronized CatFieldAdapter getInstance()
	{
		if (instance == null)
		{
			instance = new CatFieldAdapter();
		}
		return instance;
	}

	public void beanChanged(BeanChangeEvent beanChangeEvent)
	{
		CatImpl cat = (CatImpl) beanChangeEvent.getSource();
		if (cat != null)
		{
			Enum<?> fieldEnum = beanChangeEvent.getFieldEnum();
			if (fieldEnum == CatField.ID)
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
				System.out
						.println(fieldEnum + ", " + oldValue + ", " + newValue + ", " + diffValue);
			}
			else if (fieldEnum == CatField.AGE)
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
				System.out
						.println(fieldEnum + ", " + oldValue + ", " + newValue + ", " + diffValue);
			}
			else
			{
				//just for pretty
			}
		}

	}
}
