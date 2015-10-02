package org.openyu.commons.entity;

import org.openyu.commons.entity.aware.AuditEntityAware;

public interface SeqIdAuditNamesEntity extends SeqEntity, IdEntity, AuditEntityAware, NamesEntity
{

	String KEY = SeqIdAuditNamesEntity.class.getName();

}
