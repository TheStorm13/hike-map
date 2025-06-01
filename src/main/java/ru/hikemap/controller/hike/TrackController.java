package ru.hikemap.controller.hike;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hikemap.dto.response.HikeGeoJsonResponse;
import ru.hikemap.service.hike.TrackService;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {

  private final TrackService trackService;

  @GetMapping("/geojson")
  public ResponseEntity<HikeGeoJsonResponse> getHikesGeoJson(
    @RequestParam String hikeIds
  ) { // Получаем строку с ID походов
    List<Long> hikeIdList = Arrays.stream(hikeIds.split(","))
      .map(Long::parseLong) // Исправлено на Long для соответствия типу
      .collect(Collectors.toList()); // Преобразуем строку в список
    return ResponseEntity.ok(trackService.getTracks(hikeIdList)); // Передаем список вместо строки
  }
}
