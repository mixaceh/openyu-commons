package org.openyu.commons.lang.exception;

import java.io.Serializable;
import java.io.PrintWriter;
import java.io.PrintStream;

import org.openyu.commons.mark.Supporter;

/**
 * The Class ExceptionChain.
 */
public class ExceptionChain implements Serializable, Supporter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3204274450818085633L;

	/** The next. */
	private ExceptionChain next;

	/** The ex. */
	private Throwable ex;

	/**
	 * Instantiates a new exception chain.
	 */
	public ExceptionChain() {
	}

	/**
	 * Instantiates a new exception chain.
	 *
	 * @param next
	 *            the next
	 * @param ex
	 *            the ex
	 */
	private ExceptionChain(ExceptionChain next, Throwable ex) {
		this.next = next;
		this.ex = ex;
	}

	/**
	 * Append.
	 *
	 * @param ex
	 *            the ex
	 */
	public void append(Throwable ex) {
		if (this.ex == null) {
			this.ex = ex;
		} else {
			next = new ExceptionChain(next, ex);
		}
	}

	/**
	 * Prints the stack trace.
	 *
	 * @param out
	 *            the out
	 */
	public void printStackTrace(PrintStream out) {
		PrintWriter writer = new PrintWriter(out);
		printStackTrace(this, writer);
		writer.flush();
	}

	/**
	 * Prints the stack trace.
	 *
	 * @param writer
	 *            the writer
	 */
	public void printStackTrace(PrintWriter writer) {
		printStackTrace(this, writer);
	}

	/**
	 * Prints the diagnostic stack trace.
	 */
	public void printDiagnosticStackTrace() {
		if (next != null) {
			next.printDiagnosticStackTrace();
			if (ex != null) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Prints the stack trace.
	 *
	 * @param exChain
	 *            the ex chain
	 * @param writer
	 *            the writer
	 */
	private void printStackTrace(ExceptionChain exChain, PrintWriter writer) {
		if (exChain != null && exChain.next != null) {
			exChain.printStackTrace(exChain.next, writer);
		}
		if (ex != null) {
			ex.printStackTrace(writer);
		}
	}

	/**
	 * Checks for exceptions.
	 *
	 * @return true, if successful
	 */
	public boolean hasExceptions() {
		return ex != null;
	}

	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public ExceptionChain getNext() {
		return next;
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public Throwable getException() {
		return ex;
	}

	/**
	 * Gets the original message.
	 *
	 * @param ex
	 *            the ex
	 * @return the original message
	 */
	public static String getOriginalMessage(Throwable ex) {
		while (ex instanceof ChainedException) {
			ExceptionChain chain = ((ChainedException) ex).getExceptionChain();
			if (chain == null) {
				break;
			}
			while (chain.getNext() != null) {
				chain = chain.getNext();
			}
			ex = chain.getException();
		}
		String message = ex.getMessage();
		if (message == null || message.length() == 0
				|| ex instanceof ArrayIndexOutOfBoundsException) {
			String name = ex.getClass().getName();
			message = (message == null ? name : name + " " + message);
		}
		return message;
	}
}
