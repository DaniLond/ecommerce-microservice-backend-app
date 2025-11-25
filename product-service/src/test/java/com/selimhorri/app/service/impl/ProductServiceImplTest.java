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

import com.selimhorri.app.domain.Category;
import com.selimhorri.app.domain.Product;
import com.selimhorri.app.dto.CategoryDto;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.exception.wrapper.ProductNotFoundException;
import com.selimhorri.app.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceImplTest {
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	private Product product;
	private ProductDto productDto;
	
	@BeforeEach
	void setUp() {
		// Create Category
		Category category = new Category();
		category.setCategoryId(1);
		category.setCategoryTitle("Electronics");
		
		product = new Product();
		product.setProductId(1);
		product.setProductTitle("Test Product");
		product.setImageUrl("http://image.url");
		product.setSku("SKU123");
		product.setPriceUnit(99.99);
		product.setQuantity(10);
		product.setCategory(category);
		
		// Create CategoryDto
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryId(1);
		categoryDto.setCategoryTitle("Electronics");
		
		productDto = new ProductDto();
		productDto.setProductId(1);
		productDto.setProductTitle("Test Product");
		productDto.setImageUrl("http://image.url");
		productDto.setSku("SKU123");
		productDto.setPriceUnit(99.99);
		productDto.setQuantity(10);
		productDto.setCategoryDto(categoryDto);
	}
	
	@Test
	@DisplayName("Should find all products successfully")
	void testFindAll() {
		// Given
		when(productRepository.findAll()).thenReturn(Arrays.asList(product));
		
		// When
		List<ProductDto> result = productService.findAll();
		
		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(productRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find product by id successfully")
	void testFindById() {
		// Given
		when(productRepository.findById(1)).thenReturn(Optional.of(product));
		
		// When
		ProductDto result = productService.findById(1);
		
		// Then
		assertNotNull(result);
		assertEquals(1, result.getProductId());
		assertEquals("Test Product", result.getProductTitle());
		verify(productRepository, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("Should throw exception when product not found")
	void testFindByIdNotFound() {
		// Given
		when(productRepository.findById(999)).thenReturn(Optional.empty());
		
		// When & Then
		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class,
			() -> productService.findById(999)
		);
		
		assertTrue(exception.getMessage().contains("Product with id: 999 not found"));
		verify(productRepository, times(1)).findById(999);
	}
	
	@Test
	@DisplayName("Should save product successfully")
	void testSave() {
		// Given
		when(productRepository.save(any(Product.class))).thenReturn(product);
		
		// When
		ProductDto result = productService.save(productDto);
		
		// Then
		assertNotNull(result);
		assertEquals("Test Product", result.getProductTitle());
		verify(productRepository, times(1)).save(any(Product.class));
	}
	
	@Test
	@DisplayName("Should update product successfully")
	void testUpdate() {
		// Given
		when(productRepository.save(any(Product.class))).thenReturn(product);
		
		// When
		ProductDto result = productService.update(productDto);
		
		// Then
		assertNotNull(result);
		verify(productRepository, times(1)).save(any(Product.class));
	}
	
	@Test
	@DisplayName("Should delete product by id successfully")
	void testDeleteById() {
		// Given
		when(productRepository.findById(1)).thenReturn(Optional.of(product));
		doNothing().when(productRepository).delete(any(Product.class));
		
		// When
		productService.deleteById(1);
		
		// Then
		verify(productRepository, times(1)).findById(1);
		verify(productRepository, times(1)).delete(product);
	}
}
