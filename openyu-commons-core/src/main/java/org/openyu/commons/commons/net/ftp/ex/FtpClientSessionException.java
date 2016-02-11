package org.openyu.commons.commons.net.ftp.ex;

public class FtpClientSessionException extends FtpClientException {

	private static final long serialVersionUID = -1651576343772148636L;

	public FtpClientSessionException(Throwable cause) {
		super(cause);
	}

	public FtpClientSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpClientSessionException(String message) {
		super(message);
	}
}
