package org.openyu.commons.dog.po.impl;

import java.util.LinkedHashSet;
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
import javax.persistence.Version;
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
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.entity.bridge.AuditEntityBridge;
import org.openyu.commons.entity.bridge.NamesEntityBridge;
import org.openyu.commons.entity.supporter.AuditEntitySupporter;
import org.openyu.commons.entity.supporter.BaseEntitySupporter;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;

/**
 * 測試物件
 */
// --------------------------------------------------
// hibernate
// --------------------------------------------------
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "test_dog")
@SequenceGenerator(name = "test_dog_g", sequenceName = "test_dog_s", allocationSize = 1)
// when use ehcache, config in ehcache.xml
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "org.openyu.commons.dog.po.impl.DogPoImpl")
@Proxy(lazy = false)
@org.hibernate.annotations.Table(appliesTo = "test_dog", indexes = {
		@org.hibernate.annotations.Index(name = "idx_test_dog_valid_id", columnNames = { "valid", "id" }) })
// --------------------------------------------------
// hibernate search
// --------------------------------------------------
@Indexed
public class DogPoImpl extends BaseEntitySupporter implements SeqEntity, NamesEntity {

	private static final long serialVersionUID = 1056890795356260317L;

	private Long seq;

	private Integer version;

	private String id;

	private Boolean valid;

	private Set<LocaleNameEntity> names = new LinkedHashSet<LocaleNameEntity>();// 原來的方式

	private AuditEntity audit = new AuditEntitySupporter();

	public DogPoImpl() {

	}

	@Id
	@Column(name = "seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "test_dog_g")
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	@Version
	@Column(name = "version")
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "id", length = 30, unique = true)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "valid")
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	@Type(type = "org.openyu.commons.entity.userType.NamesEntityUserType")
	@Column(name = "names", length = 2048)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = NamesEntityBridge.class)
	public Set<LocaleNameEntity> getNames() {
		return names;
	}

	public void setNames(Set<LocaleNameEntity> names) {
		this.names = names;
	}

	/**
	 * 
	 * 用locale作為equlas判斷條件,若有相同locale則不加入
	 * 
	 * @param locale
	 * @param name
	 * @return
	 */
	public boolean addName(Locale locale, String name) {
		boolean result = false;
		if (names != null) {
			LocaleNameEntity localeNameEntity = new LocaleNameEntitySupporter();
			localeNameEntity.setLocale(locale);
			localeNameEntity.setName(name);
			result = names.add(localeNameEntity);
		}
		return result;
	}

	public LocaleNameEntity getNameEntry(Locale locale) {
		LocaleNameEntity result = null;
		if (names != null) {
			for (LocaleNameEntity localeNameEntity : names) {
				if (localeNameEntity.getLocale().equals(locale)) {
					result = localeNameEntity;
					break;
				}
			}
		}
		return result;
	}

	public String getName(Locale locale) {
		String result = null;
		LocaleNameEntity localeNameEntity = getNameEntry(locale);
		if (localeNameEntity != null) {
			result = localeNameEntity.getName();
		}
		return result;
	}

	public void setName(Locale locale, String name) {
		LocaleNameEntity localeNameEntity = getNameEntry(locale);
		if (localeNameEntity != null) {
			localeNameEntity.setName(name);
		} else {
			addName(locale, name);
		}
	}

	public boolean removeName(Locale locale) {
		boolean result = false;
		if (names != null) {
			LocaleNameEntity localeNameEntity = getNameEntry(locale);
			if (localeNameEntity != null) {
				result = names.remove(localeNameEntity);
			}
		}
		return result;
	}

	@Type(type = "org.openyu.commons.entity.userType.AuditEntityUserType")
	@Column(name = "audit", length = 570)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = AuditEntityBridge.class)
	public AuditEntity getAudit() {
		return audit;
	}

	public void setAudit(AuditEntity audit) {
		this.audit = audit;
	}

	@Transient
	public String getName() {
		return getName(Locale.getDefault());
	}

	public void setName(String name) {
		setName(Locale.getDefault(), name);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
