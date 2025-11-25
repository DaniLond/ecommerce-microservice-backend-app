package com.selimhorri.app.exception.wrapper;

import com.selimhorri.app.exception.ErrorCode;
import com.selimhorri.app.exception.custom.ResourceNotFoundException;

public class UserObjectNotFoundException extends ResourceNotFoundException {
	
	private static final long serialVersionUID = 1L;

	public UserObjectNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
	
	public UserObjectNotFoundException(String message) {
		super(message);
	}
	
	public UserObjectNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
	
	public UserObjectNotFoundException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
