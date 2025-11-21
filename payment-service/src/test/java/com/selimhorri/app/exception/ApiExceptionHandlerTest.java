package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selimhorri.app.exception.wrapper.PaymentNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Payment Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle PaymentNotFoundException")
	void testHandlePaymentNotFoundException() {
		var exception = new PaymentNotFoundException("Payment not found");
		var response = apiExceptionHandler.handleNotFoundException(exception);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Payment not found"));
	}
	
	@Test
	@DisplayName("Should handle bad request exceptions")
	void testHandleBadRequestException() {
		var exception = new NumberFormatException("Invalid format");
		var response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
