package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.bean.SeqAuditBean;

@XmlRootElement(name = "seqAuditBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeqAuditBeanSupporter extends SeqBeanSupporter implements SeqAuditBean
{

	private static final long serialVersionUID = 1402736741139193533L;

	//@XmlElement(type = AuditBeanSupporter.class)
	@XmlTransient
	private AuditBean audit = new AuditBeanSupporter();

	public SeqAuditBeanSupporter()
	{}

	public AuditBean getAudit()
	{
		return audit;
	}

	public void setAudit(AuditBean audit)
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
		SeqAuditBeanSupporter copy = null;
		copy = (SeqAuditBeanSupporter) super.clone();
		copy.audit = clone(audit);
		return copy;
	}
}
