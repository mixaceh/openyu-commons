package org.openyu.commons.cassandra.thrift.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CtSessionException extends BaseRuntimeException {

	private static final long serialVersionUID = 7869257612680029835L;

	public CtSessionException(String message) {
		super(message);
	}
}
