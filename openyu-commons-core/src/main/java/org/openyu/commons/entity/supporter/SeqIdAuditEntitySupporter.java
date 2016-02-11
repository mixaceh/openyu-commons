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
import org.openyu.commons.entity.IdEntity;
import org.openyu.commons.entity.SeqIdAuditEntity;
import org.openyu.commons.entity.bridge.AuditEntityBridge;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqIdAuditEntitySupporter extends SeqEntitySupporter implements
		SeqIdAuditEntity, Supporter
{

	private static final long serialVersionUID = -6426960474200705837L;

	private IdEntity id = new IdEntitySupporter();

	private AuditEntity audit = new AuditEntitySupporter();

	public SeqIdAuditEntitySupporter()
	{}

	@Column(name = "id", length = 255, unique = true)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getId()
	{
		return id.getId();
	}

	public void setId(String id)
	{
		this.id.setId(id);
	}

	@Type(type = "org.openyu.commons.entity.usertype.AuditEntityUserType")
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
		builder.append("id", getId());
		append(builder, audit);
		return builder.toString();
	}

	public Object clone()
	{
		SeqIdAuditEntitySupporter copy = null;
		copy = (SeqIdAuditEntitySupporter) super.clone();
		copy.id = clone(id);
		copy.audit = clone(audit);
		return copy;
	}
}
