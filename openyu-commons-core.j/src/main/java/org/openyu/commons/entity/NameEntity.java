package org.openyu.commons.entity;

public interface NameEntity extends BaseEntity
{

	String KEY = NameEntity.class.getName();

	String getName();

	void setName(String name);

}
