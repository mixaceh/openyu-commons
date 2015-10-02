package org.openyu.commons.bean;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * AuditBean => AuditEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface AuditBean extends BaseBean
{

	String KEY = AuditBean.class.getName();

	Date getCreateDate();

	void setCreateDate(Date createDate);

	String getCreateUser();

	void setCreateUser(String createUser);

	Date getModifiedDate();

	void setModifiedDate(Date modifiedDate);

	String getModifiedUser();

	void setModifiedUser(String modifiedUser);

}
