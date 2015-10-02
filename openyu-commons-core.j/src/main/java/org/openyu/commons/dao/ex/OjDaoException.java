package org.openyu.commons.dao.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class OjDaoException extends BaseRuntimeException {

	private static final long serialVersionUID = 8513107042643149239L;

	public OjDaoException(Throwable cause) {
		super(cause);
	}

	public OjDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public OjDaoException(String message) {
		super(message);
	}
}
