package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Shipping Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Mock
	private HttpServletRequest request;
	
	@BeforeEach
	void setUp() {
		when(request.getRequestURI()).thenReturn("/api/shipping");
	}
	
	@Test
	@DisplayName("Should handle ResourceNotFoundException")
	void testHandleResourceNotFoundException() {
		// Given
		ResourceNotFoundException exception = new ResourceNotFoundException("Shipping not found");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleResourceNotFoundException(exception, request);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
		assertTrue(response.getBody().getMessage().contains("Shipping not found"));
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException")
	void testHandleNumberFormatException() {
		// Given
		NumberFormatException exception = new NumberFormatException("Invalid ID");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleNumberFormatException(exception, request);
		
		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
		assertTrue(response.getBody().getMessage().contains("Invalid ID format"));
	}
	
	@Test
	@DisplayName("Should handle IllegalArgumentException")
	void testHandleIllegalArgumentException() {
		// Given
		IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
		
		// When
		ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleIllegalArgumentException(exception, request);
		
		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
	}
}
