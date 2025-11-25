package com.selimhorri.app.business.user.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.selimhorri.app.business.user.model.UserDto;
import com.selimhorri.app.business.user.model.response.UserUserServiceCollectionDtoResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Fallback implementation for User Service to provide graceful degradation
 * when the user service is unavailable or responding slowly.
 * 
 * This fallback provides cached or default responses to maintain partial functionality
 * during service disruptions.
 */
@Component
@Slf4j
public class UserServiceFallback {
	
	/**
	 * Fallback for findAll operation.
	 * Returns empty collection when user service is unavailable.
	 */
	public ResponseEntity<UserUserServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		log.warn("User Service findAll fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(UserUserServiceCollectionDtoResponse.builder()
						.collection(Collections.emptyList())
						.build());
	}
	
	/**
	 * Fallback for findById operation.
	 * Returns null response when user service is unavailable.
	 */
	public ResponseEntity<UserDto> findByIdFallback(String userId, Throwable throwable) {
		log.warn("User Service findById fallback triggered for userId: {}. Reason: {}", 
				userId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(null);
	}
	
	/**
	 * Fallback for findByUsername operation.
	 * Returns null response when user service is unavailable.
	 */
	public ResponseEntity<UserDto> findByUsernameFallback(String username, Throwable throwable) {
		log.warn("User Service findByUsername fallback triggered for username: {}. Reason: {}", 
				username, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(null);
	}
	
	/**
	 * Fallback for save operation.
	 * Returns the original DTO with error status when user service is unavailable.
	 */
	public ResponseEntity<UserDto> saveFallback(UserDto userDto, Throwable throwable) {
		log.warn("User Service save fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(userDto);
	}
	
	/**
	 * Fallback for update operation.
	 * Returns the original DTO with error status when user service is unavailable.
	 */
	public ResponseEntity<UserDto> updateFallback(UserDto userDto, Throwable throwable) {
		log.warn("User Service update fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(userDto);
	}
	
	/**
	 * Fallback for updateById operation.
	 * Returns the original DTO with error status when user service is unavailable.
	 */
	public ResponseEntity<UserDto> updateByIdFallback(String userId, UserDto userDto, Throwable throwable) {
		log.warn("User Service updateById fallback triggered for userId: {}. Reason: {}", 
				userId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(userDto);
	}
	
	/**
	 * Fallback for deleteById operation.
	 * Returns false when user service is unavailable.
	 */
	public ResponseEntity<Boolean> deleteByIdFallback(String userId, Throwable throwable) {
		log.warn("User Service deleteById fallback triggered for userId: {}. Reason: {}", 
				userId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(false);
	}
	
}
