package com.thenetcircle.newsfeed.titan.client.exceptions;

import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;

public class UnknownKeyIndexTypeException extends BaseException {
	private static final int ERROR_CODE = 2003;
	
	public int getErrorCode() {
		return ERROR_CODE;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3822469529036257692L;

	public UnknownKeyIndexTypeException() {
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexTypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexTypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexTypeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownKeyIndexTypeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
