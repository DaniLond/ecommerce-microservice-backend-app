package com.selimhorri.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;
import com.selimhorri.app.domain.OrderItem;
import com.selimhorri.app.domain.id.OrderItemId;
import com.selimhorri.app.dto.OrderDto;
import com.selimhorri.app.dto.OrderItemDto;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.exception.wrapper.OrderItemNotFoundException;
import com.selimhorri.app.repository.OrderItemRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderItemService Tests")
class OrderItemServiceImplTest {
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private OrderItemServiceImpl orderItemService;
	
	private OrderItem orderItem;
	private OrderItemDto orderItemDto;
	private OrderItemId orderItemId;
	
	@BeforeEach
	void setUp() {
		orderItemId = new OrderItemId(1, 1);
		
		orderItem = new OrderItem();
		orderItem.setOrderId(1);
		orderItem.setProductId(1);
		orderItem.setOrderedQuantity(5);
		
		orderItemDto = new OrderItemDto();
		orderItemDto.setOrderId(1);
		orderItemDto.setProductId(1);
		orderItemDto.setOrderedQuantity(5);
	}
	
	@Test
	@DisplayName("Should find all order items")
	void testFindAll() {
		when(orderItemRepository.findAll()).thenReturn(Arrays.asList(orderItem));
		when(restTemplate.getForObject(anyString(), eq(ProductDto.class))).thenReturn(new ProductDto());
		when(restTemplate.getForObject(anyString(), eq(OrderDto.class))).thenReturn(new OrderDto());
		
		assertNotNull(orderItemService.findAll());
		verify(orderItemRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find order item by id")
	void testFindById() {
		when(orderItemRepository.findById(any(OrderItemId.class))).thenReturn(Optional.of(orderItem));
		when(restTemplate.getForObject(anyString(), eq(ProductDto.class))).thenReturn(new ProductDto());
		when(restTemplate.getForObject(anyString(), eq(OrderDto.class))).thenReturn(new OrderDto());
		
		OrderItemDto result = orderItemService.findById(orderItemId);
		
		assertNotNull(result);
		verify(orderItemRepository, times(1)).findById(any(OrderItemId.class));
	}
	
	@Test
	@DisplayName("Should throw exception when order item not found")
	void testFindByIdNotFound() {
		when(orderItemRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThrows(OrderItemNotFoundException.class, () -> orderItemService.findById(orderItemId));
	}
	
	@Test
	@DisplayName("Should save order item")
	void testSave() {
		when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
		
		OrderItemDto result = orderItemService.save(orderItemDto);
		
		assertNotNull(result);
		verify(orderItemRepository, times(1)).save(any(OrderItem.class));
	}
	
	@Test
	@DisplayName("Should update order item")
	void testUpdate() {
		when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
		
		OrderItemDto result = orderItemService.update(orderItemDto);
		
		assertNotNull(result);
		verify(orderItemRepository, times(1)).save(any(OrderItem.class));
	}
	
	@Test
	@DisplayName("Should delete order item")
	void testDeleteById() {
		doNothing().when(orderItemRepository).deleteById(orderItemId);
		
		orderItemService.deleteById(orderItemId);
		
		verify(orderItemRepository, times(1)).deleteById(orderItemId);
	}
}
