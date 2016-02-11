package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.usertype.StringStringUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class StringStringBridge extends BaseStringBridgeSupporter {

	private StringStringUserType userType = new StringStringUserType();

	public StringStringBridge() {

	}

	public String objectToString(Object value) {
		return userType.marshal(value, null);
	}
}
