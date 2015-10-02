package org.openyu.commons.editor.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class EditorException extends BaseRuntimeException {

	private static final long serialVersionUID = 8513107042643149239L;

	public EditorException(Throwable cause) {
		super(cause);
	}

	public EditorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EditorException(String message) {
		super(message);
	}
}
