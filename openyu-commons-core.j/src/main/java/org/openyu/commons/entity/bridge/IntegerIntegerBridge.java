package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.userType.IntegerIntegerUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class IntegerIntegerBridge extends BaseStringBridgeSupporter
{

	private IntegerIntegerUserType userType = new IntegerIntegerUserType();

	public IntegerIntegerBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
