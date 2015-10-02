package org.openyu.commons.dao.inquiry.impl;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.model.supporter.BaseModelSupporter;

/**
 * 分頁條件
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "pagination")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaginationImpl extends BaseModelSupporter implements Pagination
{

	private static final long serialVersionUID = 7334670915999011413L;

	/**
	 * 目前頁數,第?頁
	 */
	@XmlTransient
	private int pageIndex = DEFAULT_PAGE_INDEX;

	/**
	 * 每頁筆數
	 */
	@XmlTransient
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 每頁筆數選項
	 */
	private List<Integer> pageSizes = new LinkedList<Integer>();

	/**
	 * 總計筆數
	 */
	@XmlTransient
	private int rowCount;

	/**
	 * 搜尋時間,秒數
	 */
	@XmlTransient
	private double processTime;

	/**
	 * 導覽頁數
	 */
	@XmlTransient
	private int navSize = DEFAULT_NAV_SIZE;

	public PaginationImpl()
	{}

	public int getPageIndex()
	{
		if (pageIndex == 0)
		{
			return DEFAULT_PAGE_INDEX;
		}
		if (pageIndex < 0)
		{
			return DEFAULT_PAGE_INDEX;
		}
		return pageIndex;
	}

	public void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public int getPageSize()
	{
		if (pageSize == 0)
		{
			return DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public List<Integer> getPageSizes()
	{
		return pageSizes;
	}

	public void setPageSizes(List<Integer> pageSizes)
	{
		this.pageSizes = pageSizes;
	}

	public int getPageCount()
	{
		if (rowCount != 0)
		{
			return (int) Math.ceil((double) rowCount / getMaxResults());

			// if (rowCount % getMaxResults() == 0) {
			// return rowCount / getMaxResults();
			// } else {
			// return (rowCount / getMaxResults()) + 1;
			// }
		}
		return DEFAULT_PAGE_COUNT;
	}

	// 內部從0開始算
	// reverseIndex= rowCount - getFirstResult()
	public int getFirstResult()
	{
		return getPageIndex() * getMaxResults();
	}

	public int getMaxResults()
	{
		return getPageSize();
	}

	public int getRowCount()
	{
		return rowCount;
	}

	public void setRowCount(int rowCount)
	{
		this.rowCount = rowCount;
	}

	public double getProcessTime()
	{
		return processTime;
	}

	public void setProcessTime(double processTime)
	{
		this.processTime = processTime;
	}

	public int getNavIndex()
	{
		return getPageIndex() / getNavSize();
	}

	public int getNavSize()
	{
		if (navSize != 0)
		{
			return navSize;
		}
		return DEFAULT_NAV_SIZE;
	}

	public void setNavSize(int navSize)
	{
		this.navSize = navSize;
	}

	public int getNavCount()
	{
		return getPageCount() / getNavSize();
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("pageSizes", pageSizes);
		builder.append("rowCount", rowCount);
		builder.append("pageSize", getPageSize());
		builder.append("pageCount", getPageCount());
		builder.append("pageIndex", pageIndex);
		builder.append("firstResult", getFirstResult());
		builder.append("processTime", processTime);
		builder.append("navSize", navSize);
		builder.append("navCount", getNavCount());
		builder.append("navIndex", getNavIndex());
		return builder.toString();
	}

	public Object clone()
	{
		PaginationImpl copy = null;
		copy = (PaginationImpl) super.clone();
		copy.pageSizes = clone(pageSizes);
		return copy;
	}
}
