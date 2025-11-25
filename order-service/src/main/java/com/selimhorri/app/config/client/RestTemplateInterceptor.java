package com.selimhorri.app.config.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Interceptor that adds Basic Authentication headers to outgoing RestTemplate requests.
 * This ensures that inter-service communication includes proper authentication credentials.
 */
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, 
                                       ClientHttpRequestExecution execution) throws IOException {
        // Add Basic Authentication header to all outgoing requests
        String auth = USERNAME + ":" + PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
        request.getHeaders().set("Authorization", authHeader);
        
        return execution.execute(request, body);
    }
}
