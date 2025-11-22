package com.selimhorri.app.resource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.exception.custom.ResourceNotFoundException;
import com.selimhorri.app.service.UserService;

@WebMvcTest(UserResource.class)
@DisplayName("UserResource Tests")
class UserResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService userService;
	
	private UserDto userDto;
	
	@BeforeEach
	void setUp() {
		userDto = new UserDto();
		userDto.setUserId(1);
		userDto.setFirstName("John");
		userDto.setLastName("Doe");
		userDto.setImageUrl("http://image.url");
		userDto.setEmail("john@example.com");
		userDto.setPhone("123456789");
	}
	
	@Test
	@DisplayName("GET /api/users - Should return all users")
	void testFindAll() throws Exception {
		// Given
		List<UserDto> users = Arrays.asList(userDto);
		when(userService.findAll()).thenReturn(users);
		
		// When & Then
		mockMvc.perform(get("/api/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.collection").isArray())
			.andExpect(jsonPath("$.collection[0].firstName").value("John"));
		
		verify(userService, times(1)).findAll();
	}
	
	@Test
	@DisplayName("GET /api/users/{userId} - Should return user by id")
	void testFindById() throws Exception {
		// Given
		when(userService.findById(1)).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(get("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(1))
			.andExpect(jsonPath("$.firstName").value("John"));
		
		verify(userService, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("GET /api/users/{userId} - Should return 400 for invalid id")
	void testFindByIdInvalid() throws Exception {
		// When & Then
		mockMvc.perform(get("/api/users/abc"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("GET /api/users/{userId} - Should return 404 when user not found")
	void testFindByIdNotFound() throws Exception {
		// Given
		when(userService.findById(999)).thenThrow(
			new ResourceNotFoundException("User with id: 999 not found")
		);
		
		// When & Then
		mockMvc.perform(get("/api/users/999"))
			.andExpect(status().isNotFound());
		
		verify(userService, times(1)).findById(999);
	}
	
	@Test
	@DisplayName("POST /api/users - Should create user successfully")
	void testSave() throws Exception {
		// Given
		when(userService.save(any(UserDto.class))).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("John"));
		
		verify(userService, times(1)).save(any(UserDto.class));
	}
	
	@Test
	@DisplayName("PUT /api/users - Should update user successfully")
	void testUpdate() throws Exception {
		// Given
		when(userService.update(any(UserDto.class))).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(put("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("John"));
		
		verify(userService, times(1)).update(any(UserDto.class));
	}
	
	@Test
	@DisplayName("PUT /api/users/{userId} - Should update user with id")
	void testUpdateWithId() throws Exception {
		// Given
		when(userService.update(eq(1), any(UserDto.class))).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(put("/api/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("John"));
		
		verify(userService, times(1)).update(eq(1), any(UserDto.class));
	}
	
	@Test
	@DisplayName("DELETE /api/users/{userId} - Should delete user successfully")
	void testDeleteById() throws Exception {
		// Given
		doNothing().when(userService).deleteById(1);
		
		// When & Then
		mockMvc.perform(delete("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(true));
		
		verify(userService, times(1)).deleteById(1);
	}
	
	@Test
	@DisplayName("GET /api/users/username/{username} - Should return user by username")
	void testFindByUsername() throws Exception {
		// Given
		when(userService.findByUsername("john_doe")).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(get("/api/users/username/john_doe"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("John"));
		
		verify(userService, times(1)).findByUsername("john_doe");
	}
	
	@Test
	@DisplayName("GET /api/users/username/{username} - Should return 404 when username not found")
	void testFindByUsernameNotFound() throws Exception {
		// Given
		when(userService.findByUsername("unknown_user")).thenThrow(
			new ResourceNotFoundException("User with username: unknown_user not found")
		);
		
		// When & Then
		mockMvc.perform(get("/api/users/username/unknown_user"))
			.andExpect(status().isNotFound());
		
		verify(userService, times(1)).findByUsername("unknown_user");
	}
	
	@Test
	@DisplayName("DELETE /api/users/{userId} - Should return 400 for invalid id format")
	void testDeleteByIdInvalidFormat() throws Exception {
		// When & Then
		mockMvc.perform(delete("/api/users/abc"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("PUT /api/users/{userId} - Should return 400 for invalid id format")
	void testUpdateWithInvalidIdFormat() throws Exception {
		// When & Then
		mockMvc.perform(put("/api/users/abc")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("POST /api/users - Should handle all user fields correctly")
	void testSaveWithAllFields() throws Exception {
		// Given
		UserDto completeUserDto = new UserDto();
		completeUserDto.setUserId(2);
		completeUserDto.setFirstName("Jane");
		completeUserDto.setLastName("Smith");
		completeUserDto.setImageUrl("http://image2.url");
		completeUserDto.setEmail("jane@example.com");
		completeUserDto.setPhone("987654321");
		
		when(userService.save(any(UserDto.class))).thenReturn(completeUserDto);
		
		// When & Then
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(completeUserDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.userId").value(2))
			.andExpect(jsonPath("$.firstName").value("Jane"))
			.andExpect(jsonPath("$.lastName").value("Smith"))
			.andExpect(jsonPath("$.email").value("jane@example.com"))
			.andExpect(jsonPath("$.phone").value("987654321"));
		
		verify(userService, times(1)).save(any(UserDto.class));
	}
	
	@Test
	@DisplayName("GET /api/users/{userId} - Should handle numeric id with spaces")
	void testFindByIdWithSpaces() throws Exception {
		// Given
		when(userService.findById(1)).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(get("/api/users/ 1 "))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(1));
		
		verify(userService, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("PUT /api/users/{userId} - Should handle numeric id with spaces")
	void testUpdateWithIdWithSpaces() throws Exception {
		// Given
		when(userService.update(eq(1), any(UserDto.class))).thenReturn(userDto);
		
		// When & Then
		mockMvc.perform(put("/api/users/ 1 ")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(1));
		
		verify(userService, times(1)).update(eq(1), any(UserDto.class));
	}
	
	@Test
	@DisplayName("GET /api/users - Should return empty list when no users exist")
	void testFindAllEmpty() throws Exception {
		// Given
		when(userService.findAll()).thenReturn(List.of());
		
		// When & Then
		mockMvc.perform(get("/api/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.collection").isArray())
			.andExpect(jsonPath("$.collection").isEmpty());
		
		verify(userService, times(1)).findAll();
	}
}
