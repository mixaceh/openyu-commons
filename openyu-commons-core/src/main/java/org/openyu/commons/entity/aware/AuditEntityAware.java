package org.openyu.commons.entity.aware;

import org.openyu.commons.entity.AuditEntity;

public interface AuditEntityAware
{

	AuditEntity getAudit();

	void setAudit(AuditEntity audit);
}
