package com.cefii.learning.learning_tracker.configuration;

import com.cefii.learning.learning_tracker.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to register custom interceptors
 * Interceptors are components that run before (and after) each request
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    /**
     * This method tells Spring to use our rate limiting interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                // Apply rate limiting to ALL API endpoints
                .addPathPatterns("/api/**")
                // EXCLUDE auth endpoints from rate limiting
                .excludePathPatterns(
                        "/api/auth/**", // Login, logout endpoints
                        "/api/users/register" // Registration endpoint
                );
    }
}