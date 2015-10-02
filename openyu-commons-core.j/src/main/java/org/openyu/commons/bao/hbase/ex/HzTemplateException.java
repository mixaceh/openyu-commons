package org.openyu.commons.bao.hbase.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class HzTemplateException extends BaseRuntimeException {

	private static final long serialVersionUID = 1583148544289202096L;

	public HzTemplateException(Throwable cause) {
		super(cause);
	}

	public HzTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public HzTemplateException(String message) {
		super(message);
	}
}
