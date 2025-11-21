package com.selimhorri.app.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.CredentialDto;
import com.selimhorri.app.dto.UserDto;

@DisplayName("UserMappingHelper Tests")
class UserMappingHelperTest {
	
	private User user;
	private UserDto userDto;
	private Credential credential;
	private CredentialDto credentialDto;
	
	@BeforeEach
	void setUp() {
		// Create Credential
		credential = new Credential();
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
		credentialDto = new CredentialDto();
		credentialDto.setCredentialId(1);
		credentialDto.setUsername("johndoe");
		credentialDto.setPassword("password123");
		credentialDto.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
		credentialDto.setIsEnabled(true);
		credentialDto.setIsAccountNonExpired(true);
		credentialDto.setIsAccountNonLocked(true);
		credentialDto.setIsCredentialsNonExpired(true);
		
		// Create UserDto
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
	@DisplayName("Should map User to UserDto successfully")
	void testMapUserToDto() {
		// When
		UserDto result = UserMappingHelper.map(user);
		
		// Then
		assertNotNull(result);
		assertEquals(user.getUserId(), result.getUserId());
		assertEquals(user.getFirstName(), result.getFirstName());
		assertEquals(user.getLastName(), result.getLastName());
		assertEquals(user.getImageUrl(), result.getImageUrl());
		assertEquals(user.getEmail(), result.getEmail());
		assertEquals(user.getPhone(), result.getPhone());
		
		// Verify Credential mapping
		assertNotNull(result.getCredentialDto());
		assertEquals(credential.getCredentialId(), result.getCredentialDto().getCredentialId());
		assertEquals(credential.getUsername(), result.getCredentialDto().getUsername());
		assertEquals(credential.getPassword(), result.getCredentialDto().getPassword());
		assertEquals(credential.getRoleBasedAuthority(), result.getCredentialDto().getRoleBasedAuthority());
		assertEquals(credential.getIsEnabled(), result.getCredentialDto().getIsEnabled());
		assertEquals(credential.getIsAccountNonExpired(), result.getCredentialDto().getIsAccountNonExpired());
		assertEquals(credential.getIsAccountNonLocked(), result.getCredentialDto().getIsAccountNonLocked());
		assertEquals(credential.getIsCredentialsNonExpired(), result.getCredentialDto().getIsCredentialsNonExpired());
	}
	
	@Test
	@DisplayName("Should map UserDto to User successfully")
	void testMapDtoToUser() {
		// When
		User result = UserMappingHelper.map(userDto);
		
		// Then
		assertNotNull(result);
		assertEquals(userDto.getUserId(), result.getUserId());
		assertEquals(userDto.getFirstName(), result.getFirstName());
		assertEquals(userDto.getLastName(), result.getLastName());
		assertEquals(userDto.getImageUrl(), result.getImageUrl());
		assertEquals(userDto.getEmail(), result.getEmail());
		assertEquals(userDto.getPhone(), result.getPhone());
		
		// Verify Credential mapping
		assertNotNull(result.getCredential());
		assertEquals(credentialDto.getCredentialId(), result.getCredential().getCredentialId());
		assertEquals(credentialDto.getUsername(), result.getCredential().getUsername());
		assertEquals(credentialDto.getPassword(), result.getCredential().getPassword());
		assertEquals(credentialDto.getRoleBasedAuthority(), result.getCredential().getRoleBasedAuthority());
		assertEquals(credentialDto.getIsEnabled(), result.getCredential().getIsEnabled());
		assertEquals(credentialDto.getIsAccountNonExpired(), result.getCredential().getIsAccountNonExpired());
		assertEquals(credentialDto.getIsAccountNonLocked(), result.getCredential().getIsAccountNonLocked());
		assertEquals(credentialDto.getIsCredentialsNonExpired(), result.getCredential().getIsCredentialsNonExpired());
	}
	
	@Test
	@DisplayName("Should handle User with all fields populated")
	void testMapUserWithAllFields() {
		// Given - user with all fields set
		user.setFirstName("Jane");
		user.setLastName("Smith");
		user.setEmail("jane.smith@example.com");
		user.setPhone("+1234567890");
		user.setImageUrl("https://example.com/image.png");
		
		// When
		UserDto result = UserMappingHelper.map(user);
		
		// Then
		assertNotNull(result);
		assertEquals("Jane", result.getFirstName());
		assertEquals("Smith", result.getLastName());
		assertEquals("jane.smith@example.com", result.getEmail());
		assertEquals("+1234567890", result.getPhone());
		assertEquals("https://example.com/image.png", result.getImageUrl());
	}
	
	@Test
	@DisplayName("Should handle UserDto with all fields populated")
	void testMapDtoWithAllFields() {
		// Given - userDto with all fields set
		userDto.setFirstName("Jane");
		userDto.setLastName("Smith");
		userDto.setEmail("jane.smith@example.com");
		userDto.setPhone("+1234567890");
		userDto.setImageUrl("https://example.com/image.png");
		
		// When
		User result = UserMappingHelper.map(userDto);
		
		// Then
		assertNotNull(result);
		assertEquals("Jane", result.getFirstName());
		assertEquals("Smith", result.getLastName());
		assertEquals("jane.smith@example.com", result.getEmail());
		assertEquals("+1234567890", result.getPhone());
		assertEquals("https://example.com/image.png", result.getImageUrl());
	}
	
	@Test
	@DisplayName("Should preserve credential role authority in mapping")
	void testPreserveRoleAuthority() {
		// Given
		credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN);
		
		// When
		UserDto result = UserMappingHelper.map(user);
		
		// Then
		assertEquals(RoleBasedAuthority.ROLE_ADMIN, result.getCredentialDto().getRoleBasedAuthority());
	}
	
	@Test
	@DisplayName("Should preserve credential boolean flags in User to DTO mapping")
	void testPreserveBooleanFlags() {
		// Given
		credential.setIsEnabled(false);
		credential.setIsAccountNonExpired(false);
		credential.setIsAccountNonLocked(false);
		credential.setIsCredentialsNonExpired(false);
		
		// When
		UserDto result = UserMappingHelper.map(user);
		
		// Then
		assertEquals(false, result.getCredentialDto().getIsEnabled());
		assertEquals(false, result.getCredentialDto().getIsAccountNonExpired());
		assertEquals(false, result.getCredentialDto().getIsAccountNonLocked());
		assertEquals(false, result.getCredentialDto().getIsCredentialsNonExpired());
	}
	
	@Test
	@DisplayName("Should preserve credential boolean flags in DTO to User mapping")
	void testPreserveBooleanFlagsInDtoToUser() {
		// Given
		credentialDto.setIsEnabled(false);
		credentialDto.setIsAccountNonExpired(false);
		credentialDto.setIsAccountNonLocked(false);
		credentialDto.setIsCredentialsNonExpired(false);
		
		// When
		User result = UserMappingHelper.map(userDto);
		
		// Then
		assertEquals(false, result.getCredential().getIsEnabled());
		assertEquals(false, result.getCredential().getIsAccountNonExpired());
		assertEquals(false, result.getCredential().getIsAccountNonLocked());
		assertEquals(false, result.getCredential().getIsCredentialsNonExpired());
	}
}
