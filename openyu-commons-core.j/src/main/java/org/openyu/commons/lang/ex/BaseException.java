package org.openyu.commons.lang.ex;

import org.openyu.commons.mark.Supporter;

/**
 * The Class BaseException.
 */
public class BaseException extends Exception implements Supporter
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8239639919543229765L;

	/**
	 * Instantiates a new base exception.
	 */
	public BaseException()
	{
		super();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 */
	public BaseException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param cause the cause
	 */
	public BaseException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public BaseException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
