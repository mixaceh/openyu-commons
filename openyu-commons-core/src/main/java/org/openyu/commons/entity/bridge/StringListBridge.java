package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.useraype.StringListUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class StringListBridge extends BaseStringBridgeSupporter {

	private StringListUserType userType = new StringListUserType();

	public StringListBridge() {

	}

	public String objectToString(Object value) {
		return userType.marshal(value, null);
	}
}
