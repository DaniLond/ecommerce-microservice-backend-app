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
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.exception.wrapper.ProductNotFoundException;
import com.selimhorri.app.service.ProductService;

@WebMvcTest(ProductResource.class)
@DisplayName("ProductResource Tests")
class ProductResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProductService productService;
	
	private ProductDto productDto;
	
	@BeforeEach
	void setUp() {
		productDto = new ProductDto();
		productDto.setProductId(1);
		productDto.setProductTitle("Test Product");
		productDto.setImageUrl("http://image.url");
		productDto.setSku("SKU123");
		productDto.setPriceUnit(99.99);
		productDto.setQuantity(10);
	}
	
	@Test
	@DisplayName("GET /api/products - Should return all products")
	void testFindAll() throws Exception {
		// Given
		when(productService.findAll()).thenReturn(Arrays.asList(productDto));
		
		// When & Then
		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.collection").isArray());
		
		verify(productService, times(1)).findAll();
	}
	
	@Test
	@DisplayName("GET /api/products/{id} - Should return product")
	void testFindById() throws Exception {
		// Given
		when(productService.findById(1)).thenReturn(productDto);
		
		// When & Then
		mockMvc.perform(get("/api/products/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productId").value(1));
		
		verify(productService, times(1)).findById(1);
	}
	
	@Test
	@DisplayName("GET /api/products/{id} - Should return 404 when not found")
	void testFindByIdNotFound() throws Exception {
		// Given
		when(productService.findById(999)).thenThrow(
			new ProductNotFoundException("Product with id: 999 not found")
		);
		
		// When & Then
		mockMvc.perform(get("/api/products/999"))
			.andExpect(status().isNotFound());
		
		verify(productService, times(1)).findById(999);
	}
	
	@Test
	@DisplayName("POST /api/products - Should create product")
	void testSave() throws Exception {
		// Given
		when(productService.save(any(ProductDto.class))).thenReturn(productDto);
		
		// When & Then
		mockMvc.perform(post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDto)))
			.andExpect(status().isCreated());
		
		verify(productService, times(1)).save(any(ProductDto.class));
	}
	
	@Test
	@DisplayName("PUT /api/products - Should update product")
	void testUpdate() throws Exception {
		// Given
		when(productService.update(any(ProductDto.class))).thenReturn(productDto);
		
		// When & Then
		mockMvc.perform(put("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDto)))
			.andExpect(status().isOk());
		
		verify(productService, times(1)).update(any(ProductDto.class));
	}
	
	@Test
	@DisplayName("DELETE /api/products/{id} - Should delete product")
	void testDeleteById() throws Exception {
		// Given
		doNothing().when(productService).deleteById(1);
		
		// When & Then
		mockMvc.perform(delete("/api/products/1"))
			.andExpect(status().isOk());
		
		verify(productService, times(1)).deleteById(1);
	}
}
