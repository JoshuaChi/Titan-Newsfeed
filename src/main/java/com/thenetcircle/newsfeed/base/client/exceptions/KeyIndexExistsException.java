/**
 * 
 */
package com.thenetcircle.newsfeed.base.client.exceptions;

/**
 * @author jchi
 *
 */
public class KeyIndexExistsException extends BaseException {
	
	private static final int ERROR_CODE = 1002;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -723576353914950775L;

	/**
	 * 
	 */
	public KeyIndexExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public KeyIndexExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public KeyIndexExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KeyIndexExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public KeyIndexExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
