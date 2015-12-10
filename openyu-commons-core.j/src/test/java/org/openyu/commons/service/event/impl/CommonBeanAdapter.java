package org.openyu.commons.service.event.impl;

import java.util.Collection;

import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.service.CommonService;
import org.openyu.commons.service.event.BeanEvent;
import org.openyu.commons.service.event.supporter.BeanAdapter;

/**
 * 事件監聽器
 * 
 * 1.若設計成singleton, 使用getInstance
 * 
 * 2.若用spring,不需使用getInstance,直接在xml上設定即可
 * 
 * 3.當insert,update,delete,find時所觸發的事件處理
 * 
 * 4.觸發之後,會將bean/params value,存到 xxxServiceImpl.beans上,要取出就用getBeanCache()
 */
public class CommonBeanAdapter extends BeanAdapter
{

	public CommonBeanAdapter()
	{

	}

	// #issue: 在還沒insert db前,id=null
	// #fix: 新增到db後,再放入mem
	public void beanInserted(BeanEvent beanEvent)
	{
		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			System.out.println("beanInserted: " + beanEvent.getName() + " (" + beanEvent.getType()
					+ ")");

			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();
			//
			String key = getKey(newValue);
			if (key != null)
			{
				commonService.getBeans().put(key, newValue);
			}
		}
	}

	//從db找到後,再放入mem
	public void beanFound(BeanEvent beanEvent)
	{
		System.out.println("beanFound: " + beanEvent.getName() + " (" + beanEvent.getType() + ")");

		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();//有可能是集合
			//
			if (newValue instanceof Collection)
			{
				Collection<?> collection = (Collection<?>) newValue;
				for (Object object : collection)
				{
					String key = getKey(object);
					if (key != null)
					{
						commonService.getBeans().put(key, object);
					}
				}
			}
			else
			{
				String key = getKey(newValue);
				if (key != null)
				{
					commonService.getBeans().put(key, newValue);
				}
			}
		}
	}

	//先放在mem
	public void beanUpdating(BeanEvent beanEvent)
	{
		System.out.println("beanUpdating: " + beanEvent.getName() + " (" + beanEvent.getType()
				+ ")");

		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();
			//
			String key = getKey(newValue);
			if (key != null)
			{
				commonService.getBeans().put(key, newValue);
			}
		}
	}

	public void beanUpdated(BeanEvent beanEvent)
	{
		System.out
				.println("beanUpdated: " + beanEvent.getName() + " (" + beanEvent.getType() + ")");

		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();
			//
			String key = getKey(newValue);
			if (key != null)
			{
				commonService.getBeans().put(key, newValue);
			}
		}
	}

	//先從mem刪除
	public void beanDeleting(BeanEvent beanEvent)
	{
		System.out.println("beanDeleting: " + beanEvent.getName() + " (" + beanEvent.getType()
				+ ")");

		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();
			//
			String key = getKey(newValue);
			//若存在則從beans刪除
			if (key != null)
			{
				commonService.getBeans().remove(key);
			}
		}
	}

	public void beanDeleted(BeanEvent beanEvent)
	{
		System.out
				.println("beanDeleted: " + beanEvent.getName() + " (" + beanEvent.getType() + ")");

		CommonService commonService = (CommonService) beanEvent.getSource();
		if (commonService != null)
		{
			@SuppressWarnings("unchecked")
			EventAttach<Object, Object> eventAttach = ((EventAttach<Object, Object>) beanEvent
					.getAttach());
			//
			Object newValue = eventAttach.getNewValue();
			//
			String key = getKey(newValue);
			//若存在則從beans刪除
			if (key != null)
			{
				commonService.getBeans().remove(key);
			}
		}
	}
}
