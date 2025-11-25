package com.selimhorri.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selimhorri.app.exception.wrapper.ProductNotFoundException;
import com.selimhorri.app.exception.wrapper.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler Tests - Favourite Service")
class ApiExceptionHandlerTest {
	
	@InjectMocks
	private ApiExceptionHandler apiExceptionHandler;
	
	@Test
	@DisplayName("Should handle ProductNotFoundException")
	void testHandleProductNotFoundException() {
		var exception = new ProductNotFoundException("Product not found");
		var response = apiExceptionHandler.handleNotFoundException(exception);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Should handle UserNotFoundException")
	void testHandleUserNotFoundException() {
		var exception = new UserNotFoundException("User not found");
		var response = apiExceptionHandler.handleNotFoundException(exception);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	

}
