/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class NoStartVertexFoundException extends BaseException {
	
	private static final int ERROR_CODE = 2010;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3520354122495383222L;

	/**
	 * 
	 */
	public NoStartVertexFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoStartVertexFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoStartVertexFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoStartVertexFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoStartVertexFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
