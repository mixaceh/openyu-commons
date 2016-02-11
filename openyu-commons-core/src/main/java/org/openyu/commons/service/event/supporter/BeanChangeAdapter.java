package org.openyu.commons.service.event.supporter;

import org.openyu.commons.lang.event.supporter.BaseListenerSupporter;
import org.openyu.commons.service.event.BeanChangeEvent;
import org.openyu.commons.service.event.BeanChangeListener;

/**
 * 1.設計多個非單例,需要有個cache keep,以供add/remove
 * 
 * 2.設計成單例,比較容易add/remove,也比較省mem
 */
public class BeanChangeAdapter extends BaseListenerSupporter implements BeanChangeListener
{

	public BeanChangeAdapter()
	{

	}

	/**
	 * 初始
	 */
	public void initialize()
	{

	}

	/**
	 * 改變前
	 * 
	 * @param beanChangeEvent
	 */
	public void beanChanging(BeanChangeEvent beanChangeEvent)
	{

	}

	/**
	 * 改變後
	 * 
	 * @param beanChangeEvent
	 */
	public void beanChanged(BeanChangeEvent beanChangeEvent)
	{

	}

}
