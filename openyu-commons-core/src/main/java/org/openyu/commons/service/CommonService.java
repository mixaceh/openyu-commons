package org.openyu.commons.service;

import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.util.concurrent.NullValueMap;

/**
 * Jdbc+Generic服務
 */
public interface CommonService extends JdbcService, GenericService {
	/**
	 * 存放共用的beans,當多緒時,物件需注意資料同步
	 * 
	 * @return
	 */
	NullValueMap<String, Object> getBeans();

	/**
	 * 設定監聽器
	 * 
	 * @param beanListener
	 */
	void setBeanListener(BeanListener beanListener);

	/**
	 * 加入監聽器
	 * 
	 * @param beanListener
	 */
	void addBeanListener(BeanListener beanListener);

	/**
	 * 移除監聽器
	 * 
	 * @param beanListener
	 */
	void removeBeanListener(BeanListener beanListener);

	/**
	 * 取得所有監聽器
	 * 
	 * @return
	 */
	BeanListener[] getBeanListeners();
}
