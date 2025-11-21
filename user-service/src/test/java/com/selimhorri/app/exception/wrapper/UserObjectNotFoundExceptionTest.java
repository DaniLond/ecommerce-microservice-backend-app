package com.selimhorri.app.exception.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserObjectNotFoundException Tests")
class UserObjectNotFoundExceptionTest {
	
	@Test
	@DisplayName("Should create exception with default constructor")
	void testDefaultConstructor() {
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException();
		
		// Then
		assertNotNull(exception);
		assertNull(exception.getMessage());
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
	}
	
	@Test
	@DisplayName("Should create exception with message and cause")
	void testConstructorWithMessageAndCause() {
		// Given
		String message = "User not found";
		Throwable cause = new RuntimeException("Database error");
		
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException(message, cause);
		
		// Then
		assertNotNull(exception);
		assertEquals(message, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}
	
	@Test
	@DisplayName("Should create exception with cause")
	void testConstructorWithCause() {
		// Given
		Throwable cause = new RuntimeException("Database error");
		
		// When
		UserObjectNotFoundException exception = new UserObjectNotFoundException(cause);
		
		// Then
		assertNotNull(exception);
		assertEquals(cause, exception.getCause());
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
}
