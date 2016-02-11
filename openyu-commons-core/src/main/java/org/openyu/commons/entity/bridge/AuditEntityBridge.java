package org.openyu.commons.entity.bridge;

import org.openyu.commons.entity.useraype.AuditEntityUserType;
import org.openyu.commons.hibernate.search.bridge.supporter.BaseStringBridgeSupporter;

//--------------------------------------------------
//reslove: Hibernate search
//--------------------------------------------------
public class AuditEntityBridge extends BaseStringBridgeSupporter {

	private AuditEntityUserType userType = new AuditEntityUserType();

	public AuditEntityBridge() {

	}

	public String objectToString(Object value) {
		return userType.marshal(value, null);
	}
}
