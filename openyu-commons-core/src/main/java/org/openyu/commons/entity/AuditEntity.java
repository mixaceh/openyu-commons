package org.openyu.commons.entity;

import java.util.Date;

public interface AuditEntity extends BaseEntity
{

	String KEY = AuditEntity.class.getName();

	Date getCreateDate();

	void setCreateDate(Date createDate);

	String getCreateUser();

	void setCreateUser(String createUser);

	Date getModifiedDate();

	void setModifiedDate(Date modifiedDate);

	String getModifiedUser();

	void setModifiedUser(String modifiedUser);

}
