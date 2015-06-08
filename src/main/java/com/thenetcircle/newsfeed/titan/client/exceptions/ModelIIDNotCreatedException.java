/**
 * 
 */
package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

/**
 * @author jchi
 *
 */
public class ModelIIDNotCreatedException extends BaseException {
	
	private static final int ERROR_CODE = 2007;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2166580165919329098L;

	/**
	 * 
	 */
	public ModelIIDNotCreatedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ModelIIDNotCreatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ModelIIDNotCreatedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ModelIIDNotCreatedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ModelIIDNotCreatedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
