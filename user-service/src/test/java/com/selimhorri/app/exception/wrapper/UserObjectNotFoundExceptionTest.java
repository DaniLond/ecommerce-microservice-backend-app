package com.selimhorri.app.exception.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.selimhorri.app.exception.ErrorCode;

@DisplayName("UserObjectNotFoundException Tests")
class UserObjectNotFoundExceptionTest {
	
	@Test
	@DisplayName("Should create exception with default constructor")
	void testDefaultConstructor() {
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException();
		
		// Then
		assertNotNull(exception);
		assertNotNull(exception.getMessage());
		assertNotNull(exception.getErrorCode());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should create exception with message")
	void testConstructorWithMessage() {
		// Given
		String message = "User not found";
		
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException(message);
		
		// Then
		assertNotNull(exception);
		assertEquals(message, exception.getMessage());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should create exception with ErrorCode")
	void testConstructorWithErrorCode() {
		// Given
		ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
		
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException(errorCode);
		
		// Then
		assertNotNull(exception);
		assertEquals(errorCode, exception.getErrorCode());
		assertNotNull(exception.getMessage());
	}
	
	@Test
	@DisplayName("Should create exception with ErrorCode and arguments")
	void testConstructorWithErrorCodeAndArgs() {
		// Given
		ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
		Object[] args = {999};
		
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException(errorCode, args);
		
		// Then
		assertNotNull(exception);
		assertEquals(errorCode, exception.getErrorCode());
		assertNotNull(exception.getMessage());
	}
	
	@Test
	@DisplayName("Should be throwable")
	void testThrowException() {
		// Given
		String message = "User with id: 999 not found";
		
		// When & Then
		assertThrows(UserObjectNotFoundException.class, () -> {
			throw new UserObjectNotFoundException(message);
		});
	}
	
	@Test
	@DisplayName("Should preserve stack trace")
	void testStackTrace() {
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException("Test exception");
		
		// Then
		assertNotNull(exception.getStackTrace());
		assertTrue(exception.getStackTrace().length > 0);
	}
	
	@Test
	@DisplayName("Should have error code accessor")
	void testGetErrorCode() {
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException("User not found");
		
		// Then
		assertNotNull(exception.getErrorCode());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
}
