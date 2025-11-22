package com.selimhorri.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.CredentialDto;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.exception.custom.ResourceNotFoundException;
import com.selimhorri.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	private User user;
	private UserDto userDto;
	
	@BeforeEach
	void setUp() {
		// Create Credential
		Credential credential = new Credential();
		credential.setCredentialId(1);
		credential.setUsername("johndoe");
		credential.setPassword("password123");
		credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
		credential.setIsEnabled(true);
		credential.setIsAccountNonExpired(true);
		credential.setIsAccountNonLocked(true);
		credential.setIsCredentialsNonExpired(true);
		
		user = new User();
		user.setUserId(1);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setImageUrl("http://image.url");
		user.setEmail("john@example.com");
		user.setPhone("123456789");
		user.setCredential(credential);
		
		// Create CredentialDto
		CredentialDto credentialDto = new CredentialDto();
		credentialDto.setCredentialId(1);
		credentialDto.setUsername("johndoe");
		credentialDto.setPassword("password123");
		credentialDto.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
		credentialDto.setIsEnabled(true);
		credentialDto.setIsAccountNonExpired(true);
		credentialDto.setIsAccountNonLocked(true);
		credentialDto.setIsCredentialsNonExpired(true);
		
		userDto = new UserDto();
		userDto.setUserId(1);
		userDto.setFirstName("John");
		userDto.setLastName("Doe");
		userDto.setImageUrl("http://image.url");
		userDto.setEmail("john@example.com");
		userDto.setPhone("123456789");
		userDto.setCredentialDto(credentialDto);
	}
	
	@Test
	@DisplayName("Should find all users successfully")
	void testFindAll() {
		// Given
		Credential credential2 = new Credential();
		credential2.setCredentialId(2);
		credential2.setUsername("janesmith");
		credential2.setPassword("password456");
		credential2.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
		credential2.setIsEnabled(true);
		credential2.setIsAccountNonExpired(true);
		credential2.setIsAccountNonLocked(true);
		credential2.setIsCredentialsNonExpired(true);
		
		User user2 = new User();
		user2.setUserId(2);
		user2.setFirstName("Jane");
		user2.setLastName("Smith");
		user2.setCredential(credential2);
		
		when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));
		
		// When
		List<UserDto> result = userService.findAll();
		
		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(userRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find user by id successfully")
	void testFindById() {
		// Given
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		// When
		UserDto result = userService.findById(1);
		
		// Then
		assertNotNull(result);
		assertEquals(1, result.getUserId());
		assertEquals("John", result.getFirstName());
		verify(userRepository, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("Should throw exception when user not found by id")
	void testFindByIdNotFound() {
		// Given
		when(userRepository.findById(999)).thenReturn(Optional.empty());
		
		// When & Then
		ResourceNotFoundException exception = assertThrows(
			ResourceNotFoundException.class,
			() -> userService.findById(999)
		);
		
		assertTrue(exception.getMessage().contains("User with id: 999 not found"));
		verify(userRepository, times(1)).findById(999);
	}
	
	@Test
	@DisplayName("Should save user successfully")
	void testSave() {
		// Given
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// When
		UserDto result = userService.save(userDto);
		
		// Then
		assertNotNull(result);
		assertEquals("John", result.getFirstName());
		verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	@DisplayName("Should update user successfully")
	void testUpdate() {
		// Given
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// When
		UserDto result = userService.update(userDto);
		
		// Then
		assertNotNull(result);
		assertEquals("John", result.getFirstName());
		verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	@DisplayName("Should update user with userId successfully")
	void testUpdateWithUserId() {
		// Given
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// When
		UserDto result = userService.update(1, userDto);
		
		// Then
		assertNotNull(result);
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	@DisplayName("Should delete user by id successfully")
	void testDeleteById() {
		// Given
		doNothing().when(userRepository).deleteById(1);
		
		// When
		userService.deleteById(1);
		
		// Then
		verify(userRepository, times(1)).deleteById(1);
	}
	
	@Test
	@DisplayName("Should find user by username successfully")
	void testFindByUsername() {
		// Given
		when(userRepository.findByCredentialUsername("john_doe")).thenReturn(Optional.of(user));
		
		// When
		UserDto result = userService.findByUsername("john_doe");
		
		// Then
		assertNotNull(result);
		assertEquals("John", result.getFirstName());
		verify(userRepository, times(1)).findByCredentialUsername("john_doe");
	}
	
	@Test
	@DisplayName("Should throw exception when user not found by username")
	void testFindByUsernameNotFound() {
		// Given
		when(userRepository.findByCredentialUsername("unknown")).thenReturn(Optional.empty());
		
		// When & Then
		ResourceNotFoundException exception = assertThrows(
			ResourceNotFoundException.class,
			() -> userService.findByUsername("unknown")
		);
		
		assertTrue(exception.getMessage().contains("User with username: unknown not found"));
		verify(userRepository, times(1)).findByCredentialUsername("unknown");
	}
}
