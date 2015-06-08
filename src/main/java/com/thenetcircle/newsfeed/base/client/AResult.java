package com.thenetcircle.newsfeed.base.client;

public abstract class AResult {

	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	
	//exception error code starts from 1000
	public static final int GENERAL_ERROR_CODE = 1000;
	
	public static final int JSON = 0;
	public static final int XML = 1;
}
