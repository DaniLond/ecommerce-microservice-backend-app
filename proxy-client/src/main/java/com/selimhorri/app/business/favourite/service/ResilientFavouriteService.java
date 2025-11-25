package com.selimhorri.app.business.favourite.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.selimhorri.app.business.favourite.model.FavouriteDto;
import com.selimhorri.app.business.favourite.model.FavouriteId;
import com.selimhorri.app.business.favourite.model.response.FavouriteFavouriteServiceCollectionDtoResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resilient wrapper service for Favourite Client Service.
 * 
 * This service wraps the Feign client with Resilience4j patterns:
 * - Circuit Breaker: Prevents cascading failures by stopping calls to failing services
 * - Retry: Automatically retries failed calls with exponential backoff
 * - Time Limiter: Ensures calls don't hang indefinitely
 * - Bulkhead: Limits concurrent calls to prevent resource exhaustion
 * 
 * All methods have fallback implementations that provide graceful degradation
 * when the favourite service is unavailable.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientFavouriteService {
	
	private final FavouriteClientService favouriteClientService;
	private final FavouriteServiceFallback favouriteServiceFallback;
	
	/**
	 * Find all favourites with resilience patterns.
	 * Circuit breaker opens after 50% failure rate over 10 calls.
	 * Retries up to 3 times with exponential backoff.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "findAllFallback")
	@Retry(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<FavouriteFavouriteServiceCollectionDtoResponse> findAll() {
		log.debug("Calling Favourite Service - findAll");
		return favouriteClientService.findAll();
	}
	
	/**
	 * Find favourite by composite ID (userId, productId, likeDate) with resilience patterns.
	 * Times out after 3 seconds to prevent hanging calls.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "findByIdFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<FavouriteDto> findById(String userId, String productId, String likeDate) {
		log.debug("Calling Favourite Service - findById: userId={}, productId={}, likeDate={}", 
				userId, productId, likeDate);
		return favouriteClientService.findById(userId, productId, likeDate);
	}
	
	/**
	 * Find favourite by FavouriteId object with resilience patterns.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "findByIdObjectFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<FavouriteDto> findById(FavouriteId favouriteId) {
		log.debug("Calling Favourite Service - findById: {}", favouriteId);
		return favouriteClientService.findById(favouriteId);
	}
	
	/**
	 * Save favourite with resilience patterns.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "saveFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<FavouriteDto> save(FavouriteDto favouriteDto) {
		log.debug("Calling Favourite Service - save");
		return favouriteClientService.save(favouriteDto);
	}
	
	/**
	 * Update favourite with resilience patterns.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "updateFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<FavouriteDto> update(FavouriteDto favouriteDto) {
		log.debug("Calling Favourite Service - update");
		return favouriteClientService.update(favouriteDto);
	}
	
	/**
	 * Delete favourite by composite ID with resilience patterns.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "deleteByIdFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<Boolean> deleteById(String userId, String productId, String likeDate) {
		log.debug("Calling Favourite Service - deleteById: userId={}, productId={}, likeDate={}", 
				userId, productId, likeDate);
		return favouriteClientService.deleteById(userId, productId, likeDate);
	}
	
	/**
	 * Delete favourite by FavouriteId object with resilience patterns.
	 */
	@CircuitBreaker(name = "favouriteService", fallbackMethod = "deleteByIdObjectFallback")
	@Retry(name = "favouriteService")
	@TimeLimiter(name = "favouriteService")
	@Bulkhead(name = "favouriteService")
	public ResponseEntity<Boolean> deleteById(FavouriteId favouriteId) {
		log.debug("Calling Favourite Service - deleteById: {}", favouriteId);
		return favouriteClientService.deleteById(favouriteId);
	}
	
	// Fallback methods delegate to the fallback component
	
	private ResponseEntity<FavouriteFavouriteServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		return favouriteServiceFallback.findAllFallback(throwable);
	}
	
	private ResponseEntity<FavouriteDto> findByIdFallback(String userId, String productId, String likeDate, Throwable throwable) {
		return favouriteServiceFallback.findByIdFallback(userId, productId, likeDate, throwable);
	}
	
	private ResponseEntity<FavouriteDto> findByIdObjectFallback(FavouriteId favouriteId, Throwable throwable) {
		return favouriteServiceFallback.findByIdObjectFallback(favouriteId, throwable);
	}
	
	private ResponseEntity<FavouriteDto> saveFallback(FavouriteDto favouriteDto, Throwable throwable) {
		return favouriteServiceFallback.saveFallback(favouriteDto, throwable);
	}
	
	private ResponseEntity<FavouriteDto> updateFallback(FavouriteDto favouriteDto, Throwable throwable) {
		return favouriteServiceFallback.updateFallback(favouriteDto, throwable);
	}
	
	private ResponseEntity<Boolean> deleteByIdFallback(String userId, String productId, String likeDate, Throwable throwable) {
		return favouriteServiceFallback.deleteByIdFallback(userId, productId, likeDate, throwable);
	}
	
	private ResponseEntity<Boolean> deleteByIdObjectFallback(FavouriteId favouriteId, Throwable throwable) {
		return favouriteServiceFallback.deleteByIdObjectFallback(favouriteId, throwable);
	}
	
}
