package org.openyu.commons.collector.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CollectorException extends BaseRuntimeException {

	private static final long serialVersionUID = 8513107042643149239L;

	public CollectorException(Throwable cause) {
		super(cause);
	}

	public CollectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public CollectorException(String message) {
		super(message);
	}
}
