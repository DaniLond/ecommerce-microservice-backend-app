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
import com.selimhorri.app.domain.Payment;
import com.selimhorri.app.dto.OrderDto;
import com.selimhorri.app.dto.PaymentDto;
import com.selimhorri.app.exception.wrapper.PaymentNotFoundException;
import com.selimhorri.app.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService Tests")
class PaymentServiceImplTest {
	
	@Mock
	private PaymentRepository paymentRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private PaymentServiceImpl paymentService;
	
	private Payment payment;
	private PaymentDto paymentDto;
	private OrderDto orderDto;
	
	@BeforeEach
	void setUp() {
		payment = new Payment();
		payment.setPaymentId(1);
		payment.setOrderId(1);
		
		// Create OrderDto
		orderDto = new OrderDto();
		orderDto.setOrderId(1);
		
		paymentDto = new PaymentDto();
		paymentDto.setPaymentId(1);
		paymentDto.setOrderDto(orderDto);
	}
	
	@Test
	@DisplayName("Should find all payments successfully")
	void testFindAll() {
		when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment));
		when(restTemplate.getForObject(anyString(), eq(OrderDto.class))).thenReturn(orderDto);
		
		assertNotNull(paymentService.findAll());
		verify(paymentRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find payment by id")
	void testFindById() {
		when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
		when(restTemplate.getForObject(anyString(), eq(OrderDto.class))).thenReturn(orderDto);
		
		PaymentDto result = paymentService.findById(1);
		
		assertNotNull(result);
		verify(paymentRepository, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("Should throw exception when payment not found")
	void testFindByIdNotFound() {
		when(paymentRepository.findById(999)).thenReturn(Optional.empty());
		
		assertThrows(PaymentNotFoundException.class, () -> paymentService.findById(999));
	}
	
	@Test
	@DisplayName("Should save payment")
	void testSave() {
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
		
		PaymentDto result = paymentService.save(paymentDto);
		
		assertNotNull(result);
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}
	
	@Test
	@DisplayName("Should update payment")
	void testUpdate() {
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
		
		PaymentDto result = paymentService.update(paymentDto);
		
		assertNotNull(result);
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}
	
	@Test
	@DisplayName("Should delete payment")
	void testDeleteById() {
		doNothing().when(paymentRepository).deleteById(1);
		
		paymentService.deleteById(1);
		
		verify(paymentRepository, times(1)).deleteById(1);
	}
}
