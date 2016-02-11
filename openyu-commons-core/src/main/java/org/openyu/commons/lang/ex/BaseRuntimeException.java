package org.openyu.commons.lang.ex;

/**
 * The Class BaseRuntimeException.
 */
public class BaseRuntimeException extends RuntimeException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3502512403089659123L;

	/**
	 * Instantiates a new base runtime exception.
	 */
	public BaseRuntimeException()
	{
		super();
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param message the message
	 */
	public BaseRuntimeException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param cause the cause
	 */
	public BaseRuntimeException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public BaseRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
