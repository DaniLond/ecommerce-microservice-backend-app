package com.selimhorri.app.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filter to block dangerous HTTP methods (TRACE, TRACK, OPTIONS)
 * Fixes HIGH vulnerability: HTTP Method TRACK allowed
 */
@Component
@Order(1)
public class HttpMethodFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String method = httpRequest.getMethod();
        
        // Block dangerous HTTP methods
        if ("TRACE".equalsIgnoreCase(method) || 
            "TRACK".equalsIgnoreCase(method) ||
            "OPTIONS".equalsIgnoreCase(method)) {
            httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 
                "HTTP method " + method + " is not allowed");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
