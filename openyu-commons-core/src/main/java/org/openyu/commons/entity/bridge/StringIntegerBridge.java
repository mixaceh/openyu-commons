package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.usertype.StringIntegerUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class StringIntegerBridge extends BaseStringBridgeSupporter
{

	private StringIntegerUserType userType = new StringIntegerUserType();

	public StringIntegerBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
