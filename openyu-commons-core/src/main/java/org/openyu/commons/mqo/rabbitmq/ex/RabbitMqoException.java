package org.openyu.commons.mqo.rabbitmq.ex;

import org.openyu.commons.lang.ex.BaseRuntimeException;

public class RabbitMqoException extends BaseRuntimeException {

	private static final long serialVersionUID = -3173526426130924672L;

	public RabbitMqoException(Throwable cause) {
		super(cause);
	}

	public RabbitMqoException(String message, Throwable cause) {
		super(message, cause);
	}

	public RabbitMqoException(String message) {
		super(message);
	}
}
