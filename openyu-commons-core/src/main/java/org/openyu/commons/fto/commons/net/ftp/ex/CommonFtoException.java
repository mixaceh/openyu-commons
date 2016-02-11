package org.openyu.commons.fto.commons.net.ftp.ex;

public class CommonFtoException extends FtoException {

	private static final long serialVersionUID = 7236874301682323929L;

	public CommonFtoException(Throwable cause) {
		super(cause);
	}

	public CommonFtoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonFtoException(String message) {
		super(message);
	}
}
