package org.openyu.commons.commons.net.ftp.ex;

public class CnfSessionException extends CnfException {

	private static final long serialVersionUID = -1651576343772148636L;

	public CnfSessionException(Throwable cause) {
		super(cause);
	}

	public CnfSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CnfSessionException(String message) {
		super(message);
	}
}
