/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class KeyIndexTypeNotDefinedException extends BaseException {
	
	private static final int ERROR_CODE = 2002;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -7933638496389357191L;

	/**
	 * 
	 */
	public KeyIndexTypeNotDefinedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public KeyIndexTypeNotDefinedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public KeyIndexTypeNotDefinedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KeyIndexTypeNotDefinedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public KeyIndexTypeNotDefinedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
