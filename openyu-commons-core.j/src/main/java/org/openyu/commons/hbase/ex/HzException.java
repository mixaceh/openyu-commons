package org.openyu.commons.hbase.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class HzException extends BaseRuntimeException {

	private static final long serialVersionUID = 8025326065436715908L;

	public HzException(Throwable cause) {
		super(cause);
	}

	public HzException(String message, Throwable cause) {
		super(message, cause);
	}

	public HzException(String message) {
		super(message);
	}
}
