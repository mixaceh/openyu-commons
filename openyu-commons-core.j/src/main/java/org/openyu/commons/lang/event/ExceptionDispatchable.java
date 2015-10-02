package org.openyu.commons.lang.event;

import java.util.EventListener;

/**
 * The Interface ExceptionDispatchable.
 */
public interface ExceptionDispatchable {

	/**
	 * Exception dispatch.
	 *
	 * @param listener
	 *            the listener
	 * @throws Exception
	 *             the exception
	 */
	void exceptionDispatch(EventListener listener) throws Exception;
}
