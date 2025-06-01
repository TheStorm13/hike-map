package ru.hikemap.dto.error;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private final int status;
  private final String message;
  private final ZonedDateTime timestamp;
}
