package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.usertype.IntegerListUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class IntegerListBridge extends BaseStringBridgeSupporter
{

	private IntegerListUserType userType = new IntegerListUserType();

	public IntegerListBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
