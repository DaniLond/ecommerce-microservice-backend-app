package com.selimhorri.app.resource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.selimhorri.app.config.SecurityConfig;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selimhorri.app.dto.PaymentDto;
import com.selimhorri.app.service.PaymentService;

@WebMvcTest(PaymentResource.class)
@Import(SecurityConfig.class)
@DisplayName("PaymentResource Tests")
class PaymentResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PaymentService paymentService;
	
	private PaymentDto paymentDto;
	
	@BeforeEach
	void setUp() {
		paymentDto = new PaymentDto();
		paymentDto.setPaymentId(1);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("GET /api/payments - Should return all payments")
	void testFindAll() throws Exception {
		when(paymentService.findAll()).thenReturn(Arrays.asList(paymentDto));
		
		mockMvc.perform(get("/api/payments"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("GET /api/payments/{id} - Should return payment")
	void testFindById() throws Exception {
		when(paymentService.findById(1)).thenReturn(paymentDto);
		
		mockMvc.perform(get("/api/payments/1"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("POST /api/payments - Should create payment")
	void testSave() throws Exception {
		when(paymentService.save(any(PaymentDto.class))).thenReturn(paymentDto);
		
		mockMvc.perform(post("/api/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(paymentDto)))
			.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("PUT /api/payments - Should update payment")
	void testUpdate() throws Exception {
		when(paymentService.update(any(PaymentDto.class))).thenReturn(paymentDto);
		
		mockMvc.perform(put("/api/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(paymentDto)))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("DELETE /api/payments/{id} - Should delete payment")
	void testDeleteById() throws Exception {
		doNothing().when(paymentService).deleteById(1);
		
		mockMvc.perform(delete("/api/payments/1"))
			.andExpect(status().isOk());
	}
}
