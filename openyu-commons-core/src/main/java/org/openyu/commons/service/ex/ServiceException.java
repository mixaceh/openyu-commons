package org.openyu.commons.service.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class ServiceException extends BaseRuntimeException {

	private static final long serialVersionUID = 2208603105991226210L;

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
}
