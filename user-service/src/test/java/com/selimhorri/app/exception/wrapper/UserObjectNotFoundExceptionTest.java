package com.selimhorri.app.exception.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.selimhorri.app.exception.ErrorCode;
import com.selimhorri.app.exception.custom.ResourceNotFoundException;

@DisplayName("ResourceNotFoundException Tests")
class UserObjectNotFoundExceptionTest {
	
	@Test
	@DisplayName("Should create exception with message")
	void testConstructorWithMessage() {
		// Given
		String message = "User not found";
		
		// When
		ResourceNotFoundException exception = new ResourceNotFoundException(message);
		
		// Then
		assertNotNull(exception);
		assertEquals(message, exception.getMessage());
		assertNotNull(exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should create exception with error code")
	void testConstructorWithErrorCode() {
		// When
		ResourceNotFoundException exception = new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
		
		// Then
		assertNotNull(exception);
		assertNotNull(exception.getMessage());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should create exception with error code and formatted message")
	void testConstructorWithErrorCodeAndArgs() {
		// Given
		Integer userId = 999;
		
		// When
		ResourceNotFoundException exception = new ResourceNotFoundException(
			ErrorCode.USER_NOT_FOUND, userId
		);
		
		// Then
		assertNotNull(exception);
		assertNotNull(exception.getMessage());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should have error code when created with message")
	void testErrorCodeWithMessage() {
		// Given
		String message = "User with id: 123 not found";
		
		// When
		ResourceNotFoundException exception = new ResourceNotFoundException(message);
		
		// Then
		assertNotNull(exception.getErrorCode());
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
	
	@Test
	@DisplayName("Should be throwable")
	void testThrowException() {
		// Given
		String message = "User with id: 999 not found";
		
		// When & Then
		assertThrows(ResourceNotFoundException.class, () -> {
			throw new ResourceNotFoundException(message);
		});
	}
	
	@Test
	@DisplayName("Should preserve stack trace")
	void testStackTrace() {
		// When
		ResourceNotFoundException exception = new ResourceNotFoundException("Test exception");
		
		// Then
		assertNotNull(exception.getStackTrace());
		assertTrue(exception.getStackTrace().length > 0);
	}
}
