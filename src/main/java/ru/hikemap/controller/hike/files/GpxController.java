package ru.hikemap.controller.hike.files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hikemap.service.hike.files.GpxService;

@RestController
@RequestMapping("/files")
public class GpxController {

  @GetMapping(value = "/gpx/{hikeId}")
  public ResponseEntity<Resource> getGpxFile(@PathVariable Long hikeId) {
    Resource gpxResource = GpxService.downloadGpxFile(hikeId);
    String filename = String.format("hike_%d.gpx", hikeId);

    return ResponseEntity.ok()
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + filename + "\""
      )
      .body(gpxResource);
  }
}
