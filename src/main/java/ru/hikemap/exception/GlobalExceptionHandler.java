package ru.hikemap.exception;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hikemap.dto.response.error.ErrorResponse;
import ru.hikemap.exception.exceptions.ConflictException;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.exception.exceptions.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
    ResourceNotFoundException ex
  ) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
    return buildErrorResponse(ex, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(
    { ValidationException.class, MethodArgumentNotValidException.class }
  )
  public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
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
