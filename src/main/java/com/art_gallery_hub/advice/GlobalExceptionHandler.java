package com.art_gallery_hub.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            WebRequest request)
    {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String reason = ex.getReason();

        String requestDescription = request.getDescription(false);
        String path = requestDescription.startsWith("uri=")
                ? requestDescription.substring(4)
                : requestDescription;

        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toString(), // ISO 8601
                status.value(), // e.x 409
                status.getReasonPhrase(), // e.x, "Conflict"
                reason, // e.x "User already exists"
                path // e.x "/api/auth/register-artist"
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
