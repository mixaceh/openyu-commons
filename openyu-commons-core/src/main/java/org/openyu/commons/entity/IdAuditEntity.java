package org.openyu.commons.entity;

@Deprecated
/**
 * 因合併多個欄位成一個欄位,故不用再用此class,處理多個欄位 
 */
public interface IdAuditEntity extends IdEntity, AuditEntity
{

	String KEY = IdAuditEntity.class.getName();

}
