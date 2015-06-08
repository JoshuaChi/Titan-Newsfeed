/**
 * 
 */
package com.thenetcircle.newsfeed.base.client.exceptions;

/**
 * @author jchi
 *
 */
public abstract class BaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7689571468872746419L;

	abstract public int getErrorCode();
	
	/**
	 * 
	 */
	public BaseException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public BaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BaseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
