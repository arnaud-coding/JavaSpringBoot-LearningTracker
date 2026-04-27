package com.cefii.learning.learning_tracker.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String identifier) {
        super(String.format("%s not found with identifer: %s", resource, identifier));
    }
}
