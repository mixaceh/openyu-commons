package org.openyu.commons.entity.supporter;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class AuditEntitySupporter extends BaseEntitySupporter implements AuditEntity, Supporter {

	private static final long serialVersionUID = 7611438859678437079L;

	private Date createDate;

	private String createUser;

	private Date modifiedDate;

	private String modifiedUser;

	public AuditEntitySupporter() {
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "create_user", length = 20)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "modified_date")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "modified_user", length = 20)
	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("createDate", createDate);
		builder.append("createUser", createUser);
		builder.append("modifiedDate", modifiedDate);
		builder.append("modifiedUser", modifiedUser);
		return builder.toString();
	}

	public Object clone() {
		AuditEntitySupporter copy = null;
		copy = (AuditEntitySupporter) super.clone();
		copy.createDate = clone(createDate);
		copy.modifiedDate = clone(modifiedDate);
		return copy;
	}
}
