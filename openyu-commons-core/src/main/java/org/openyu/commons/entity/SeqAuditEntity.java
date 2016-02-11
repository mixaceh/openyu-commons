package org.openyu.commons.entity;

import org.openyu.commons.entity.aware.AuditEntityAware;

public interface SeqAuditEntity extends SeqEntity, AuditEntityAware
{

	String KEY = SeqAuditEntity.class.getName();

}
