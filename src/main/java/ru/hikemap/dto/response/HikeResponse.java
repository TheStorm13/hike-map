package ru.hikemap.dto.response;

import java.time.LocalDate;

public record HikeResponse(
  Long id,
  String title,
  String description,
  String photoPath,
  String trackGpxPath,
  String reportPdfPath,
  LocalDate startDate,
  LocalDate endDate,
  int difficulty,
  boolean is_categorical,
  String area,
  String hikeType,
  String organizer
) {}
