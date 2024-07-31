package com.cg.fms.exception;

public class UserException extends RuntimeException{
private static final long serialVersionUID = 1L;
	
	public UserException(String errorMessage) {
		super(errorMessage);
	}

}
