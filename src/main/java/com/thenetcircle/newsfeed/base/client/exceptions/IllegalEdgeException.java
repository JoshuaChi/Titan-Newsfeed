/**
 * 
 */
package com.thenetcircle.newsfeed.base.client.exceptions;

/**
 * @author jchi
 *
 */
public class IllegalEdgeException extends BaseException {
	
	private static final int ERROR_CODE = 1004;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3785509549759024903L;

	/**
	 * 
	 */
	public IllegalEdgeException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public IllegalEdgeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public IllegalEdgeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalEdgeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public IllegalEdgeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
