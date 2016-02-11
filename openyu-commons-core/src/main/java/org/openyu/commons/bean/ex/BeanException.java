package org.openyu.commons.bean.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class BeanException extends BaseRuntimeException {

	private static final long serialVersionUID = -5141865627368869712L;

	public BeanException(Throwable cause) {
		super(cause);
	}

	public BeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanException(String message) {
		super(message);
	}
}
