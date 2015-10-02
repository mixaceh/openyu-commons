package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.enumz.StringEnum;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 *  全部是否("all"/"true"/"false")選項
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface WhetherOption extends BaseBean, NamesBean
{

	/**
	 * 全部是否類別
	 */
	public enum WhetherType implements StringEnum
	{
		/**
		 * 所有
		 */
		ALL("all"),

		/**
		 * 否
		 */
		FALSE("false"),

		/**
		 * 是
		 */
		TRUE("true"),

		//
		;
		private final String value;

		private WhetherType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

	/**
	 * 是否類別,key
	 * 
	 * @return
	 */
	WhetherType getId();

	void setId(WhetherType id);

}
