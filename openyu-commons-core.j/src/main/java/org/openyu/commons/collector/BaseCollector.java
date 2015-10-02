package org.openyu.commons.collector;

import org.openyu.commons.model.BaseModel;

/**
 * 收集器
 */
public interface BaseCollector extends BaseModel {

	String KEY = BaseCollector.class.getName();

	/**
	 * id
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

}
