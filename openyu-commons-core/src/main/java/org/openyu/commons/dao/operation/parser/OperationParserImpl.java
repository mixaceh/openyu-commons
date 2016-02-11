package org.openyu.commons.dao.operation.parser;

import org.openyu.commons.dao.operation.Operation;

public class OperationParserImpl implements OperationParser {

	private Operation operation;

	public OperationParserImpl(Operation operation) {
		setOperation(operation);
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public void parse() {
		if (getOperation() == null) {
			throw new NullPointerException("operation is null");
		}
		// --------------------------------------------------
		boolean enable = !(getOperation().isInserted()
				|| getOperation().isInsertedDetail() || getOperation()
				.isDeletedDetail());

		// insert
		getOperation().setInsertEnable(enable);
		// delete
		getOperation().setDeleteEnable(enable);
		// refresh
		getOperation().setRefreshEnable(enable);
		// list
		getOperation().setListEnable(enable);
		// ------------------------------------------------------------------
		// sort
		getOperation().setSortEnable(enable);
	}
}
