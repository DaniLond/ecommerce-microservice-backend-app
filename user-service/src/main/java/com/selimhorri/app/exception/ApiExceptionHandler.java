package com.selimhorri.app.exception;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.selimhorri.app.exception.custom.DuplicateResourceException;
import com.selimhorri.app.exception.custom.InvalidInputException;
import com.selimhorri.app.exception.custom.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {
	
	@ExceptionHandler(value = {
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class
	})
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle validation exception*\n");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(ErrorCode.VALIDATION_ERROR.getCode())
				.message("Validation failed")
				.path(request.getRequestURI())
				.build();
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> 
			errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage())
		);
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle NOT FOUND exception*\n");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.errorCode(e.getErrorCode().getCode())
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = {
		EntityNotFoundException.class,
		EmptyResultDataAccessException.class
	})
	public ResponseEntity<ErrorResponse> handleJpaNotFoundException(RuntimeException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle JPA NOT FOUND exception*\n");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.errorCode(ErrorCode.USER_NOT_FOUND.getCode())
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle CONFLICT exception*\n");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.errorCode(e.getErrorCode().getCode())
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle BAD REQUEST (InvalidInput) exception*\n");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(e.getErrorCode().getCode())
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {
		IllegalArgumentException.class,
		IllegalStateException.class,
		NumberFormatException.class
	})
	public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle BAD REQUEST exception*\n");
		
		String message = e.getMessage();
		if (e instanceof NumberFormatException) {
			message = "Invalid ID format: must be a valid number";
		}
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(ErrorCode.INVALID_INPUT.getCode())
				.message(message)
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
		
		log.info("**ApiExceptionHandler controller, handle TYPE MISMATCH exception*\n");
		
		String message = String.format("Invalid value '%s' for parameter '%s': expected type %s", 
				e.getValue(), 
				e.getName(), 
				e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(ErrorCode.INVALID_FORMAT.getCode())
				.message(message)
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, HttpServletRequest request) {
		
		log.error("**ApiExceptionHandler controller, handle INTERNAL SERVER ERROR exception*\n", e);
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
				.message("An unexpected error occurred")
				.details(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}










