package com.selimhorri.app.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter to block dangerous HTTP methods at the API Gateway level
 * This prevents TRACE, TRACK, and OPTIONS methods from reaching backend services
 */
@Component
public class HttpMethodGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        
        // HttpMethod may be null for non-standard methods like TRACK
        if (httpMethod == null) {
            exchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return exchange.getResponse().setComplete();
        }
        
        String method = httpMethod.name();
        
        // Block dangerous HTTP methods
        if ("TRACE".equalsIgnoreCase(method) || 
            "TRACK".equalsIgnoreCase(method) ||
            "OPTIONS".equalsIgnoreCase(method)) {
            
            exchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return exchange.getResponse().setComplete();
        }
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Execute this filter with highest priority
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
