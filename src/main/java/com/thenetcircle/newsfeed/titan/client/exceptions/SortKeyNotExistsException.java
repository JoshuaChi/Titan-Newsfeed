/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class SortKeyNotExistsException extends BaseException {
	
	private static final int ERROR_CODE = 2006;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -723576353914950712L;

	/**
	 * 
	 */
	public SortKeyNotExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SortKeyNotExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SortKeyNotExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SortKeyNotExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SortKeyNotExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
