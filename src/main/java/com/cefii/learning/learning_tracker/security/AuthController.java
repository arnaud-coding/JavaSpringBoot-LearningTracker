package com.cefii.learning.learning_tracker.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController // Indicates that this is a REST Controller
@RequestMapping("/api/auth") // Defines the basic path for the requests to this controller
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager; // Inject the authentication manager
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) { // Receives an AuthRequest object
        // Tries to authenticate the user with given username and password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // If authentication succeeds, loads user's details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Generates a JWT token for the authenticated user
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt); // Returns le JWT token in the HTTP response OK 200
    }
}
