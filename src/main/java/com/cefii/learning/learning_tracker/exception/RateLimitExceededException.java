package com.cefii.learning.learning_tracker.exception;

/**
 * Exception thrown when a user exceeds their rate limit quota
 * This will be caught by GlobalExceptionHandler and return a 429 status
 */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}
