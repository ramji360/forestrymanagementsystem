package com.cg.fms.exception;

public class ContractException extends RuntimeException{
private static final long serialVersionUID = 1L;
	
	public ContractException(String errorMessage) {
		super(errorMessage);
	}

}
