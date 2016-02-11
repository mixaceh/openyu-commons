package org.openyu.commons.dao.ex;

public class CommonDaoException extends DaoException {

	private static final long serialVersionUID = 3989513665484115135L;

	public CommonDaoException(Throwable cause) {
		super(cause);
	}

	public CommonDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonDaoException(String message) {
		super(message);
	}
}
