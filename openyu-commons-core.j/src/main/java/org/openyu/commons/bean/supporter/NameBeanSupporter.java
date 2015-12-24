package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.bean.NameBean;

@XmlRootElement(name = "nameBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class NameBeanSupporter extends BaseBeanSupporter implements NameBean {

	private static final long serialVersionUID = 136603947206846568L;

	private String name;

	public NameBeanSupporter() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("name", name);
		return builder.toString();
	}

	public Object clone() {
		NameBeanSupporter copy = null;
		copy = (NameBeanSupporter) super.clone();
		return copy;
	}
}
