package org.openyu.commons.entity;

public interface IdEntity extends BaseEntity
{

	String KEY = IdEntity.class.getName();

	/**
	 * id,商業邏輯用
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

}
