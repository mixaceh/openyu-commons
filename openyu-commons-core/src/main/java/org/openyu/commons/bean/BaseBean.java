package org.openyu.commons.bean;

import org.openyu.commons.model.BaseModel;
import org.openyu.commons.service.event.BeanChangeListener;

/**
 * BaseBean => BaseEntity
 * 
 * 有事件觸發
 */
public interface BaseBean extends BaseModel
{

	String KEY = BaseBean.class.getName();

	/**
	 * 加入監聽器
	 * 
	 * @param beanChangeListener
	 */
	void addBeanChangeListener(BeanChangeListener beanChangeListener);

	/**
	 * 移除監聽器
	 * 
	 * @param beanChangeListener
	 */
	void removeBeanChangeListener(BeanChangeListener beanChangeListener);

	/**
	 * 取得所有監聽器
	 * 
	 * @return
	 */
	BeanChangeListener[] getBeanChangeListeners();
}
