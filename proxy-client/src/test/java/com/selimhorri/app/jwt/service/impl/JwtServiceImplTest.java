package com.selimhorri.app.jwt.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.selimhorri.app.jwt.util.JwtUtil;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceImplTest {
	
	@Mock
	private JwtUtil jwtUtil;
	
	@InjectMocks
	private JwtServiceImpl jwtService;
	
	private String token;
	private UserDetails userDetails;
	private Date expirationDate;
	
	@BeforeEach
	void setUp() {
		token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
		userDetails = User.builder()
				.username("testuser")
				.password("password")
				.authorities("ROLE_USER")
				.build();
		expirationDate = new Date(System.currentTimeMillis() + 3600000);
	}
	
	@Test
	@DisplayName("Should extract username from token successfully")
	void testExtractUsername() {
		// Given
		when(jwtUtil.extractUsername(token)).thenReturn("testuser");
		
		// When
		String username = jwtService.extractUsername(token);
		
		// Then
		assertNotNull(username);
		assertEquals("testuser", username);
		verify(jwtUtil, times(1)).extractUsername(token);
	}
	
	@Test
	@DisplayName("Should extract expiration date from token successfully")
	void testExtractExpiration() {
		// Given
		when(jwtUtil.extractExpiration(token)).thenReturn(expirationDate);
		
		// When
		Date result = jwtService.extractExpiration(token);
		
		// Then
		assertNotNull(result);
		assertEquals(expirationDate, result);
		verify(jwtUtil, times(1)).extractExpiration(token);
	}
	
	@Test
	@DisplayName("Should extract claims with resolver function")
	void testExtractClaims() {
		// Given
		Function<Claims, String> claimsResolver = Claims::getSubject;
		when(jwtUtil.extractClaims(eq(token), any())).thenReturn("testuser");
		
		// When
		String result = jwtService.extractClaims(token, claimsResolver);
		
		// Then
		assertNotNull(result);
		assertEquals("testuser", result);
		verify(jwtUtil, times(1)).extractClaims(eq(token), any());
	}
	
	@Test
	@DisplayName("Should generate token from userDetails successfully")
	void testGenerateToken() {
		// Given
		String generatedToken = "generated.jwt.token";
		when(jwtUtil.generateToken(userDetails)).thenReturn(generatedToken);
		
		// When
		String result = jwtService.generateToken(userDetails);
		
		// Then
		assertNotNull(result);
		assertEquals(generatedToken, result);
		verify(jwtUtil, times(1)).generateToken(userDetails);
	}
	
	@Test
	@DisplayName("Should validate token successfully when valid")
	void testValidateTokenValid() {
		// Given
		when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);
		
		// When
		Boolean isValid = jwtService.validateToken(token, userDetails);
		
		// Then
		assertNotNull(isValid);
		assertTrue(isValid);
		verify(jwtUtil, times(1)).validateToken(token, userDetails);
	}
	
	@Test
	@DisplayName("Should return false when token is invalid")
	void testValidateTokenInvalid() {
		// Given
		when(jwtUtil.validateToken(token, userDetails)).thenReturn(false);
		
		// When
		Boolean isValid = jwtService.validateToken(token, userDetails);
		
		// Then
		assertNotNull(isValid);
		assertFalse(isValid);
		verify(jwtUtil, times(1)).validateToken(token, userDetails);
	}
	
	@Test
	@DisplayName("Should handle null token in extractUsername")
	void testExtractUsernameWithNullToken() {
		// Given
		when(jwtUtil.extractUsername(null)).thenReturn(null);
		
		// When
		String username = jwtService.extractUsername(null);
		
		// Then
		assertNull(username);
		verify(jwtUtil, times(1)).extractUsername(null);
	}
	
	@Test
	@DisplayName("Should handle different user details")
	void testGenerateTokenWithDifferentUsers() {
		// Given
		UserDetails adminUser = User.builder()
				.username("admin")
				.password("adminpass")
				.authorities("ROLE_ADMIN")
				.build();
		String adminToken = "admin.jwt.token";
		when(jwtUtil.generateToken(adminUser)).thenReturn(adminToken);
		
		// When
		String result = jwtService.generateToken(adminUser);
		
		// Then
		assertNotNull(result);
		assertEquals(adminToken, result);
		verify(jwtUtil, times(1)).generateToken(adminUser);
	}
}
