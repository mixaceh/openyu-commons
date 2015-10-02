package org.openyu.commons.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CnfException extends BaseRuntimeException {

	private static final long serialVersionUID = 3874602650799121938L;

	public CnfException(Throwable cause) {
		super(cause);
	}

	public CnfException(String message, Throwable cause) {
		super(message, cause);
	}

	public CnfException(String message) {
		super(message);
	}
}
