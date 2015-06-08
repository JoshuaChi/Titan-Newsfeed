/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class VertexIdNotExistsException extends BaseException {
	
	private static final int ERROR_CODE = 2001;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 7580479716490546204L;

	/**
	 * 
	 */
	public VertexIdNotExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VertexIdNotExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VertexIdNotExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VertexIdNotExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public VertexIdNotExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
