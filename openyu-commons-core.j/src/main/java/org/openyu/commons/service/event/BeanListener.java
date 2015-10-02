package org.openyu.commons.service.event;

import org.openyu.commons.lang.event.BaseListener;

/**
 * bean監聽器
 * 
 * inserted(added)
 * 
 * updated(replaced)
 * 
 * deleted(removed)
 * 
 * found(found)
 */
public interface BeanListener extends BaseListener
{

	/**
	 * 新增前
	 * 
	 * @param beanEvent
	 */
	void beanInserting(BeanEvent beanEvent);

	/**
	 * 新增後
	 * 
	 * @param beanEvent
	 */
	void beanInserted(BeanEvent beanEvent);

	/**
	 * 查詢前
	 * 
	 * @param beanEvent
	 */
	void beanFinding(BeanEvent beanEvent);

	/**
	 * 查詢後
	 * 
	 * @param beanEvent
	 */
	void beanFound(BeanEvent beanEvent);

	/**
	 * 修改前
	 * 
	 * @param beanEvent
	 */
	void beanUpdating(BeanEvent beanEvent);

	/**
	 * 修改後
	 * 
	 * @param beanEvent
	 */
	void beanUpdated(BeanEvent beanEvent);

	/**
	 * 刪除前
	 * 
	 * @param beanEvent
	 */
	void beanDeleting(BeanEvent beanEvent);

	/**
	 * 刪除後
	 * 
	 * @param beanEvent
	 */
	void beanDeleted(BeanEvent beanEvent);

}
