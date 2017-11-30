package org.openyu.commons.commons.pool.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class CacheTemplateException extends BaseRuntimeException {

	private static final long serialVersionUID = -2270791234052068364L;

	public CacheTemplateException(Throwable cause) {
		super(cause);
	}

	public CacheTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheTemplateException(String message) {
		super(message);
	}
}
