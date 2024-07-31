package com.cg.fms.exception;

public class ProductException extends RuntimeException{
private static final long serialVersionUID = 1L;
	
	public ProductException(String errorMessage) {
		super(errorMessage);
	}

}
