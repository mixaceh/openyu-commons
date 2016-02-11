package org.openyu.commons.lang.event;

import org.openyu.commons.lang.ex.ExceptionChain;

/**
 * 事件
 *
 * The Interface BaseEvent.
 */
public interface BaseEvent {

	/** The key. */
	String KEY = BaseEvent.class.getName();

	/**
	 * Gets the exception chain.
	 *
	 * @return the exception chain
	 */
	ExceptionChain getExceptionChain();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Type of.
	 *
	 * @param name
	 *            the name
	 * @return the int
	 */
	int typeOf(String name);

	/**
	 * Gets the attach.
	 *
	 * @return the attach
	 */
	Object getAttach();
}
