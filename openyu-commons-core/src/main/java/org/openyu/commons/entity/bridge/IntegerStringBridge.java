package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.useraype.IntegerStringUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class IntegerStringBridge extends BaseStringBridgeSupporter
{

	private IntegerStringUserType userType = new IntegerStringUserType();

	public IntegerStringBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
