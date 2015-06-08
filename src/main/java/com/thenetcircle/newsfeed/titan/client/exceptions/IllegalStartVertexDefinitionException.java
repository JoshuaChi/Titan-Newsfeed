/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class IllegalStartVertexDefinitionException extends BaseException {
	
	private static final int ERROR_CODE = 2008;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -1619490102351687929L;

	/**
	 * 
	 */
	public IllegalStartVertexDefinitionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public IllegalStartVertexDefinitionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public IllegalStartVertexDefinitionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalStartVertexDefinitionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public IllegalStartVertexDefinitionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
