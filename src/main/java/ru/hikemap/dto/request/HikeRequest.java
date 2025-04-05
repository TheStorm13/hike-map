package ru.hikemap.dto.request;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

public record HikeRequest(
  String title,
  String description,
  LocalDate startDate,
  LocalDate endDate
  //,
  //  MultipartFile photo,
  //  MultipartFile trackGpx,
  //  MultipartFile reportPdf
) {}
