package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selimhorri.app.exception.wrapper.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Proxy Client")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle UnauthorizedException")
	void testHandleUnauthorizedException() {
		var exception = new UnauthorizedException("Unauthorized access");
		var response = apiExceptionHandler.handleUnauthorizedException(exception);
		
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Unauthorized access"));
	}
	
	@Test
	@DisplayName("Should handle NumberFormatException")
	void testHandleNumberFormatException() {
		var exception = new NumberFormatException("Invalid ID");
		var response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMsg().contains("Invalid number format"));
	}
	
	@Test
	@DisplayName("Should handle IllegalArgumentException")
	void testHandleIllegalArgumentException() {
		var exception = new IllegalArgumentException("Invalid argument");
		var response = apiExceptionHandler.handleBadRequestException(exception);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
