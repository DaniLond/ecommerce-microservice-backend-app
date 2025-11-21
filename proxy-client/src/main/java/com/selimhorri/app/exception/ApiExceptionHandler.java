package com.selimhorri.app.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.selimhorri.app.exception.payload.ExceptionMsg;
import com.selimhorri.app.exception.wrapper.CredentialNotFoundException;
import com.selimhorri.app.exception.wrapper.FavouriteNotFoundException;
import com.selimhorri.app.exception.wrapper.UnauthorizedException;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.exception.wrapper.VerificationTokenNotFoundException;

import feign.FeignException;
import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {
	
	@ExceptionHandler(value = {
		FeignClientException.class,
		FeignServerException.class,
		FeignException.class
	})
	public <T extends FeignException> ResponseEntity<ExceptionMsg> handleProxyException(final T e) {
		
		log.info("**ApiExceptionHandler controller, handle feign proxy exception*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;
		
		return new ResponseEntity<>(
				ExceptionMsg.builder()
					.msg(e.contentUTF8())
					.httpStatus(badRequest)
					.timestamp(ZonedDateTime
							.now(ZoneId.systemDefault()))
					.build(), badRequest);
	}
	
	@ExceptionHandler(value = {
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class
	})
	public <T extends BindException> ResponseEntity<ExceptionMsg> handleValidationException(final T e) {
		
		log.info("**ApiExceptionHandler controller, handle validation exception*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;
		
		String errorMsg = "Validation failed";
		if (e.getBindingResult().getFieldError() != null) {
			errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
		}
		
		return new ResponseEntity<>(
				ExceptionMsg.builder()
					.msg(errorMsg)
					.httpStatus(badRequest)
					.timestamp(ZonedDateTime
							.now(ZoneId.systemDefault()))
					.build(), badRequest);
	}
	
	@ExceptionHandler(value = {
		UnauthorizedException.class
	})
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleUnauthorizedException(final T e) {
		
		log.info("**ApiExceptionHandler controller, handle UNAUTHORIZED exception*\n");
		final var unauthorized = HttpStatus.UNAUTHORIZED;
		
		return new ResponseEntity<>(
				ExceptionMsg.builder()
					.msg(e.getMessage())
					.httpStatus(unauthorized)
					.timestamp(ZonedDateTime
							.now(ZoneId.systemDefault()))
					.build(), unauthorized);
	}
	
	@ExceptionHandler(value = {
		UserObjectNotFoundException.class,
		CredentialNotFoundException.class,
		VerificationTokenNotFoundException.class,
		FavouriteNotFoundException.class
	})
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleNotFoundException(final T e) {
		
		log.info("**ApiExceptionHandler controller, handle NOT FOUND exception*\n");
		final var notFound = HttpStatus.NOT_FOUND;
		
		return new ResponseEntity<>(
				ExceptionMsg.builder()
					.msg(e.getMessage())
					.httpStatus(notFound)
					.timestamp(ZonedDateTime
							.now(ZoneId.systemDefault()))
					.build(), notFound);
	}
	
	@ExceptionHandler(value = {
		IllegalStateException.class,
		IllegalArgumentException.class,
		NumberFormatException.class
	})
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleBadRequestException(final T e) {
		
		log.info("**ApiExceptionHandler controller, handle BAD REQUEST exception*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;
		
		String message = e.getMessage();
		if (e instanceof NumberFormatException) {
			message = "Invalid number format for ID parameter";
		}
		
		return new ResponseEntity<>(
				ExceptionMsg.builder()
					.msg(message)
					.httpStatus(badRequest)
					.timestamp(ZonedDateTime
							.now(ZoneId.systemDefault()))
					.build(), badRequest);
	}
	
}










