package org.openyu.commons.bao.redis.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class RedisBaoException extends BaseRuntimeException {

	private static final long serialVersionUID = 2189221209457124946L;

	public RedisBaoException(Throwable cause) {
		super(cause);
	}

	public RedisBaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisBaoException(String message) {
		super(message);
	}
}
