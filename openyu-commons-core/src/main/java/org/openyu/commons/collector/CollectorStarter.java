package org.openyu.commons.collector;

import java.util.List;

import org.openyu.commons.bean.BaseBean;

/**
 * collector啟動
 */
public interface CollectorStarter extends BaseBean
{

	/**
	 * collector類別名稱
	 * 
	 * @return
	 */
	List<String> getNames();

	void setNames(List<String> names);
}
