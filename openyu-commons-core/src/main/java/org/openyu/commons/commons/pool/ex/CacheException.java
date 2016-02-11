package org.openyu.commons.commons.pool.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CacheException extends BaseRuntimeException {

	private static final long serialVersionUID = -511828698167024896L;

	public CacheException(Throwable cause) {
		super(cause);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(String message) {
		super(message);
	}
}
