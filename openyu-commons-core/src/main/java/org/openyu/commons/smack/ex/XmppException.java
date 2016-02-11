package org.openyu.commons.smack.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class XmppException extends BaseRuntimeException {

	private static final long serialVersionUID = -5936535143956121204L;

	public XmppException(Throwable cause) {
		super(cause);
	}

	public XmppException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmppException(String message) {
		super(message);
	}
}
