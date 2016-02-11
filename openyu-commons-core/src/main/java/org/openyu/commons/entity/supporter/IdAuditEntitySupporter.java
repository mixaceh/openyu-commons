package org.openyu.commons.entity.supporter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.IdAuditEntity;
import org.openyu.commons.mark.Supporter;

@Deprecated
/**
 * 因合併多個欄位成一個欄位,故不用再用此class,處理多個欄位 
 */
@MappedSuperclass
public class IdAuditEntitySupporter extends IdEntitySupporter implements IdAuditEntity,
		Supporter
{

	private static final long serialVersionUID = -9140718775326705030L;

	private AuditEntity audit = new AuditEntitySupporter();

	public IdAuditEntitySupporter()
	{}

	@Column(name = "create_date")
	public Date getCreateDate()
	{
		return audit.getCreateDate();
	}

	public void setCreateDate(Date createDate)
	{
		audit.setCreateDate(createDate);
	}

	@Column(name = "create_user", length = 20)
	public String getCreateUser()
	{
		return audit.getCreateUser();
	}

	public void setCreateUser(String createUser)
	{
		audit.setCreateUser(createUser);
	}

	@Column(name = "modified_date")
	public Date getModifiedDate()
	{
		return audit.getModifiedDate();
	}

	public void setModifiedDate(Date modifiedDate)
	{
		audit.setModifiedDate(modifiedDate);
	}

	@Column(name = "modified_user", length = 20)
	public String getModifiedUser()
	{
		return audit.getModifiedUser();
	}

	public void setModifiedUser(String modifiedUser)
	{
		audit.setModifiedUser(modifiedUser);
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("createDate", getCreateDate());
		builder.append("createUser", getCreateUser());
		builder.append("modifiedDate", getModifiedDate());
		builder.append("modifiedUser", getModifiedUser());
		return builder.toString();
	}

	public Object clone()
	{
		IdAuditEntitySupporter copy = null;
		copy = (IdAuditEntitySupporter) super.clone();
		copy.audit = (AuditEntity) clone(audit);
		return copy;
	}
}
