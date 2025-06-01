package ru.hikemap.controller.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.request.HikeTypeRequest;
import ru.hikemap.dto.response.HikeTypeResponse;
import ru.hikemap.exception.exceptions.ValidationException;
import ru.hikemap.service.hike.HikeTypeService;

@RestController
@RequestMapping("/hikes/types")
@RequiredArgsConstructor
public class HikeTypeController {

  private final HikeTypeService hikeTypeService;

  @PostMapping
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<HikeTypeResponse> createHikeType(
    @RequestBody HikeTypeRequest hikeTypeRequest
  ) {
    return ResponseEntity.ok(hikeTypeService.createHikeType(hikeTypeRequest));
  }

  @GetMapping
  public ResponseEntity<List<HikeTypeResponse>> getAllHikeType() {
    return ResponseEntity.ok(hikeTypeService.getAllHikeType());
  }

  @GetMapping("/{id}")
  public ResponseEntity<HikeTypeResponse> getHikeTypeById(
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(hikeTypeService.getHikeTypeById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<HikeTypeResponse> updateHikeType(
    @PathVariable Long id,
    @RequestBody HikeTypeRequest hikeTypeRequest
  ) {
    if (!id.equals(hikeTypeRequest.id())) {
      throw new ValidationException("Path ID and body ID must match");
    }
    return ResponseEntity.ok(
      hikeTypeService.updateHikeType(id, hikeTypeRequest)
    );
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<Void> deleteHikeType(@PathVariable Long id) {
    hikeTypeService.deleteHikeType(id);
    return ResponseEntity.noContent().build();
  }
}
