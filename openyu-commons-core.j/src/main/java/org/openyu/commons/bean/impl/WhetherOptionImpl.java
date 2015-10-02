package org.openyu.commons.bean.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.WhetherOption;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;

/**
 * 全部是否("all"/"true"/"false")選項
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "whetherOption")
@XmlAccessorType(XmlAccessType.FIELD)
public class WhetherOptionImpl extends NamesBeanSupporter implements WhetherOption
{

	private static final long serialVersionUID = 4644282406281853593L;

	/**
	 * 是否類別,key
	 */
	private WhetherType id;

	public WhetherOptionImpl(WhetherType id)
	{
		this.id = id;
	}

	public WhetherOptionImpl()
	{
		this(null);
	}

	public WhetherType getId()
	{
		return id;
	}

	public void setId(WhetherType id)
	{
		this.id = id;
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.appendSuper(super.toString());
		return builder.toString();
	}

	public Object clone()
	{
		WhetherOptionImpl copy = null;
		copy = (WhetherOptionImpl) super.clone();
		return copy;
	}

}
