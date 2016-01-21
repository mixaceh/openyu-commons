package org.openyu.commons.fto.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class FtoException extends BaseRuntimeException {

	private static final long serialVersionUID = 8513107042643149239L;

	public FtoException(Throwable cause) {
		super(cause);
	}

	public FtoException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtoException(String message) {
		super(message);
	}
}
