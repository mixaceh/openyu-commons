package org.openyu.commons.dao.inquiry.impl;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.model.supporter.BaseModelSupporter;

/**
 * 查詢條件
 * 
 * 1.pagination 分頁條件
 * 
 * 2.sort 排序欄位
 * 
 * 3.order 排序方向
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "inquiry")
@XmlAccessorType(XmlAccessType.FIELD)
public class InquiryImpl extends BaseModelSupporter implements Inquiry
{

	private static final long serialVersionUID = -3715904912097814319L;

	/**
	 * 分頁條件
	 */
	@XmlElement(type = PaginationImpl.class)
	private Pagination pagination;

	/**
	 * 目前排序欄位
	 */
	@XmlTransient
	private Sort sort;

	/**
	 * 排序欄位選項
	 */
	@XmlElement(type = SortImpl.class)
	private List<Sort> sorts = new LinkedList<Sort>();

	/**
	 * 目前排序方向
	 */
	@XmlTransient
	private Order order;

	/**
	 * 排序方向選項
	 */
	@XmlElement(type = OrderImpl.class)
	private List<Order> orders = new LinkedList<Order>();

	/**
	 * 是否輸出
	 */
	private boolean export = false;

	public InquiryImpl()
	{

	}

	public Pagination getPagination()
	{
		return pagination;
	}

	public void setPagination(Pagination pagination)
	{
		this.pagination = pagination;
	}

	public Sort getSort()
	{
		return sort;
	}

	public void setSort(Sort sort)
	{
		this.sort = sort;
	}

	public List<Sort> getSorts()
	{
		return sorts;
	}

	public void setSorts(List<Sort> sorts)
	{
		this.sorts = sorts;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public List<Order> getOrders()
	{
		return orders;
	}

	public void setOrders(List<Order> orders)
	{
		this.orders = orders;
	}

	public boolean isExport()
	{
		return export;
	}

	public void setExport(boolean export)
	{
		this.export = export;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("pagination", pagination);
		builder.append("sort", sort);
		builder.append("sorts", sorts);
		builder.append("order", order);
		builder.append("orders", orders);
		builder.append("export", export);
		return builder.toString();
	}

	public Object clone()
	{
		InquiryImpl copy = null;
		copy = (InquiryImpl) super.clone();
		copy.pagination = clone(pagination);
		copy.sort = clone(sort);
		copy.sorts = clone(sorts);
		copy.order = clone(order);
		copy.orders = clone(orders);
		return copy;
	}
}
