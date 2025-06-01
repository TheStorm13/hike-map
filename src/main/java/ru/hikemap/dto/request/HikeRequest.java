package ru.hikemap.dto.request;

import java.time.LocalDate;

public record HikeRequest(
  String title,
  String description,
  LocalDate startDate,
  LocalDate endDate,
  Long organizerId,
  Long areaId,
  Long hikeTypeId,
  int difficulty,
  boolean isCategorical
) {}
