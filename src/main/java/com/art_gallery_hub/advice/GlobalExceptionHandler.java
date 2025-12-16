package com.art_gallery_hub.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    // General response format for all errors
    public record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    // Special response format for validation errors (contains a list of details)
    public record ValidationDetailedResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path,
            List<FieldError> details
    ) {}

    // List of specific errors
    public record FieldError(
            String field,
            String message
    ) {}

    // --- 1 . Handling standard ResponseStatusException (404, 409, etc.) ---
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            WebRequest request)
    {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String reason = ex.getReason();
        String path = extractPath(request);

        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toString(), // ISO 8601
                status.value(), // e.x 409
                status.getReasonPhrase(), // e.x, "Conflict"
                reason, // e.x "User already exists"
                path // e.x "/api/auth/register-artist"
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    // --- 2. DTO Validation Error Handling (@Valid, @NotBlank, etc.) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationDetailedResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request)
    {
        HttpStatus status = HttpStatus.BAD_REQUEST; // Always 400
        String path = extractPath(request);

        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(
                        error.getField(),
                        // Using the message from the @NotBlank annotation(message="...")
                        error.getDefaultMessage()
                ))
                .toList();

        ValidationDetailedResponse errorResponse = new ValidationDetailedResponse(
                Instant.now().toString(),
                status.value(), // 400
                status.getReasonPhrase(), // Bad Request
                "Validation failed for input data.", // General message
                path, // e.x "/api/artist/profile"
                errors
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    // Additional method for extracting a clean URI path
    private String extractPath(WebRequest request) {
        String requestDescription = request.getDescription(false);
        return requestDescription.startsWith("uri=")
                ? requestDescription.substring(4)
                : requestDescription;
    }
}
