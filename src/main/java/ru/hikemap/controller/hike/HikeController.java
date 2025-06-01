package ru.hikemap.controller.hike;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.request.HikeRequest;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.service.hike.HikeService;

@RestController
@RequestMapping("/hikes")
@RequiredArgsConstructor
public class HikeController {

  private final HikeService hikeService;

  @GetMapping("/{hikeId}")
  public ResponseEntity<HikeResponse> getHikeDetails(
    @PathVariable Long hikeId
  ) {
    return ResponseEntity.ok(hikeService.getHike(hikeId));
  }

  @GetMapping("/all")
  public ResponseEntity<List<HikeResponse>> getAllHikes() {
    return ResponseEntity.ok(hikeService.getAllHikes());
  }

  @GetMapping("/filters")
  public ResponseEntity<List<HikeResponse>> getFilteredHikes(
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) LocalDate startDateFrom,
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) LocalDate startDateTo,
    @RequestParam(required = false) Integer difficulty,
    @RequestParam(required = false) Boolean isCategorical,
    @RequestParam(required = false) Long areaId,
    @RequestParam(required = false) Long hikeTypeId,
    @RequestParam(required = false) Long organizerId
  ) {
    List<HikeResponse> hikes = hikeService.getFilteredHikes(
      startDateFrom,
      startDateTo,
      difficulty,
      isCategorical,
      areaId,
      hikeTypeId,
      organizerId
    );

    return ResponseEntity.ok(hikes);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<Long> createHike(@RequestBody HikeRequest hikeRequest) {
    Long savedHikeId = hikeService.createHike(hikeRequest);
    return new ResponseEntity<>(savedHikeId, HttpStatus.CREATED);
  }

  @PutMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public HikeResponse updateHike(
    @PathVariable Long id,
    @PathVariable(required = false) String title,
    @PathVariable(required = false) String description,
    @PathVariable(required = false) LocalDate startDate,
    @PathVariable(required = false) LocalDate endDate
  ) {
    return hikeService.updateHike(id, title, description, startDate, endDate);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public void deleteHike(@PathVariable Long id) {
    hikeService.deleteHike(id);
  }
}
