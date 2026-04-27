package com.cefii.learning.learning_tracker.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cefii.learning.learning_tracker.exception.RateLimitExceededException;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;

/**
 * Rate Limiting Interceptor using Bucket4j
 *
 * This interceptor enforces per-user rate limiting by:
 * 1. Extracting the authenticated username from JWT token (via Spring Security context)
 * 2. Maintaining a "bucket" of tokens for each user (100 tokens per minute)
 * 3. Consuming 1 token per request
 * 4. Blocking requests when the bucket is empty
 */
@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    /**
     * Map to store rate limiter buckets per user
     * Key: Username (extracted from JWT token)
     * Value: Bucket4j rate limiter bucket
     *
     * ConcurrentHashMap is thread-safe for handling concurrent requests
     */
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * This method runs BEFORE each controller method is invoked
     * It checks if the user has exceeded their rate limit
     *
     * @return true if request is allowed, false if rate limit exceeded
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // Get the authenticated user from Spring Security context
        String username = extractAuthenticatedUsername();

        if (username == null) {
            // Unauthenticated requests - use IP address
            username = extractClientIpAddress(request);
            log.debug("Unauthenticated request, using IP: {}", username);
        }

        log.debug("Rate limit check for user: {}", username);

        // Get or create a rate limiter bucket for this user
        Bucket bucket = buckets.computeIfAbsent(username, k -> createNewBucket());

        // Try to consume 1 token (each request uses 1 token)
        if (bucket.tryConsume(1)) {
            // Token consumed successfully - request is allowed
            log.debug("Rate limit OK for user: {}. Tokens remaining in bucket.", username);
            return true;
        } else {
            // Rate limit exceeded - reject request
            log.warn("Rate limit EXCEEDED for user: {}", username);
            throw new RateLimitExceededException(
                    "Too many requests. Rate limit: 100 requests per minute. Please try again later.");
        }
    }

    /**
     * Creates a new rate limiter bucket with these settings:
     * - 100 requests allowed
     * - Per 1 minute period
     * - Tokens refill at regular intervals
     *
     * How it works:
     * - Each request consumes 1 token
     * - Tokens refill to 100 every 1 minute
     * - Burst allowed up to 100 requests
     */
    private Bucket createNewBucket() {
        // Define the bandwidth limit using builder: 100 tokens per 1 minute
        // In Bucket4j 8.7.0, use Bandwidth.builder() instead of deprecated simple()
        Bandwidth limit = Bandwidth.builder()
                .capacity(100) // Maximum 100 tokens in the bucket
                .refillGreedy(100, Duration.ofMinutes(1)) // Refill 100 tokens every 1 minute
                .build();

        // Build and return a new bucket with the specified configuration
        Bucket bucket = Bucket.builder()
                .addLimit(limit)
                .build();

        log.debug("Created new rate limit bucket: 100 requests per minute");
        return bucket;
    }

    /**
     * Extracts the authenticated username from Spring Security context
     * The username was set there by JwtAuthFilter during JWT validation
     *
     * @return Username if authenticated, null if not authenticated
     */
    private String extractAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Get the principal (username) from the authentication object
            String username = authentication.getName();

            // "anonymousUser" means no real authentication
            if (username != null && !username.equals("anonymousUser")) {
                return username;
            }
        }

        return null;
    }

    /**
     * Gets the client's IP address from the request
     * Used as fallback for unauthenticated requests
     *
     * Tries multiple headers in order of preference:
     * 1. X-Forwarded-For (proxy/load balancer)
     * 2. X-Real-IP (reverse proxy)
     * 3. Direct remote address
     */
    private String extractClientIpAddress(HttpServletRequest request) {
        // Check for X-Forwarded-For header (used by proxies)
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, take the first one
            return xForwardedFor.split(",")[0].trim();
        }

        // Check for X-Real-IP header (used by some reverse proxies)
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        // Fall back to direct remote address
        return request.getRemoteAddr();
    }
}