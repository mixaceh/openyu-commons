package org.openyu.commons.dao.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class DaoException extends BaseRuntimeException {

	private static final long serialVersionUID = 8513107042643149239L;

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}
}
