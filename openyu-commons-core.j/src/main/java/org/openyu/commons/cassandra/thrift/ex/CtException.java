package org.openyu.commons.cassandra.thrift.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CtException extends BaseRuntimeException {

	private static final long serialVersionUID = 7455477350797302486L;

	public CtException(Throwable cause) {
		super(cause);
	}

	public CtException(String message, Throwable cause) {
		super(message, cause);
	}

	public CtException(String message) {
		super(message);
	}
}
