package org.openyu.commons.smack.ex;

public class XmppSessionException extends XmppException {

	private static final long serialVersionUID = -3960774783606656076L;

	public XmppSessionException(Throwable cause) {
		super(cause);
	}

	public XmppSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmppSessionException(String message) {
		super(message);
	}
}
