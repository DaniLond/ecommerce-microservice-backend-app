package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.selimhorri.app.exception.payload.ExceptionMsg;
import com.selimhorri.app.exception.wrapper.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Product Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle ProductNotFoundException")
	void testHandleProductNotFoundException() {
		// Given
		ProductNotFoundException exception = new ProductNotFoundException("Product not found");
		
		// When
		ResponseEntity<ExceptionMsg> response = apiExceptionHandler.handleNotFoundException(exception);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Product not found"));
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException")
	void testHandleNumberFormatException() {
		// Given
		NumberFormatException exception = new NumberFormatException("Invalid ID");
		
		// When
		ResponseEntity<ExceptionMsg> response = apiExceptionHandler.handleBadRequestException(exception);
		
		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Invalid number format"));
	}
}
