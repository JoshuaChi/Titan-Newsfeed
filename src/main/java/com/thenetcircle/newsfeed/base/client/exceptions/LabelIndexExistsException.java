/**
 * 
 */
package com.thenetcircle.newsfeed.base.client.exceptions;

/**
 * @author jchi
 *
 */
public class LabelIndexExistsException extends BaseException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3659040688189987757L;
	private static final int ERROR_CODE = 1006;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}


	/**
	 * 
	 */
	public LabelIndexExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public LabelIndexExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public LabelIndexExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LabelIndexExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public LabelIndexExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
