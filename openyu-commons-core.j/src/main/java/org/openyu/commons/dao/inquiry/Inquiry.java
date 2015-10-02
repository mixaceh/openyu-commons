package org.openyu.commons.dao.inquiry;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.model.BaseModel;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 查詢條件
 * 
 * 1.page 分頁條件
 * 
 * 2.sort 排序條件
 * 
 * 3.order 排序方向
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Inquiry extends BaseModel
{

	/**
	 * 分頁條件
	 * 
	 * @return
	 */
	Pagination getPagination();

	void setPagination(Pagination pagination);

	/**
	 * 目前排序欄位
	 * 
	 * @return
	 */
	Sort getSort();

	void setSort(Sort sort);

	/**
	 * 排序欄位選項
	 * 
	 * @return
	 */
	List<Sort> getSorts();

	void setSorts(List<Sort> sorts);

	/**
	 * 目前排序方向
	 * 
	 * @return
	 */
	Order getOrder();

	void setOrder(Order order);

	/**
	 * 排序方向選項
	 * 
	 * @return
	 */
	List<Order> getOrders();

	void setOrders(List<Order> orders);

	/**
	 * 是否輸出
	 * 
	 * @return
	 */
	boolean isExport();

	void setExport(boolean export);

}