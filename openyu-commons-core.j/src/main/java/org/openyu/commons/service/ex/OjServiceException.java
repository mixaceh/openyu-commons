package org.openyu.commons.service.ex;

public class OjServiceException extends ServiceException {

	private static final long serialVersionUID = 8513107042643149239L;

	public OjServiceException(Throwable cause) {
		super(cause);
	}

	public OjServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public OjServiceException(String message) {
		super(message);
	}
}
