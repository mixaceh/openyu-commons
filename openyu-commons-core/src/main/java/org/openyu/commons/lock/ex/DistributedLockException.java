package org.openyu.commons.lock.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class DistributedLockException extends BaseRuntimeException {

	private static final long serialVersionUID = -2074668507421606578L;

	public DistributedLockException(Throwable cause) {
		super(cause);
	}

	public DistributedLockException(String message, Throwable cause) {
		super(message, cause);
	}

	public DistributedLockException(String message) {
		super(message);
	}
}
