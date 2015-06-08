/**
 * 
 */
package com.thenetcircle.newsfeed.base.client.exceptions;

/**
 * Throw this exception when vertex does exist in graph.
 * 
 * @author Joshua Chi<jchi@thenetcircle.com>
 *
 */
public class VertexExistsException extends BaseException {
	
	private static final int ERROR_CODE = 1001;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6268717553667554370L;

	/**
	 * 
	 */
	public VertexExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VertexExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VertexExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VertexExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public VertexExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
