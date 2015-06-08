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
public class VertexNotExistsException extends BaseException {
	private static final int ERROR_CODE = 1005;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4911377249222149127L;


	/**
	 * 
	 */
	public VertexNotExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VertexNotExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VertexNotExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VertexNotExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public VertexNotExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
