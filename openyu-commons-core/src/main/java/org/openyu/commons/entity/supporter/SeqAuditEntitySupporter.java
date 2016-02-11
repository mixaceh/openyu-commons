package org.openyu.commons.entity.supporter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.SeqAuditEntity;
import org.openyu.commons.entity.bridge.AuditEntityBridge;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqAuditEntitySupporter extends SeqEntitySupporter implements SeqAuditEntity,
		Supporter
{

	private static final long serialVersionUID = -7987114510751586517L;

	private AuditEntity audit = new AuditEntitySupporter();

	public SeqAuditEntitySupporter()
	{}

	@Type(type = "org.openyu.commons.entity.userType.AuditEntityUserType")
	@Column(name = "audit", length = 570)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = AuditEntityBridge.class)
	public AuditEntity getAudit()
	{
		return audit;
	}

	public void setAudit(AuditEntity audit)
	{
		this.audit = audit;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("audit", audit);
		return builder.toString();
	}

	public Object clone()
	{
		SeqAuditEntitySupporter copy = null;
		copy = (SeqAuditEntitySupporter) super.clone();
		copy.audit = clone(audit);
		return copy;
	}
}
