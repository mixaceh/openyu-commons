package org.openyu.commons.hbase.thrift.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class HtException extends BaseRuntimeException {

	private static final long serialVersionUID = -2568884621368526779L;

	public HtException(Throwable cause) {
		super(cause);
	}

	public HtException(String message, Throwable cause) {
		super(message, cause);
	}

	public HtException(String message) {
		super(message);
	}
}
