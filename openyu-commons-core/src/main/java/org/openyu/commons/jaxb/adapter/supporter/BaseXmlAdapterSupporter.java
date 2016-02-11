package org.openyu.commons.jaxb.adapter.supporter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.openyu.commons.jaxb.adapter.BaseXmlAdapter;
import org.openyu.commons.mark.Supporter;

/**
 * xml轉接器
 * 
 * object <-> xml
 */
public abstract class BaseXmlAdapterSupporter<ValueType, BoundType> extends
		XmlAdapter<ValueType, BoundType> implements BaseXmlAdapter<ValueType, BoundType>, Supporter
{

	public BaseXmlAdapterSupporter()
	{}

}
