package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

public class UnknownEdgeDirectionException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3959565156198123015L;
	
	private static final int ERROR_CODE = 2008;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	public UnknownEdgeDirectionException() {
		// TODO Auto-generated constructor stub
	}

	public UnknownEdgeDirectionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnknownEdgeDirectionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownEdgeDirectionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownEdgeDirectionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
