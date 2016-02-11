package org.openyu.commons.bean.supporter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.jaxb.adapter.DateXmlAdapter;

@XmlRootElement(name = "auditBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditBeanSupporter extends BaseBeanSupporter implements AuditBean
{

	private static final long serialVersionUID = -898112908473679921L;

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date createDate;

	private String createUser;

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date modifiedDate;

	private String modifiedUser;

	public AuditBeanSupporter()
	{}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getModifiedDate()
	{
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedUser()
	{
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser)
	{
		this.modifiedUser = modifiedUser;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("createDate", createDate);
		builder.append("createUser", createUser);
		builder.append("modifiedDate", modifiedDate);
		builder.append("modifiedUser", modifiedUser);
		return builder.toString();
	}

	public Object clone()
	{
		AuditBeanSupporter copy = null;
		copy = (AuditBeanSupporter) super.clone();
		copy.createDate = clone(createDate);
		copy.modifiedDate = clone(modifiedDate);
		return copy;
	}
}
