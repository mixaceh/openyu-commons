package org.openyu.commons.dao.inquiry;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.enumz.StringEnum;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 排序方向
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Order extends BaseBean, NamesBean
{

	/**
	 * 排序方向類別
	 */
	public enum OrderType implements StringEnum
	{
		/**
		 * 自然
		 */
		NATURAL("natural"),

		/**
		 * 升冪
		 */
		ASC("asc"),

		/**
		 * 降冪
		 */
		DESC("desc"),

		//
		;
		private final String value;

		private OrderType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

	/**
	 * 排序方向類別,key
	 * 
	 * @return
	 */
	OrderType getId();

	void setId(OrderType id);

}
