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
	@DisplayName("GET /api/orders - Should return all orders")
	void testFindAll() throws Exception {
		when(orderService.findAll()).thenReturn(Arrays.asList(orderDto));
		
		mockMvc.perform(get("/api/orders"))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).findAll();
	}
	
	@Test
	@DisplayName("GET /api/orders/{id} - Should return order")
	void testFindById() throws Exception {
		when(orderService.findById(1)).thenReturn(orderDto);
		
		mockMvc.perform(get("/api/orders/1"))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("POST /api/orders - Should create order")
	void testSave() throws Exception {
		when(orderService.save(any(OrderDto.class))).thenReturn(orderDto);
		
		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDto)))
			.andExpect(status().isCreated());
		
		verify(orderService, times(1)).save(any(OrderDto.class));
	}
	
	@Test
	@DisplayName("PUT /api/orders - Should update order")
	void testUpdate() throws Exception {
		when(orderService.update(any(OrderDto.class))).thenReturn(orderDto);
		
		mockMvc.perform(put("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDto)))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).update(any(OrderDto.class));
	}
	
	@Test
	@DisplayName("DELETE /api/orders/{id} - Should delete order")
	void testDeleteById() throws Exception {
		doNothing().when(orderService).deleteById(1);
		
		mockMvc.perform(delete("/api/orders/1"))
			.andExpect(status().isOk());
		
		verify(orderService, times(1)).deleteById(1);
	}
}
