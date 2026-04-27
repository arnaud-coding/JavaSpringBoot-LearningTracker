// Centralized Exception handler for all controllers
package com.cefii.learning.learning_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * HANDLER 1: Resource Not Found (404)
     * Triggered when: You try to fetch/update/delete a user/course that doesn't exist
     * Example: GET /api/users/999 where user 999 doesn't exist
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value()) // 404 status code for not found resources
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .details(null)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * HANDLER 2: Configuration Exception (500)
     * Triggered when: There's a critical misconfiguration in the application (missing properties, invalid bean setup)
     * Exemple: Database credentials not set, missing required beans
     */
    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleConfigurationException(ConfigurationException ex, WebRequest request) {

        log.error("Configuration error: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) // 500 status code for server errors
                .error("    CONFIGURATION_ERROR")
                .message("Application configuration is invalid. Please check the application logs for details.")
                .path(request.getDescription(false).replace("uri=", ""))
                .details(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * HANDLER 3: Validation Errors (400)
     * Triggered when: Input data fails validation constraints (when @Valid annotation used in controller methods fails)
     * Example: POST /api/users with missing required fields or invalid email format
     * This is from ResponseEntityHandler, we overrife the default method to provide a custom error response format
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        log.error("Validation failed: {}", ex.getMessage());

        // Extract all validation error messages
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value()) // 400 status code for client errors
                .error("VALIDATION_FAILED")
                .message("Invalid input data provided.")
                .path(request.getDescription(false).replace("uri=", ""))
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * HANDLER 4: Type mismatch errors (400)
     * Triggered when: A request parameter or path variable cannot be converted to the expected type (request parameters have wrong type)
     * Example: GET /api/users/abc where the user ID is expected to be a number
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

        log.error("Type mismatch: {}", ex.getMessage());

        String detail = String.format("Parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value()) // 400 status code for client errors
                .error("TYPE_MISMATCH")
                .message("Invalid parameter type provided.")
                .path(request.getDescription(false).replace("uri=", ""))
                .details(List.of(detail))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * HANDLER 5: Generic Catch-all Exception Handler (500)
     * Triggered when: ANY unexpected exception occurs that isn't handled by the specific handlers above (fallback for unhandled exceptions)
     * Example: Null pointer exception, database connection failure, etc.
     * This ensures that even if an unforeseen error occurs, the client receives a consistent error response format instead of a generic server error page.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(Exception ex, WebRequest request) {

        log.error("Unexpected error: {}", ex.getMessage(), ex); // Log full stack trace for debugging

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) // 500 status code for server errors
                .error("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred. Please contact support.")
                .path(request.getDescription(false).replace("uri=", ""))
                .details(List.of(ex.getClass().getSimpleName() + ": " + ex.getMessage())) // Include exception class and message in details for debugging
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * HANDLER 6: Rate Limit Exceeded (429)
     * Triggered when: A user exceeds the allowed number of requests in a given time frame (100 per minute in our case)
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(
            RateLimitExceededException ex, WebRequest request) {

        log.warn("Rate limit exceeded: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.TOO_MANY_REQUESTS.value()) // 429
                .error("RATE_LIMIT_EXCEEDED")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .details(null)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

}