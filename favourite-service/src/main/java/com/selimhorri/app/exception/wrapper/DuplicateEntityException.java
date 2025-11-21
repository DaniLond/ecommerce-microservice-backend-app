package com.selimhorri.app.exception.wrapper;

public class DuplicateEntityException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public DuplicateEntityException() {
		super();
	}
	
	public DuplicateEntityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DuplicateEntityException(String message) {
		super(message);
	}
	
	public DuplicateEntityException(Throwable cause) {
		super(cause);
	}
	
}
