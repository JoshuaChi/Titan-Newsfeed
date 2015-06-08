package com.thenetcircle.newsfeed.test;

public class NewsfeedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public static enum Error {
		UserExisted,
		UserAlreadyFollowed,
		
	}
	
	public NewsfeedException(Error _error) {
		error = _error;
	}
	
	private Error error;

	public Error getError() {
		return error;
	}
	
	
}
