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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selimhorri.app.dto.OrderDto;
import com.selimhorri.app.exception.wrapper.OrderNotFoundException;
import com.selimhorri.app.service.OrderService;

@WebMvcTest(OrderResource.class)
@DisplayName("OrderResource Tests")
class OrderResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private OrderService orderService;
	
	private OrderDto orderDto;
	
	@BeforeEach
	void setUp() {
		orderDto = new OrderDto();
		orderDto.setOrderId(1);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("GET /api/orders - Should return all orders")
	void testFindAll() throws Exception {
		when(orderService.findAll()).thenReturn(Arrays.asList(orderDto));
		
		mockMvc.perform(get("/api/orders"))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).findAll();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("GET /api/orders/{id} - Should return order")
	void testFindById() throws Exception {
		when(orderService.findById(1)).thenReturn(orderDto);
		
		mockMvc.perform(get("/api/orders/1"))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).findById(1);
	}
	
}
