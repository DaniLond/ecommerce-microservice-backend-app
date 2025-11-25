package com.selimhorri.app.business.product.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.selimhorri.app.business.product.model.ProductDto;
import com.selimhorri.app.business.product.model.response.ProductProductServiceCollectionDtoResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Fallback implementation for Product Service to provide graceful degradation
 * when the product service is unavailable or responding slowly.
 * 
 * This fallback provides cached or default responses to maintain partial functionality
 * during service disruptions.
 */
@Component
@Slf4j
public class ProductServiceFallback {
	
	/**
	 * Fallback for findAll operation.
	 * Returns empty collection when product service is unavailable.
	 */
	public ResponseEntity<ProductProductServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		log.warn("Product Service findAll fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ProductProductServiceCollectionDtoResponse.builder()
						.collection(Collections.emptyList())
						.build());
	}
	
	/**
	 * Fallback for findById operation.
	 * Returns null response when product service is unavailable.
	 */
	public ResponseEntity<ProductDto> findByIdFallback(String productId, Throwable throwable) {
		log.warn("Product Service findById fallback triggered for productId: {}. Reason: {}", 
				productId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(null);
	}
	
	/**
	 * Fallback for save operation.
	 * Returns the original DTO with error status when product service is unavailable.
	 */
	public ResponseEntity<ProductDto> saveFallback(ProductDto productDto, Throwable throwable) {
		log.warn("Product Service save fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(productDto);
	}
	
	/**
	 * Fallback for update operation (single parameter).
	 * Returns the original DTO with error status when product service is unavailable.
	 */
	public ResponseEntity<ProductDto> updateFallback(ProductDto productDto, Throwable throwable) {
		log.warn("Product Service update fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(productDto);
	}
	
	/**
	 * Fallback for update operation (with productId).
	 * Returns the original DTO with error status when product service is unavailable.
	 */
	public ResponseEntity<ProductDto> updateByIdFallback(String productId, ProductDto productDto, Throwable throwable) {
		log.warn("Product Service updateById fallback triggered for productId: {}. Reason: {}", 
				productId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(productDto);
	}
	
	/**
	 * Fallback for deleteById operation.
	 * Returns false when product service is unavailable.
	 */
	public ResponseEntity<Boolean> deleteByIdFallback(String productId, Throwable throwable) {
		log.warn("Product Service deleteById fallback triggered for productId: {}. Reason: {}", 
				productId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(false);
	}
	
}
