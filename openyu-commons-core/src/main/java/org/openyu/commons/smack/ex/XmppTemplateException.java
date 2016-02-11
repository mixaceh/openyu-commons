package org.openyu.commons.smack.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class XmppTemplateException extends BaseRuntimeException {

	private static final long serialVersionUID = 2265588733910964500L;

	public XmppTemplateException(Throwable cause) {
		super(cause);
	}

	public XmppTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmppTemplateException(String message) {
		super(message);
	}
}
