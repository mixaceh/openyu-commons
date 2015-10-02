package org.openyu.commons.fto.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CnfFtoException extends BaseRuntimeException {
	
	private static final long serialVersionUID = -7232266508228584182L;

	public CnfFtoException(Throwable cause) {
		super(cause);
	}

	public CnfFtoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CnfFtoException(String message) {
		super(message);
	}
}
