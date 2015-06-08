package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

public class UnknownOrderDirectionException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3959565156198333015L;
	
	private static final int ERROR_CODE = 2005;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}

	public UnknownOrderDirectionException() {
		// TODO Auto-generated constructor stub
	}

	public UnknownOrderDirectionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnknownOrderDirectionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownOrderDirectionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownOrderDirectionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
