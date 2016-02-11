package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

// --------------------------------------------------
// reslove: JAXB can’t handle interfaces
// --------------------------------------------------
//<orderTypes key="ALL">all</orderTypes>
//<orderTypes key="FALSE">false</orderTypes>
//<orderTypes key="TRUE">true</orderTypes>
//--------------------------------------------------
public class OrderTypeXmlAdapter extends
		BaseXmlAdapterSupporter<OrderTypeXmlAdapter.OrderTypeEntry, OrderType>
{

	public OrderTypeXmlAdapter()
	{}

	// --------------------------------------------------
	public static class OrderTypeEntry
	{
		@XmlAttribute
		public String key;

		@XmlValue
		public String value;

		public OrderTypeEntry(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public OrderTypeEntry()
		{}
	}

	// --------------------------------------------------
	public OrderType unmarshal(OrderTypeEntry value) throws Exception
	{
		OrderType result = null;
		//
		if (value != null)
		{
			result = OrderType.valueOf(value.key);
		}
		return result;
	}

	public OrderTypeEntry marshal(OrderType value) throws Exception
	{
		OrderTypeEntry result = null;
		//
		if (value != null)
		{
			result = new OrderTypeEntry(value.name(), value.getValue());
		}
		return result;
	}
}
