package org.openyu.commons.entity;

import org.openyu.commons.entity.aware.AuditEntityAware;

public interface SeqIdAuditEntity extends SeqEntity, IdEntity, AuditEntityAware
{

	String KEY = SeqIdAuditEntity.class.getName();

}
