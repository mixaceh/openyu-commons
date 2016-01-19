package org.openyu.commons.fto.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class FtpClientFtoException extends BaseRuntimeException {
	
	private static final long serialVersionUID = -7232266508228584182L;

	public FtpClientFtoException(Throwable cause) {
		super(cause);
	}

	public FtpClientFtoException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpClientFtoException(String message) {
		super(message);
	}
}
