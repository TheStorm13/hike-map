package ru.hikemap.controller.hike;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hikemap.service.files.FileService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PostMapping("/upload")
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<String> uploadFile(
    @RequestParam("hikeId") Long hikeId,
    @RequestParam("file") MultipartFile file,
    @RequestParam("type") String type
  ) {
    fileService.saveFile(hikeId, file, type);
    return ResponseEntity.ok("File uploaded successfully.");
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(
    @RequestParam("hikeId") Long hikeId,
    @RequestParam("type") String type
  ) {
    Resource file = fileService.loadFile(hikeId, type);

    return ResponseEntity.ok()
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\""
      )
      .body(file);
  }
}
