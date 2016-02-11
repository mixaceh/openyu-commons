package org.openyu.commons.bao.hbase.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class HzBaoException extends BaseRuntimeException {

	private static final long serialVersionUID = 5761929366838631596L;

	public HzBaoException(Throwable cause) {
		super(cause);
	}

	public HzBaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public HzBaoException(String message) {
		super(message);
	}
}
