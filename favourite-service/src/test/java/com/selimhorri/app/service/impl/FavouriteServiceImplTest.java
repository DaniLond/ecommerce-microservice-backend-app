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

import java.time.LocalDateTime;
import org.springframework.web.client.RestTemplate;
import com.selimhorri.app.domain.Favourite;
import com.selimhorri.app.domain.id.FavouriteId;
import com.selimhorri.app.dto.FavouriteDto;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.repository.FavouriteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavouriteService Tests")
class FavouriteServiceImplTest {
	
	@Mock
	private FavouriteRepository favouriteRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private FavouriteServiceImpl favouriteService;
	
	private Favourite favourite;
	private FavouriteDto favouriteDto;
	private FavouriteId favouriteId;
	
	@BeforeEach
	void setUp() {
		favouriteId = new FavouriteId(1, 1, LocalDateTime.now());
		
		favourite = new Favourite();
		favourite.setUserId(1);
		favourite.setProductId(1);
		favourite.setLikeDate(LocalDateTime.now());
		
		favouriteDto = new FavouriteDto();
		favouriteDto.setUserId(1);
		favouriteDto.setProductId(1);
		favouriteDto.setLikeDate(LocalDateTime.now());
	}
	
	@Test
	@DisplayName("Should find all favourites")
	void testFindAll() {
		when(favouriteRepository.findAll()).thenReturn(Arrays.asList(favourite));
		when(restTemplate.getForObject(anyString(), eq(ProductDto.class))).thenReturn(new ProductDto());
		when(restTemplate.getForObject(anyString(), eq(UserDto.class))).thenReturn(new UserDto());
		
		assertNotNull(favouriteService.findAll());
		verify(favouriteRepository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Should find favourite by id")
	void testFindById() {
		when(favouriteRepository.findById(any())).thenReturn(Optional.of(favourite));
		when(restTemplate.getForObject(anyString(), eq(ProductDto.class))).thenReturn(new ProductDto());
		when(restTemplate.getForObject(anyString(), eq(UserDto.class))).thenReturn(new UserDto());
		
		FavouriteDto result = favouriteService.findById(favouriteId);
		
		assertNotNull(result);
	}
	
	@Test
	@DisplayName("Should save favourite")
	void testSave() {
		when(favouriteRepository.save(any(Favourite.class))).thenReturn(favourite);
		
		FavouriteDto result = favouriteService.save(favouriteDto);
		
		assertNotNull(result);
		verify(favouriteRepository, times(1)).save(any(Favourite.class));
	}
	
	@Test
	@DisplayName("Should update favourite")
	void testUpdate() {
		when(favouriteRepository.save(any(Favourite.class))).thenReturn(favourite);
		
		FavouriteDto result = favouriteService.update(favouriteDto);
		
		assertNotNull(result);
		verify(favouriteRepository, times(1)).save(any(Favourite.class));
	}
	
	@Test
	@DisplayName("Should delete favourite")
	void testDeleteById() {
		doNothing().when(favouriteRepository).deleteById(any());
		
		favouriteService.deleteById(favouriteId);
		
		verify(favouriteRepository, times(1)).deleteById(any());
	}
}
