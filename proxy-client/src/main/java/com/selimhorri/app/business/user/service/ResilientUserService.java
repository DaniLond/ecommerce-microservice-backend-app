package com.selimhorri.app.business.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.selimhorri.app.business.user.model.UserDto;
import com.selimhorri.app.business.user.model.response.UserUserServiceCollectionDtoResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resilient wrapper service for User Client Service.
 * 
 * This service wraps the Feign client with Resilience4j patterns:
 * - Circuit Breaker: Prevents cascading failures by stopping calls to failing services
 * - Retry: Automatically retries failed calls with exponential backoff
 * - Time Limiter: Ensures calls don't hang indefinitely
 * - Bulkhead: Limits concurrent calls to prevent resource exhaustion
 * 
 * All methods have fallback implementations that provide graceful degradation
 * when the user service is unavailable.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientUserService {
	
	private final UserClientService userClientService;
	private final UserServiceFallback userServiceFallback;
	
	/**
	 * Find all users with resilience patterns.
	 * Circuit breaker opens after 50% failure rate over 10 calls.
	 * Retries up to 3 times with exponential backoff.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "findAllFallback")
	@Retry(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserUserServiceCollectionDtoResponse> findAll() {
		log.debug("Calling User Service - findAll");
		return userClientService.findAll();
	}
	
	/**
	 * Find user by ID with resilience patterns.
	 * Times out after 3 seconds to prevent hanging calls.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "findByIdFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserDto> findById(String userId) {
		log.debug("Calling User Service - findById: {}", userId);
		return userClientService.findById(userId);
	}
	
	/**
	 * Find user by username with resilience patterns.
	 * Critical for authentication flows.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "findByUsernameFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserDto> findByUsername(String username) {
		log.debug("Calling User Service - findByUsername: {}", username);
		return userClientService.findByUsername(username);
	}
	
	/**
	 * Save user with resilience patterns.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "saveFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserDto> save(UserDto userDto) {
		log.debug("Calling User Service - save");
		return userClientService.save(userDto);
	}
	
	/**
	 * Update user with resilience patterns.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "updateFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserDto> update(UserDto userDto) {
		log.debug("Calling User Service - update");
		return userClientService.update(userDto);
	}
	
	/**
	 * Update user by ID with resilience patterns.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "updateByIdFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<UserDto> update(String userId, UserDto userDto) {
		log.debug("Calling User Service - updateById: {}", userId);
		return userClientService.update(userId, userDto);
	}
	
	/**
	 * Delete user by ID with resilience patterns.
	 */
	@CircuitBreaker(name = "userService", fallbackMethod = "deleteByIdFallback")
	@Retry(name = "userService")
	@TimeLimiter(name = "userService")
	@Bulkhead(name = "userService")
	public ResponseEntity<Boolean> deleteById(String userId) {
		log.debug("Calling User Service - deleteById: {}", userId);
		return userClientService.deleteById(userId);
	}
	
	// Fallback methods delegate to the fallback component
	
	private ResponseEntity<UserUserServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		return userServiceFallback.findAllFallback(throwable);
	}
	
	private ResponseEntity<UserDto> findByIdFallback(String userId, Throwable throwable) {
		return userServiceFallback.findByIdFallback(userId, throwable);
	}
	
	private ResponseEntity<UserDto> findByUsernameFallback(String username, Throwable throwable) {
		return userServiceFallback.findByUsernameFallback(username, throwable);
	}
	
	private ResponseEntity<UserDto> saveFallback(UserDto userDto, Throwable throwable) {
		return userServiceFallback.saveFallback(userDto, throwable);
	}
	
	private ResponseEntity<UserDto> updateFallback(UserDto userDto, Throwable throwable) {
		return userServiceFallback.updateFallback(userDto, throwable);
	}
	
	private ResponseEntity<UserDto> updateByIdFallback(String userId, UserDto userDto, Throwable throwable) {
		return userServiceFallback.updateByIdFallback(userId, userDto, throwable);
	}
	
	private ResponseEntity<Boolean> deleteByIdFallback(String userId, Throwable throwable) {
		return userServiceFallback.deleteByIdFallback(userId, throwable);
	}
	
}
