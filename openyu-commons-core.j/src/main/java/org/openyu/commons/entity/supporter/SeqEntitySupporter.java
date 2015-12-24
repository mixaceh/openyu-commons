package org.openyu.commons.entity.supporter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqEntitySupporter extends BaseEntitySupporter implements SeqEntity, Supporter {

	private static final long serialVersionUID = -4288500948838027154L;

	// 2011/10/14 改由實作類別實現,不在這處理了,因會有exception
	// private Long seq;

	private Integer version;

	public SeqEntitySupporter() {
	}

	// @Id
	// @Column(name = "seq")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// @GeneratedValue(strategy = GenerationType.AUTO, generator = "xxx_g")
	// public Long getSeq() {
	// return seq;
	// }
	//
	// public void setSeq(Long seq) {
	// this.seq = seq;
	// }

	@Version
	@Column(name = "version")
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SeqEntitySupporter)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		SeqEntitySupporter other = (SeqEntitySupporter) object;
		if (getSeq() == null || other.getSeq() == null) {
			return false;
		}
		return new EqualsBuilder().append(getSeq(), other.getSeq()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSeq()).toHashCode();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("seq", getSeq());
		builder.append("version", version);
		return builder.toString();
	}

	public Object clone() {
		SeqEntitySupporter copy = null;
		copy = (SeqEntitySupporter) super.clone();
		return copy;
	}
}
