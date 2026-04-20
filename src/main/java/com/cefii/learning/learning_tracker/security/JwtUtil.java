package com.cefii.learning.learning_tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {
    // -----ATTRIBUTES-----
    private final SecretKey secretKey; // injecter via @Value ou constructeur
    private final long expirationTime; // injecter via @Value ou constructeur

    // -----CONSTRUCTOR-----
    public JwtUtil(@Value("${security.jwt.secret-key}") String rawSecret, // 1. Inject the secret key as a string
            @Value("${security.jwt.expiration-time}") long expirationTime) {

        this.expirationTime = expirationTime;

        // 2.Decode the string
        byte[] keyBytes = java.util.Base64.getDecoder().decode(rawSecret);

        // 3. Create a SecretKey object from the decoded byte array
        // hmacShaKeyFor() returns a SecretKey object suitable for HMAC-SHA algorithms,
        // using the provided byte array as the key material.
        this.secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    // -----METHODS-----
    // Method to extract username (subject) from the token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Method to generate a new token JWT
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .header()
                .add("typ", "JWT") // Optional: sets the type header
                .and()
                .subject(userDetails.getUsername()) // defines the subject (username)
                .issuedAt(new Date(System.currentTimeMillis())) // issue date
                .expiration(new Date(System.currentTimeMillis() + expirationTime))// expiration date
                .signWith(secretKey) // sign the token with the secret key using the default HS256 algorithm
                .compact(); // constructs the JWT and serializes it to a compact, URL-safe string
    }

    // Method to validate a token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration); // Extracts the expiration date from the
                                                                            // token
        return expiration.before(new Date());
    }

    // Utility method to extract a specific claim from a token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Method to extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
