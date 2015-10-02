package org.openyu.commons.lang.event;

import java.util.EventListener;

/**
 * The Interface EventDispatchable.
 */
public interface EventDispatchable {

	/**
	 * Dispatch.
	 *
	 * @param listener
	 *            the listener
	 */
	void dispatch(EventListener listener);
}
