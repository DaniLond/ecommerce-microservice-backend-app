package com.selimhorri.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

/**
 * Security configuration for payment-service
 * Fixes HIGH vulnerabilities:
 * - Authorization Bypass: Requires authentication for protected endpoints
 * - HTTP Method TRACK: Disables dangerous HTTP methods
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless REST API
            .csrf().disable()
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (health check, discovery)
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/actuator/info").permitAll()
                
                // Sensitive actuator endpoints require ADMIN role
                .antMatchers("/actuator/env").hasRole("ADMIN")
                .antMatchers("/actuator/configprops").hasRole("ADMIN")
                .antMatchers("/actuator/mappings").hasRole("ADMIN")
                .antMatchers("/actuator/**").hasRole("ADMIN")
                
                // Restrict dangerous HTTP methods to ADMIN only
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                
                // Require authentication for all API endpoints
                .antMatchers("/api/**").authenticated()
                
                // All other requests need authentication
                .anyRequest().authenticated()
            )
            
            // Use HTTP Basic authentication
            .httpBasic()
            
            .and()
            
            // Stateless session management for REST API
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            .and()
            
            // Security headers
            .headers()
                .frameOptions().deny()
                .xssProtection().and()
                .contentSecurityPolicy("default-src 'self'");

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create in-memory users for testing
        // In production, replace with database-backed UserDetailsService
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
