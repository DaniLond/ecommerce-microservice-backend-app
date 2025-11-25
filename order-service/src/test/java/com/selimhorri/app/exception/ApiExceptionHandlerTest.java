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
import com.selimhorri.app.exception.wrapper.OrderNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Order Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle OrderNotFoundException")
	void testHandleOrderNotFoundException() {
		OrderNotFoundException exception = new OrderNotFoundException("Order not found");
		
		ResponseEntity<ExceptionMsg> response = apiExceptionHandler.handleNotFoundException(exception);
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Order not found"));
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException")
	void testHandleNumberFormatException() {
		NumberFormatException exception = new NumberFormatException("Invalid ID");
		
		ResponseEntity<ExceptionMsg> response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Should handle IllegalArgumentException")
	void testHandleIllegalArgumentException() {
		IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
		
		ResponseEntity<ExceptionMsg> response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
