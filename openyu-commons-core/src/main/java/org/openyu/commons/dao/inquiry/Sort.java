package org.openyu.commons.dao.inquiry;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.bean.NamesBean;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 排序欄位
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Sort extends BaseBean, NamesBean
{

	/**
	 * 排序欄位id
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

	/**
	 * 排序方向
	 * 
	 * @return
	 */
	Order getOrder();

	void setOrder(Order order);

}