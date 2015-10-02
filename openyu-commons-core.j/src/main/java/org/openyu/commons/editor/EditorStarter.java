package org.openyu.commons.editor;

import java.util.List;

import org.openyu.commons.bean.BaseBean;

/**
 * editor啟動
 */
public interface EditorStarter extends BaseBean
{

	/**
	 * editor類別名稱
	 * 
	 * @return
	 */
	List<String> getNames();

	void setNames(List<String> names);
}
