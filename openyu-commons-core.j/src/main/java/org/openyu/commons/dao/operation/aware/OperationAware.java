package org.openyu.commons.dao.operation.aware;

import org.openyu.commons.dao.operation.Operation;

public interface OperationAware {

	String KEY = OperationAware.class.getName();

	void setOperation(Operation operation);

	Operation getOperation();
}
