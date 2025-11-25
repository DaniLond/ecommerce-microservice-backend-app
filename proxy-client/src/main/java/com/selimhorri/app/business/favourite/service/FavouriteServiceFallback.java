package com.selimhorri.app.business.favourite.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.selimhorri.app.business.favourite.model.FavouriteDto;
import com.selimhorri.app.business.favourite.model.FavouriteId;
import com.selimhorri.app.business.favourite.model.response.FavouriteFavouriteServiceCollectionDtoResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Fallback implementation for Favourite Service to provide graceful degradation
 * when the favourite service is unavailable or responding slowly.
 * 
 * This fallback provides cached or default responses to maintain partial functionality
 * during service disruptions.
 */
@Component
@Slf4j
public class FavouriteServiceFallback {
	
	/**
	 * Fallback for findAll operation.
	 * Returns empty collection when favourite service is unavailable.
	 */
	public ResponseEntity<FavouriteFavouriteServiceCollectionDtoResponse> findAllFallback(Throwable throwable) {
		log.warn("Favourite Service findAll fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(FavouriteFavouriteServiceCollectionDtoResponse.builder()
						.collection(Collections.emptyList())
						.build());
	}
	
	/**
	 * Fallback for findById operation (with path variables).
	 * Returns null response when favourite service is unavailable.
	 */
	public ResponseEntity<FavouriteDto> findByIdFallback(String userId, String productId, String likeDate, Throwable throwable) {
		log.warn("Favourite Service findById fallback triggered for userId: {}, productId: {}, likeDate: {}. Reason: {}", 
				userId, productId, likeDate, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(null);
	}
	
	/**
	 * Fallback for findById operation (with FavouriteId).
	 * Returns null response when favourite service is unavailable.
	 */
	public ResponseEntity<FavouriteDto> findByIdObjectFallback(FavouriteId favouriteId, Throwable throwable) {
		log.warn("Favourite Service findById fallback triggered for favouriteId: {}. Reason: {}", 
				favouriteId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(null);
	}
	
	/**
	 * Fallback for save operation.
	 * Returns the original DTO with error status when favourite service is unavailable.
	 */
	public ResponseEntity<FavouriteDto> saveFallback(FavouriteDto favouriteDto, Throwable throwable) {
		log.warn("Favourite Service save fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(favouriteDto);
	}
	
	/**
	 * Fallback for update operation.
	 * Returns the original DTO with error status when favourite service is unavailable.
	 */
	public ResponseEntity<FavouriteDto> updateFallback(FavouriteDto favouriteDto, Throwable throwable) {
		log.warn("Favourite Service update fallback triggered. Reason: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(favouriteDto);
	}
	
	/**
	 * Fallback for deleteById operation (with path variables).
	 * Returns false when favourite service is unavailable.
	 */
	public ResponseEntity<Boolean> deleteByIdFallback(String userId, String productId, String likeDate, Throwable throwable) {
		log.warn("Favourite Service deleteById fallback triggered for userId: {}, productId: {}, likeDate: {}. Reason: {}", 
				userId, productId, likeDate, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(false);
	}
	
	/**
	 * Fallback for deleteById operation (with FavouriteId).
	 * Returns false when favourite service is unavailable.
	 */
	public ResponseEntity<Boolean> deleteByIdObjectFallback(FavouriteId favouriteId, Throwable throwable) {
		log.warn("Favourite Service deleteById fallback triggered for favouriteId: {}. Reason: {}", 
				favouriteId, throwable.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(false);
	}
	
}
