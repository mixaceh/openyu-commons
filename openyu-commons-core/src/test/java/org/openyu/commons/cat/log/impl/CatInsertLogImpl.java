package org.openyu.commons.cat.log.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.openyu.commons.cat.log.CatInsertLog;
import org.openyu.commons.entity.supporter.SeqLogEntitySupporter;

//--------------------------------------------------
//hibernate
//--------------------------------------------------
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "cat_insert_log")
@SequenceGenerator(name = "cat_insert_log_g", sequenceName = "cat_insert_log_s", allocationSize = 1)
// when use ehcache, config in ehcache.xml
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "org.openyu.commons.cat.log.impl.CatInsertLogImpl")
@Proxy(lazy = false)
@org.hibernate.annotations.Table(appliesTo = "cat_insert_log", indexes = {
		@org.hibernate.annotations.Index(name = "idx_cat_insert_log_1", columnNames = { "cat_id", "log_date" }) })
// --------------------------------------------------
// search
// --------------------------------------------------
// @Indexed
public class CatInsertLogImpl extends SeqLogEntitySupporter implements CatInsertLog {

	private static final long serialVersionUID = 7994838313811358292L;

	private Long seq;

	/**
	 * 貓id
	 */
	private String catId;

	public CatInsertLogImpl() {
	}

	@Id
	@Column(name = "seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "cat_insert_log_g")
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	@Column(name = "cat_id", length = 30)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		return builder.toString();
	}

	public Object clone() {
		CatInsertLogImpl copy = null;
		copy = (CatInsertLogImpl) super.clone();
		return copy;
	}
}
