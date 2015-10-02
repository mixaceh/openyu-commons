package org.openyu.commons.dao.inquiry.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.dao.inquiry.Order;

/**
 * 排序方向
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderImpl extends NamesBeanSupporter implements Order
{

	private static final long serialVersionUID = 8304265723935416859L;

	/**
	 * 排序方向類別,key
	 */
	private OrderType id;

	public OrderImpl(OrderType id)
	{
		this.id = id;
	}

	public OrderImpl()
	{
		this(null);
	}

	public OrderType getId()
	{
		return id;
	}

	public void setId(OrderType id)
	{
		this.id = id;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof OrderImpl))
		{
			return false;
		}
		if (this == object)
		{
			return true;
		}
		OrderImpl other = (OrderImpl) object;
		if (id == null || other.id == null)
		{
			return false;
		}
		return new EqualsBuilder().append(id, other.id).isEquals();
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
		OrderImpl copy = null;
		copy = (OrderImpl) super.clone();
		return copy;
	}

}
