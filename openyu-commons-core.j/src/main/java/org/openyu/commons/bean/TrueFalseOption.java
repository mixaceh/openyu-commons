package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.enumz.BooleanEnum;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 是否(true/false)選項
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface TrueFalseOption extends BaseBean, NamesBean
{

	/**
	 * 是否類別
	 */
	public enum TrueFalseType implements BooleanEnum
	{
		/**
		 * 否
		 */
		FALSE(false),

		/**
		 * 是
		 */
		TRUE(true),

		//
		;
		private final boolean value;

		private TrueFalseType(boolean value)
		{
			this.value = value;
		}

		public boolean getValue()
		{
			return value;
		}
	}

	/**
	 * 是否類別,key
	 * 
	 * @return
	 */
	TrueFalseType getId();

	void setId(TrueFalseType id);

}
