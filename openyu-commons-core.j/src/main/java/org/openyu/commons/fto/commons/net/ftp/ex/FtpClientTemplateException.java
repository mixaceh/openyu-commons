package org.openyu.commons.fto.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class FtpClientTemplateException extends BaseRuntimeException {

	private static final long serialVersionUID = 7610353693203462696L;

	public FtpClientTemplateException(Throwable cause) {
		super(cause);
	}

	public FtpClientTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpClientTemplateException(String message) {
		super(message);
	}
}
