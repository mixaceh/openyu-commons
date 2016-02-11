package org.openyu.commons.entity;

public interface SeqEntity extends BaseEntity
{

	String KEY = SeqEntity.class.getName();

	/**
	 * DB序號
	 * 
	 * @return
	 */
	Long getSeq();

	void setSeq(Long seq);

	/**
	 * 版本號,樂觀鎖
	 * 
	 * @return
	 */
	Integer getVersion();

	void setVersion(Integer version);
}
