package org.openyu.commons.bean.supporter;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.bean.IdBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.WeightBean;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.lang.event.EventCaster;
import org.openyu.commons.lang.event.EventHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.service.event.BeanChangeEvent;
import org.openyu.commons.service.event.BeanChangeListener;
import org.openyu.commons.util.DateHelper;

public class BaseBeanSupporter extends BaseModelSupporter implements BaseBean, Supporter
{

	private static final long serialVersionUID = -194139978071831141L;

	private transient EventCaster beanChangeListeners;

	public BaseBeanSupporter()
	{}

	public void addBeanChangeListener(BeanChangeListener beanChangeListener)
	{
		beanChangeListeners = EventCaster.add(beanChangeListeners, beanChangeListener);
	}

	public void removeBeanChangeListener(BeanChangeListener beanChangeListener)
	{
		beanChangeListeners = EventCaster.remove(beanChangeListeners, beanChangeListener);
	}

	public BeanChangeListener[] getBeanChangeListeners()
	{
		BeanChangeListener[] result = null;
		if (beanChangeListeners != null)
		{
			result = (BeanChangeListener[]) beanChangeListeners
					.getListeners(BeanChangeListener.class);
		}
		return result;
	}

	//1.source: 指的是BaseBean
	//2.fieldName:指的是欲觸發的欄位名稱
	//3.attach: 指的是EventAttach,觸發事件時,傳入改變的新舊值或其它可自定義的值
	protected void fireBeanChangeEvent(Object source, String fieldName, int type, Object attach)
	{
		if (beanChangeListeners != null)
		{
			BeanChangeEvent beanChangeEvent = new BeanChangeEvent(source, fieldName, type, attach);
			beanChangeListeners.dispatch(beanChangeEvent);
		}
	}

	protected void fireBeanChanging(Object source, String fieldName)
	{
		fireBeanChanging(source, fieldName, null);
	}

	protected void fireBeanChanging(Object source, String fieldName, Object attach)
	{
		fireBeanChangeEvent(source, fieldName, BeanChangeEvent.CHANGING, attach);
	}

	protected void fireBeanChanged(Object source, String fieldName)
	{
		fireBeanChanged(source, fieldName, null);
	}

	protected void fireBeanChanged(Object source, String fieldName, Object attach)
	{
		fireBeanChangeEvent(source, fieldName, BeanChangeEvent.CHANGED, attach);
	}

	// --------------------------------------------------
	protected void fireBeanChangeEvent(Object source, Enum<?> fieldEnum, int type, Object attach)
	{
		if (beanChangeListeners != null)
		{
			BeanChangeEvent beanChangeEvent = new BeanChangeEvent(source, fieldEnum, type, attach);
			beanChangeListeners.dispatch(beanChangeEvent);
		}
	}

	protected void fireBeanChanging(Object source, Enum<?> fieldEnum)
	{
		fireBeanChanging(source, fieldEnum, null);
	}

	protected void fireBeanChanging(Object source, Enum<?> fieldEnum, Object attach)
	{
		fireBeanChangeEvent(source, fieldEnum, BeanChangeEvent.CHANGING, attach);
	}

	protected void fireBeanChanged(Object source, Enum<?> fieldEnum)
	{
		fireBeanChanged(source, fieldEnum, null);
	}

	protected void fireBeanChanged(Object source, Enum<?> fieldEnum, Object attach)
	{
		fireBeanChangeEvent(source, fieldEnum, BeanChangeEvent.CHANGED, attach);
	}

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------
	/**
	 * 事件附件
	 * 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	protected <OLD_VALUE, NEW_VALUE> EventAttach<OLD_VALUE, NEW_VALUE> eventAttach(OLD_VALUE oldValue,
																				NEW_VALUE newValue)
	{
		return EventHelper.eventAttach(oldValue, newValue);
	}

	// ----------------------------------------------------------------
	public String toString()
	{
		return "";
	}

	protected void append(ToStringBuilder builder, IdBean idBean)
	{
		if (idBean != null)
		{
			builder.append("id", idBean.getId());
			builder.append("dataId", idBean.getDataId());
		}
		else
		{
			builder.append("id", (Object) null);
			builder.append("dataId", (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, String fieldName, NamesBean namesBean)
	{
		if (namesBean != null)
		{
			append(builder, fieldName, namesBean.getNames());
		}
		else
		{
			builder.append(fieldName, (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, String fieldName, Set<LocaleNameBean> names)
	{
		if (names != null)
		{
			Map<Locale, String> buff = new LinkedHashMap<Locale, String>();
			for (LocaleNameBean entry : names)
			{
				buff.put(entry.getLocale(), entry.getName());
			}
			builder.append(fieldName, buff);
		}
		else
		{
			builder.append(fieldName, (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, LocaleNameBean localeNameBean)
	{
		if (localeNameBean != null)
		{
			builder.append("locale", localeNameBean.getLocale());
			builder.append("name", localeNameBean.getName());
		}
		else
		{
			builder.append("locale", (Object) null);
			builder.append("name", (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, AuditBean auditBean)
	{
		if (auditBean != null)
		{
			builder.append("createDate", DateHelper.toString(auditBean.getCreateDate()));
			builder.append("createUser", auditBean.getCreateUser());
			builder.append("modifiedDate", DateHelper.toString(auditBean.getModifiedDate()));
			builder.append("modifiedUser", auditBean.getModifiedUser());
		}
		else
		{
			builder.append("createDate", (Object) null);
			builder.append("createUser", (Object) null);
			builder.append("modifiedDate", (Object) null);
			builder.append("modifiedUser", (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, WeightBean weightBean)
	{
		if (weightBean != null)
		{
			builder.append("probability", weightBean.getProbability());
			builder.append("weight", weightBean.getWeight());
		}
		else
		{
			builder.append("probability", 0d);
			builder.append("weight", 0);
		}
	}

}
