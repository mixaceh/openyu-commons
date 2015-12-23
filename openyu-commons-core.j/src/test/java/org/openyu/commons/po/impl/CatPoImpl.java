package org.openyu.commons.po.impl;

import java.util.Locale;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NamesEntity;
import org.openyu.commons.entity.bridge.AuditEntityBridge;
import org.openyu.commons.entity.bridge.NamesEntityBridge;
import org.openyu.commons.entity.supporter.AuditEntitySupporter;
import org.openyu.commons.entity.supporter.NamesEntitySupporter;
import org.openyu.commons.entity.supporter.SeqEntitySupporter;

/**
 * 測試物件
 */
//--------------------------------------------------
//hibernate
//--------------------------------------------------
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "test_cat")
@SequenceGenerator(name = "test_cat_g", sequenceName = "test_cat_s", allocationSize = 1)
// when use ehcache, config in ehcache.xml
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "org.openyu.commons.po.impl.CatPoImpl")
@Proxy(lazy = false)
@org.hibernate.annotations.Table(appliesTo = "test_cat", indexes = { @org.hibernate.annotations.Index(name = "idx_test_cat_valid_id", columnNames = {
		"valid", "id" }) })
// --------------------------------------------------
// hibernate search
// --------------------------------------------------
@Indexed
public class CatPoImpl extends SeqEntitySupporter implements NamesEntity
{
	private static final long serialVersionUID = -724313847576583704L;

	private Long seq;

	private String id;

	private Boolean valid;

	//#deprecated
	//private Set<LocaleNameEntity> names = new LinkedHashSet<LocaleNameEntity>();

	//#new
	private NamesEntity names = new NamesEntitySupporter();

	private AuditEntity audit = new AuditEntitySupporter();

	public CatPoImpl()
	{

	}

	@Id
	@Column(name = "seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "test_cat_g")
	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	@Column(name = "id", length = 30, unique = true)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Column(name = "valid")
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public Boolean getValid()
	{
		return valid;
	}

	public void setValid(Boolean valid)
	{
		this.valid = valid;
	}

	@Type(type = "org.openyu.commons.entity.userType.NamesEntityUserType")
	@Column(name = "names", length = 2048)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = NamesEntityBridge.class)
	public Set<LocaleNameEntity> getNames()
	{
		return names.getNames();
	}

	public void setNames(Set<LocaleNameEntity> names)
	{
		this.names.setNames(names);
	}

	public boolean addName(Locale locale, String name)
	{
		return names.addName(locale, name);
	}

	public LocaleNameEntity getNameEntry(Locale locale)
	{
		return names.getNameEntry(locale);
	}

	public String getName(Locale locale)
	{
		return names.getName(locale);
	}

	public void setName(Locale locale, String name)
	{
		names.setName(locale, name);
	}

	public boolean removeName(Locale locale)
	{
		return names.removeName(locale);
	}

	@Transient
	public String getName()
	{
		return names.getName();
	}

	public void setName(String name)
	{
		names.setName(name);
	}

	@Type(type = "org.openyu.commons.entity.userType.AuditEntityUserType")
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
		return ToStringBuilder.reflectionToString(this);
	}
}
