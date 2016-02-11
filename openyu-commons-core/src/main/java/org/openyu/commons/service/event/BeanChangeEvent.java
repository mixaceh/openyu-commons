package org.openyu.commons.service.event;

import java.util.EventListener;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.lang.event.EventDispatchable;
import org.openyu.commons.lang.event.supporter.BaseEventSupporter;

/**
 * bean改變事件
 */
public class BeanChangeEvent extends BaseEventSupporter implements EventDispatchable
{

	private static final long serialVersionUID = -9110009155013108646L;

	/**
	 * 改變前
	 */
	public static final int CHANGING = 0;

	/**
	 * 改變後
	 */
	public static final int CHANGED = 1;

	private transient String fieldName;

		private transient Enum<?> fieldEnum;

	/**
	 * 用String當作欲觸發field的識別
	 * 
	 * @param source
	 * @param fieldName
	 * @param type
	 * @param attach
	 */
	public BeanChangeEvent(Object source, String fieldName, int type, Object attach)
	{
		super(source, type, attach);
		this.fieldName = fieldName;
	}

	public BeanChangeEvent(Object source, String fieldName, int type)
	{
		this(source, fieldName, type, null);
	}

		/**
		 * 用Enum當作欲觸發field的識別
		 * 
		 * 2012/02/23
		 * 
		 * @param source
		 * @param fieldName
		 * @param type
		 * @param attach
		 */
		public BeanChangeEvent(Object source, Enum<?> fieldEnum, int type, Object attach)
		{
			super(source, type, attach);
			this.fieldEnum = fieldEnum;
		}
	
		public BeanChangeEvent(Object source, Enum<?> fieldEnum, int type)
		{
			this(source, fieldEnum, type, null);
		}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public Enum<?> getFieldEnum()
	{
		return fieldEnum;
	}

	public void setFieldEnum(Enum<?> fieldEnum)
	{
		this.fieldEnum = fieldEnum;
	}

	public void dispatch(EventListener listener)
	{
		switch (getType())
		{
			case CHANGING:
				((BeanChangeListener) listener).beanChanging(this);
				break;
			case CHANGED:
				((BeanChangeListener) listener).beanChanged(this);
				break;
		}
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("fieldName", fieldName);
		//		builder.append("fieldEnum", fieldEnum);
		return builder.toString();
	}
}
