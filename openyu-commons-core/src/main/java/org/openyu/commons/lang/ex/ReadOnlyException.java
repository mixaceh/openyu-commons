package org.openyu.commons.lang.ex;


/**
 * The Class ReadOnlyException.
 */
public class ReadOnlyException extends BaseRuntimeException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2095364774244930409L;

	/**
	 * Instantiates a new read only exception.
	 */
	public ReadOnlyException()
	{
		super();
	}

	/**
	 * Instantiates a new read only exception.
	 *
	 * @param message the message
	 */
	public ReadOnlyException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new read only exception.
	 *
	 * @param cause the cause
	 */
	public ReadOnlyException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Instantiates a new read only exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ReadOnlyException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
