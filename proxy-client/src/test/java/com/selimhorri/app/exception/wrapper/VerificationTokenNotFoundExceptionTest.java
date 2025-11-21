package com.selimhorri.app.exception.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("VerificationTokenNotFoundException Tests")
class VerificationTokenNotFoundExceptionTest {
	
	@Test
	@DisplayName("Should create exception with default constructor")
	void testDefaultConstructor() {
		// When
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException();
		
		// Then
		assertNotNull(exception);
		assertNull(exception.getMessage());
	}
	
	@Test
	@DisplayName("Should create exception with message")
	void testConstructorWithMessage() {
		// Given
		String message = "Verification token not found";
		
		// When
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException(message);
		
		// Then
		assertNotNull(exception);
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@DisplayName("Should create exception with message and cause")
	void testConstructorWithMessageAndCause() {
		// Given
		String message = "Verification token not found";
		Throwable cause = new RuntimeException("Token expired");
		
		// When
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException(message, cause);
		
		// Then
		assertNotNull(exception);
		assertEquals(message, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}
	
	@Test
	@DisplayName("Should create exception with cause only")
	void testConstructorWithCause() {
		// Given
		Throwable cause = new RuntimeException("Token expired");
		
		// When
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException(cause);
		
		// Then
		assertNotNull(exception);
		assertEquals(cause, exception.getCause());
	}
	
	@Test
	@DisplayName("Should be throwable")
	void testThrowException() {
		// Given
		String message = "Token with id: 123 not found";
		
		// When & Then
		assertThrows(VerificationTokenNotFoundException.class, () -> {
			throw new VerificationTokenNotFoundException(message);
		});
	}
	
	@Test
	@DisplayName("Should preserve stack trace")
	void testStackTrace() {
		// When
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException("Test exception");
		
		// Then
		assertNotNull(exception.getStackTrace());
		assertTrue(exception.getStackTrace().length > 0);
	}
	
	@Test
	@DisplayName("Should be instance of RuntimeException")
	void testInstanceOfRuntimeException() {
		// Given
		VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException("Test message");
		
		// Then
		assertTrue(exception instanceof RuntimeException);
	}
}
