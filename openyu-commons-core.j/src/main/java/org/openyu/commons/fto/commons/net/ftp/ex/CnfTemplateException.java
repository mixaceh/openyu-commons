package org.openyu.commons.fto.commons.net.ftp.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CnfTemplateException extends BaseRuntimeException {

	private static final long serialVersionUID = 7610353693203462696L;

	public CnfTemplateException(Throwable cause) {
		super(cause);
	}

	public CnfTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CnfTemplateException(String message) {
		super(message);
	}
}
