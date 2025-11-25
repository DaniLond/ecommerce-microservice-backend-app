package com.selimhorri.app.business.product.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.selimhorri.app.business.product.model.ProductDto;
import com.selimhorri.app.business.product.model.response.ProductProductServiceCollectionDtoResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resilient wrapper service for Product Client Service.
 * 
 * This service wraps the Feign client with Resilience4j patterns:
 * - Circuit Breaker: Prevents cascading failures by stopping calls to failing services
 * - Retry: Automatically retries failed calls with exponential backoff
 * - Time Limiter: Ensures calls don't hang indefinitely
 * - Bulkhead: Limits concurrent calls to prevent resource exhaustion
 * 
 * All methods have fallback implementations that provide graceful degradation
 * when the product service is unavailable.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientProductService {
	
	private final ProductClientService productClientService;
	private final ProductServiceFallback productServiceFallback;
	
	/**
	 * Find all products with resilience patterns.
	 * Circuit breaker opens after 50% failure rate over 10 calls.
	 * Retries up to 3 times with exponential backoff.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "findAllFallback")
	@Retry(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<ProductProductServiceCollectionDtoResponse> findAll() {
		log.debug("Calling Product Service - findAll");
		return productClientService.findAll();
	}
	
	/**
	 * Find product by ID with resilience patterns.
	 * Times out after 3 seconds to prevent hanging calls.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "findByIdFallback")
	@Retry(name = "productService")
	@TimeLimiter(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<ProductDto> findById(String productId) {
		log.debug("Calling Product Service - findById: {}", productId);
		return productClientService.findById(productId);
	}
	
	/**
	 * Save product with resilience patterns.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "saveFallback")
	@Retry(name = "productService")
	@TimeLimiter(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<ProductDto> save(ProductDto productDto) {
		log.debug("Calling Product Service - save");
		return productClientService.save(productDto);
	}
	
	/**
	 * Update product with resilience patterns.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "updateFallback")
	@Retry(name = "productService")
	@TimeLimiter(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<ProductDto> update(ProductDto productDto) {
		log.debug("Calling Product Service - update");
		return productClientService.update(productDto);
	}
	
	/**
	 * Update product by ID with resilience patterns.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "updateByIdFallback")
	@Retry(name = "productService")
	@TimeLimiter(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<ProductDto> update(String productId, ProductDto productDto) {
		log.debug("Calling Product Service - updateById: {}", productId);
		return productClientService.update(productId, productDto);
	}
	
	/**
	 * Delete product by ID with resilience patterns.
	 */
	@CircuitBreaker(name = "productService", fallbackMethod = "deleteByIdFallback")
	@Retry(name = "productService")
	@TimeLimiter(name = "productService")
	@Bulkhead(name = "productService")
	public ResponseEntity<Boolean> deleteById(String productId) {
		log.debug("Calling Product Service - deleteById: {}", productId);
		return productClientService.deleteById(productId);
	}
	
	// Fallback methods delegate to the fallback component
	
	private ResponseEntity<ProductProductServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		return productServiceFallback.findAllFallback(throwable);
	}
	
	private ResponseEntity<ProductDto> findByIdFallback(String productId, Throwable throwable) {
		return productServiceFallback.findByIdFallback(productId, throwable);
	}
	
	private ResponseEntity<ProductDto> saveFallback(ProductDto productDto, Throwable throwable) {
		return productServiceFallback.saveFallback(productDto, throwable);
	}
	
	private ResponseEntity<ProductDto> updateFallback(ProductDto productDto, Throwable throwable) {
		return productServiceFallback.updateFallback(productDto, throwable);
	}
	
	private ResponseEntity<ProductDto> updateByIdFallback(String productId, ProductDto productDto, Throwable throwable) {
		return productServiceFallback.updateByIdFallback(productId, productDto, throwable);
	}
	
	private ResponseEntity<Boolean> deleteByIdFallback(String productId, Throwable throwable) {
		return productServiceFallback.deleteByIdFallback(productId, throwable);
	}
	
}
