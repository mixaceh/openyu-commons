package org.openyu.commons.service.ex;

public class CommonServiceException extends ServiceException {

	private static final long serialVersionUID = 8513107042643149239L;

	public CommonServiceException(Throwable cause) {
		super(cause);
	}

	public CommonServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonServiceException(String message) {
		super(message);
	}
}
