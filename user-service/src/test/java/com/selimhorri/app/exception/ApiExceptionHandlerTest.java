package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.selimhorri.app.exception.custom.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Mock
	private HttpServletRequest request;
	
	@BeforeEach
	void setUp() {
		when(request.getRequestURI()).thenReturn("/api/users");
	}
	
	@Test
	@DisplayName("Should handle ResourceNotFoundException")
	void testHandleResourceNotFoundException() {
		// Given
		ResourceNotFoundException exception = new ResourceNotFoundException("User not found");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleResourceNotFoundException(exception, request);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("User not found"));
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
	}
	
	@Test
	@DisplayName("Should handle IllegalArgumentException")
	void testHandleIllegalArgumentException() {
		// Given
		IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
		when(request.getRequestURI()).thenReturn("/api/users");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleBadRequestException(exception, request);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Invalid argument"));
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException with specific message")
	void testHandleNumberFormatException() {
		// Given
		NumberFormatException exception = new NumberFormatException("For input string: \"abc\"");
		when(request.getRequestURI()).thenReturn("/api/users/abc");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleBadRequestException(exception, request);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Invalid ID format"));
	}
	
	@Test
	@DisplayName("Should handle IllegalStateException")
	void testHandleIllegalStateException() {
		// Given
		IllegalStateException exception = new IllegalStateException("Invalid state");
		when(request.getRequestURI()).thenReturn("/api/users");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleBadRequestException(exception, request);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().contains("Invalid state"));
	}
}
