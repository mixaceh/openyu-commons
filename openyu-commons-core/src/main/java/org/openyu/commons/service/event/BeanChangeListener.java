package org.openyu.commons.service.event;

import org.openyu.commons.lang.event.BaseListener;

/**
 * bean改變監聽器
 */
public interface BeanChangeListener extends BaseListener
{
	/**
	 * 改變前
	 * 
	 * @param beanChangeEvent
	 */
	void beanChanging(BeanChangeEvent beanChangeEvent);

	/**
	 * 改變後
	 * 
	 * @param beanChangeEvent
	 */
	void beanChanged(BeanChangeEvent beanChangeEvent);
}
