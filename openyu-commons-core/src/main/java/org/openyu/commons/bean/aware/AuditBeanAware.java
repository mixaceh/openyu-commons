package org.openyu.commons.bean.aware;

import org.openyu.commons.bean.AuditBean;

public interface AuditBeanAware
{

	AuditBean getAudit();

	void setAudit(AuditBean audit);
}
