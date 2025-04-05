package ru.hikemap.controller.hike;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

  @GetMapping
  public ResponseEntity<List<HikeResponse>> getHikes() {
    return ResponseEntity.ok(hikeService.getAllHikes());
  }

  @GetMapping("/all")
  public ResponseEntity<List<HikeResponse>> getAllHikes() {
    return ResponseEntity.ok(hikeService.getAllHikes());
  }

  @PostMapping
  public HikeResponse createHike(
    @PathVariable String title,
    @PathVariable String description,
    @PathVariable LocalDate startDate,
    @PathVariable LocalDate endDate,
    @RequestPart(required = false) MultipartFile photo,
    @RequestPart(required = false) MultipartFile trackGpx,
    @RequestPart(required = false) MultipartFile reportPdf
  ) {
    //todo: add file processing logic
    return hikeService.createHike(
      title,
      description,
      startDate,
      endDate,
      photo,
      trackGpx,
      reportPdf
    );
  }

  @PutMapping(value = "/{id}")
  public HikeResponse updateHike(
    @PathVariable Long id,
    @PathVariable(required = false) String title,
    @PathVariable(required = false) String description,
    @PathVariable(required = false) LocalDate startDate,
    @PathVariable(required = false) LocalDate endDate,
    @RequestPart(required = false) MultipartFile photo,
    @RequestPart(required = false) MultipartFile trackGpx,
    @RequestPart(required = false) MultipartFile reportPdf
  ) {
    return hikeService.updateHike(
      id,
      title,
      description,
      startDate,
      endDate,
      photo,
      trackGpx,
      reportPdf
    );
  }

  @DeleteMapping("/{id}")
  public void deleteHike(@PathVariable Long id) {
    hikeService.deleteHike(id);
  }
}
