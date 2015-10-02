package org.openyu.commons.dao.inquiry.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Sort;

/**
 * 排序欄位
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "sort")
@XmlAccessorType(XmlAccessType.FIELD)
public class SortImpl extends NamesBeanSupporter implements Sort
{

	private static final long serialVersionUID = -2197892175543590082L;

	/**
	 * 排序欄位
	 */
	private String id;

	/**
	 * 排序方向
	 */
	@XmlElement(type = OrderImpl.class)
	private Order order;

	public SortImpl()
	{
		this.order = new OrderImpl();
	}

	public SortImpl(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof SortImpl))
		{
			return false;
		}
		if (this == object)
		{
			return true;
		}
		SortImpl other = (SortImpl) object;
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
		builder.append("order", order);
		return builder.toString();
	}

	public Object clone()
	{
		SortImpl copy = null;
		copy = (SortImpl) super.clone();
		copy.order = clone(order);
		return copy;
	}

}
