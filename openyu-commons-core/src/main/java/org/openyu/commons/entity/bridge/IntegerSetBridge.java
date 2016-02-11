package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.useraype.IntegerSetUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class IntegerSetBridge extends BaseStringBridgeSupporter
{

	private IntegerSetUserType userType = new IntegerSetUserType();

	public IntegerSetBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
