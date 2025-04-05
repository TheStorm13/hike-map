package ru.hikemap.dto.response;

import java.time.LocalDate;
import org.geolatte.geom.LineString;
import ru.hikemap.entity.Hike.Area;
import ru.hikemap.entity.Hike.DifficultyCategory;
import ru.hikemap.entity.Hike.HikeType;
import ru.hikemap.entity.User;

public record HikeResponse(
  Long id,
  String title,
  String photoPath,
  LocalDate startDate,
  LocalDate endDate,
  String trackGpxPath,
  LineString trackGeometry,
  String reportPdfPath,
  User organizer,
  Area area,
  DifficultyCategory difficulty,
  HikeType hikeType
) {}
