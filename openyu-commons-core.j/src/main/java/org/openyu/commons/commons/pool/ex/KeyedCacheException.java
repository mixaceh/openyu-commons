package org.openyu.commons.commons.pool.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class KeyedCacheException extends BaseRuntimeException {

	private static final long serialVersionUID = -511828698167024896L;

	public KeyedCacheException(Throwable cause) {
		super(cause);
	}

	public KeyedCacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyedCacheException(String message) {
		super(message);
	}
}
