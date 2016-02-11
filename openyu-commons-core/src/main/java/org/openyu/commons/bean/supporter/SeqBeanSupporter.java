package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.SeqBean;

@XmlRootElement(name = "seqBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeqBeanSupporter extends BaseBeanSupporter implements SeqBean {

	private static final long serialVersionUID = -5166374150818025568L;

	@XmlTransient
	private long seq;

	@XmlTransient
	private int version;

	public SeqBeanSupporter() {
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SeqBeanSupporter)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		SeqBeanSupporter other = (SeqBeanSupporter) object;
		return new EqualsBuilder().append(seq, other.seq).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(seq).toHashCode();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("seq", seq);
		builder.append("version", version);
		return builder.toString();
	}

	public Object clone() {
		SeqBeanSupporter copy = null;
		copy = (SeqBeanSupporter) super.clone();
		return copy;
	}
}
