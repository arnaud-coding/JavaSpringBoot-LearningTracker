package com.cefii.learning.learning_tracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean; // Base class for filters that are not specific to HTTP requests

import java.io.IOException;

@Component // Marks this class as a Spring component
public class JwtAuthFilter extends GenericFilterBean { // Extends GenericFilterBean to create a custom filter
    @Autowired
    private JwtUtil jwtUtil; // Injects JwtUtil to handle JWT operations
    @Autowired
    private CustomUserDetailsService userDetailsService; // Injects user details service to load user informations

    @Override // Principal method of the filter that processes each request
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request; // Cast to HttpServletRequest
        final String authHeader = httpRequest.getHeader("Authorization"); // Get the Authorization header

        final String jwt;
        final String username;

        // checks if the header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response); // continues the filter chain if no valid token is found
            return;
        }

        jwt = authHeader.substring(7); // Extracts the JWT token from the header
        username = jwtUtil.extractUserName(jwt); // Extracts the username from the JWT token

        // If the username is not null and if there is no authentication already set in the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Loads user details using the username

            if (jwtUtil.isTokenValid(jwt, userDetails)) { // Validates the token against the user details
                // Creates an authentication token with the user details and authorities
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Sets additional details for the authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                // Sets the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response); // Continues the filter chain after processing the JWT token
    }

}
