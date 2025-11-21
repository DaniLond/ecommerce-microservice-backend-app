package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selimhorri.app.exception.wrapper.OrderItemNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Shipping Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle OrderItemNotFoundException")
	void testHandleOrderItemNotFoundException() {
		var exception = new OrderItemNotFoundException("OrderItem not found");
		var response = apiExceptionHandler.handleNotFoundException(exception);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException")
	void testHandleNumberFormatException() {
		var exception = new NumberFormatException("Invalid ID");
		var response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
