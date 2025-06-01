package ru.hikemap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hikemap.dto.error.ErrorResponse;
import ru.hikemap.exception.exceptions.ConflictException;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.exception.exceptions.UnauthorizedException;
import ru.hikemap.exception.exceptions.ValidationException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex
    ) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex
    ) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(
            {ValidationException.class, MethodArgumentNotValidException.class}
    )
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex
    ) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception ex,
            HttpStatus status
    ) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                ZonedDateTime.now(ZoneOffset.UTC)
        );
        return new ResponseEntity<>(error, status);
    }
}
