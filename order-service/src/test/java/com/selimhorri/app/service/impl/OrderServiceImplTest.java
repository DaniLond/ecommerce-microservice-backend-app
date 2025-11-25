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

import com.selimhorri.app.domain.Cart;
import com.selimhorri.app.domain.Order;
import com.selimhorri.app.dto.CartDto;
import com.selimhorri.app.dto.OrderDto;
import com.selimhorri.app.exception.wrapper.OrderNotFoundException;
import com.selimhorri.app.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceImplTest {
	
	@Mock
	private OrderRepository orderRepository;
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	private Order order;
	private OrderDto orderDto;
	
	@BeforeEach
	void setUp() {
		// Create Cart
		Cart cart = new Cart();
		cart.setCartId(1);
		
		order = new Order();
		order.setOrderId(1);
		order.setCart(cart);
		
		// Create CartDto
		CartDto cartDto = new CartDto();
		cartDto.setCartId(1);
		
		orderDto = new OrderDto();
		orderDto.setOrderId(1);
		orderDto.setCartDto(cartDto);
	}
	
	@Test
	@DisplayName("Should find all orders successfully")
	void testFindAll() {
		// Given
		when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
		
		// When
		List<OrderDto> result = orderService.findAll();
		
		// Then
		assertNotNull(result);
		verify(orderRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find order by id successfully")
	void testFindById() {
		// Given
		when(orderRepository.findById(1)).thenReturn(Optional.of(order));
		
		// When
		OrderDto result = orderService.findById(1);
		
		// Then
		assertNotNull(result);
		verify(orderRepository, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("Should throw exception when order not found")
	void testFindByIdNotFound() {
		// Given
		when(orderRepository.findById(999)).thenReturn(Optional.empty());
		
		// When & Then
		assertThrows(OrderNotFoundException.class, () -> orderService.findById(999));
	}
	
	@Test
	@DisplayName("Should save order successfully")
	void testSave() {
		// Given
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		
		// When
		OrderDto result = orderService.save(orderDto);
		
		// Then
		assertNotNull(result);
		verify(orderRepository, times(1)).save(any(Order.class));
	}
	
	@Test
	@DisplayName("Should update order successfully")
	void testUpdate() {
		// Given
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		
		// When
		OrderDto result = orderService.update(orderDto);
		
		// Then
		assertNotNull(result);
		verify(orderRepository, times(1)).save(any(Order.class));
	}
	
	@Test
	@DisplayName("Should delete order by id successfully")
	void testDeleteById() {
		// Given
		when(orderRepository.findById(1)).thenReturn(Optional.of(order));
		doNothing().when(orderRepository).delete(any(Order.class));
		
		// When
		orderService.deleteById(1);
		
		// Then
		verify(orderRepository, times(1)).findById(1);
		verify(orderRepository, times(1)).delete(order);
	}
}
