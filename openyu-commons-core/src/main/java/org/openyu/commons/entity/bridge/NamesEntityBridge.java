package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.usertype.NamesEntityUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class NamesEntityBridge extends BaseStringBridgeSupporter
{

	private NamesEntityUserType userType = new NamesEntityUserType();

	public NamesEntityBridge()
	{

	}

	public String objectToString(Object value)
	{
		return userType.marshal(value,null);
	}
}
