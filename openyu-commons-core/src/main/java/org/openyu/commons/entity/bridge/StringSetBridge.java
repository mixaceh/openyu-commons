package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.usertype.StringSetUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class StringSetBridge extends BaseStringBridgeSupporter {

	private StringSetUserType userType = new StringSetUserType();

	public StringSetBridge() {

	}

	public String objectToString(Object value) {
		return userType.marshal(value, null);
	}
}
