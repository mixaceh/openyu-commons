package org.openyu.commons.awt.control;

import java.awt.Component;
import org.openyu.commons.service.BaseService;

;
/**
 * 基礎控制器
 */
public interface BaseControl extends BaseService {

	/**
	 * id
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

	/**
	 * 元件
	 * 
	 * @return
	 */
	Component getComponent();

	void setComponent(Component component);

	/**
	 * 顯示
	 * 
	 * @param visible
	 */
	void setVisible(boolean visible);
}
