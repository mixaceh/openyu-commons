package org.openyu.commons.hbase.ex;

public class HzSessionException extends HzException {

	private static final long serialVersionUID = 4748487320658106804L;

	public HzSessionException(Throwable cause) {
		super(cause);
	}

	public HzSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public HzSessionException(String message) {
		super(message);
	}
}
