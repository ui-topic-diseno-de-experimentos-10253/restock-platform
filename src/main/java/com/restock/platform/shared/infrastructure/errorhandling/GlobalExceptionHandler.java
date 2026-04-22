package com.restock.platform.shared.infrastructure.errorhandling;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.restock.platform.shared.domain.exceptions.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Global exception handler for REST API.
 * <p>
 * Handles common exceptions like JSON parsing issues and validation errors,
 * returning standardized error responses to the client.
 * </p>
 *
 * @author Julio Castro Alejos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link HttpMessageNotReadableException}, typically caused by malformed JSON input.
     * <p>
     * If the root cause is an {@link InvalidFormatException}, it extracts and reports the invalid value and target type.
     *
     * @param ex the exception thrown by Spring during deserialization
     * @return a {@link ResponseEntity} with HTTP 400 status and an error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            var targetType = ife.getTargetType().getSimpleName();
            var invalidValue = ife.getValue();
            message = String.format("Invalid value '%s' for type '%s'.", invalidValue, targetType);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    /**
     * Handles {@link IllegalArgumentException}, typically thrown from domain or application logic validations.
     *
     * @param ex the exception containing a descriptive message
     * @return a {@link ResponseEntity} with HTTP 400 status and the validation error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }
}
