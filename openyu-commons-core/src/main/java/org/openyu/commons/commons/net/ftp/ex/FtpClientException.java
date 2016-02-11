package org.openyu.commons.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class FtpClientException extends BaseRuntimeException {

	private static final long serialVersionUID = 3874602650799121938L;

	public FtpClientException(Throwable cause) {
		super(cause);
	}

	public FtpClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpClientException(String message) {
		super(message);
	}
}
