package org.openyu.commons.helper.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class HelperException extends BaseRuntimeException {

	private static final long serialVersionUID = 2208603105991226210L;

	public HelperException(Throwable cause) {
		super(cause);
	}

	public HelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public HelperException(String message) {
		super(message);
	}
}
