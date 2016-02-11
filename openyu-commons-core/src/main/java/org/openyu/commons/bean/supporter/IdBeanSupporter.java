package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.IdBean;

@XmlRootElement(name = "idBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdBeanSupporter extends BaseBeanSupporter implements IdBean {

	private static final long serialVersionUID = -6331913209274884905L;

	/**
	 * id,唯一碼,不可重複,商業邏輯用
	 */
	private String id;

	/**
	 * 資料id,xml用
	 */
	private String dataId;

	private boolean only;

	public IdBeanSupporter() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public boolean isOnly() {
		return only;
	}

	public void setOnly(boolean only) {
		this.only = only;
	}

	public boolean equals(Object object) {
		if (!(object instanceof IdBeanSupporter)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		IdBeanSupporter other = (IdBeanSupporter) object;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("id", id);
		builder.append("dataId", dataId);
		builder.append("only", only);
		return builder.toString();
	}

	public Object clone() {
		IdBeanSupporter copy = null;
		copy = (IdBeanSupporter) super.clone();
		return copy;
	}
}
