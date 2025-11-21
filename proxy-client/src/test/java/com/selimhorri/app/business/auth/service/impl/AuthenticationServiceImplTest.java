package com.selimhorri.app.business.auth.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.selimhorri.app.business.auth.model.request.AuthenticationRequest;
import com.selimhorri.app.business.auth.model.response.AuthenticationResponse;
import com.selimhorri.app.exception.wrapper.IllegalAuthenticationCredentialsException;
import com.selimhorri.app.jwt.service.JwtService;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceImplTest {
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private UserDetailsService userDetailsService;
	
	@Mock
	private JwtService jwtService;
	
	@InjectMocks
	private AuthenticationServiceImpl authenticationService;
	
	private AuthenticationRequest authenticationRequest;
	private UserDetails userDetails;
	private String generatedToken;
	
	@BeforeEach
	void setUp() {
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("testuser");
		authenticationRequest.setPassword("password123");
		
		userDetails = User.builder()
				.username("testuser")
				.password("password123")
				.authorities("ROLE_USER")
				.build();
		
		generatedToken = "generated.jwt.token";
	}
	
	@Test
	@DisplayName("Should authenticate user successfully and return JWT token")
	void testAuthenticateSuccess() {
		// Given
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(null);
		when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
		when(jwtService.generateToken(userDetails)).thenReturn(generatedToken);
		
		// When
		AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
		
		// Then
		assertNotNull(response);
		assertEquals(generatedToken, response.getJwtToken());
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userDetailsService, times(1)).loadUserByUsername("testuser");
		verify(jwtService, times(1)).generateToken(userDetails);
	}
	
	@Test
	@DisplayName("Should throw IllegalAuthenticationCredentialsException when credentials are invalid")
	void testAuthenticateWithBadCredentials() {
		// Given
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));
		
		// When & Then
		IllegalAuthenticationCredentialsException exception = assertThrows(
			IllegalAuthenticationCredentialsException.class,
			() -> authenticationService.authenticate(authenticationRequest)
		);
		
		assertTrue(exception.getMessage().contains("Bad credentials"));
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userDetailsService, never()).loadUserByUsername(anyString());
		verify(jwtService, never()).generateToken(any(UserDetails.class));
	}
	
	@Test
	@DisplayName("Should handle authentication with empty username")
	void testAuthenticateWithEmptyUsername() {
		// Given
		authenticationRequest.setUsername("");
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));
		
		// When & Then
		assertThrows(
			IllegalAuthenticationCredentialsException.class,
			() -> authenticationService.authenticate(authenticationRequest)
		);
		
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}
	
	@Test
	@DisplayName("Should handle authentication with empty password")
	void testAuthenticateWithEmptyPassword() {
		// Given
		authenticationRequest.setPassword("");
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));
		
		// When & Then
		assertThrows(
			IllegalAuthenticationCredentialsException.class,
			() -> authenticationService.authenticate(authenticationRequest)
		);
		
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}
	
	@Test
	@DisplayName("Should authenticate with different user roles")
	void testAuthenticateWithAdminRole() {
		// Given
		AuthenticationRequest adminRequest = new AuthenticationRequest();
		adminRequest.setUsername("admin");
		adminRequest.setPassword("adminpass");
		
		UserDetails adminUser = User.builder()
				.username("admin")
				.password("adminpass")
				.authorities("ROLE_ADMIN")
				.build();
		
		String adminToken = "admin.jwt.token";
		
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(null);
		when(userDetailsService.loadUserByUsername("admin")).thenReturn(adminUser);
		when(jwtService.generateToken(adminUser)).thenReturn(adminToken);
		
		// When
		AuthenticationResponse response = authenticationService.authenticate(adminRequest);
		
		// Then
		assertNotNull(response);
		assertEquals(adminToken, response.getJwtToken());
		verify(userDetailsService, times(1)).loadUserByUsername("admin");
	}
	
	@Test
	@DisplayName("Should return null for JWT authentication (not implemented)")
	void testAuthenticateJwt() {
		// Given
		String jwt = "some.jwt.token";
		
		// When
		Boolean result = authenticationService.authenticate(jwt);
		
		// Then
		assertNull(result);
	}
}
