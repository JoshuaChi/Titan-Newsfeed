package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

public class UnknownKeyIndexException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3959565156198052015L;
	
	private static final int ERROR_CODE = 2004;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	public UnknownKeyIndexException() {
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
