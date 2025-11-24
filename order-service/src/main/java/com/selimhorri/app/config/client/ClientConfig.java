package com.selimhorri.app.config.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class ClientConfig {
	
	private final RestTemplateInterceptor restTemplateInterceptor;
	
	public ClientConfig(RestTemplateInterceptor restTemplateInterceptor) {
		this.restTemplateInterceptor = restTemplateInterceptor;
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplateBean() {
		RestTemplate restTemplate = new RestTemplate();
		// Add interceptor to include authentication in inter-service calls
		restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor));
		return restTemplate;
	}
	
	
	
}










