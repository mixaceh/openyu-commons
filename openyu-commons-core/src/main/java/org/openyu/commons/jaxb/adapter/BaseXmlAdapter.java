package org.openyu.commons.jaxb.adapter;

/**
 * xml轉接器
 * 
 * object <-> xml
 */
public interface BaseXmlAdapter<ValueType, BoundType>
{
	BoundType unmarshal(ValueType value) throws Exception;

	ValueType marshal(BoundType value) throws Exception;
}
